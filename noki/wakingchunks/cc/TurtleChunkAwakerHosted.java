package noki.wakingchunks.cc;

import net.minecraft.world.World;
import noki.wakingchunks.ChunkManager;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;


/**********
 * @class TurtleChunkAwakerHosted
 *
 * @description
 * @description_en
 */
public class TurtleChunkAwakerHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private ITurtleAccess turtle;
	private TurtleSide side;
	
	private String label = null;
	private int level = 0;
	private boolean activated = false;
	
	private int currentPosX;
	private int currentPosY;
	private int currentPosZ;
	private int prevPosX;
	private int prevPosY;
	private int prevPosZ;

	
	//******************************//
	// define member methods.
	//******************************//
	public TurtleChunkAwakerHosted(ITurtleAccess turtle, TurtleSide side) {
		
		this.turtle = turtle;
		this.side = side;
		
	}
	
	@Override
	public String getType() {
		
		return "ChunkAwaker";
		
	}

	@Override
	public String[] getMethodNames() {
		
		return new String[] { "setLevel", "activate", "isActivated" };
		
	}
 
	@Override
	public synchronized void attach(IComputerAccess computer) {
		
		this.prevPosX = this.turtle.getPosition().posX;
		this.prevPosY = this.turtle.getPosition().posY;
		this.prevPosZ = this.turtle.getPosition().posZ;
		this.currentPosX = this.prevPosX;
		this.currentPosY = this.prevPosY;
		this.currentPosZ = this.prevPosZ;
		
		this.level = this.turtle.getUpgradeNBTData(this.side).getInteger("level");

		String label = this.getLabel(this.turtle.getWorld());
		if(label != null) {
			this.activated = ChunkManager.loadChunks(this.turtle.getWorld(), 
					this.currentPosX, this.currentPosY, this.currentPosZ, this.level, label);
		}
		else {
			this.activated = false;
		}
 
	}

	@Override
	public synchronized void detach(IComputerAccess computer) {
		
		ChunkManager.log("on detach.");
		
		if(this.label != null) {
			this.turtle.getUpgradeNBTData(this.side).setString("label", "");
			this.turtle.updateUpgradeNBTData(this.side);
			ChunkManager.releaseTicket(this.turtle.getWorld(), this.label);
			this.label = null;
		}
		
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		
		return false;
		
	}
 
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {

		switch(method) {
			case 0:
				return this.setLevel(arguments);
			case 1:
				return this.activate(arguments);
			case 2:
				return this.isActivated();
			default:
				return new Object[] {false, "Failed for unknown reasons"};
		}
	}
	
	private Object[] setLevel(Object[] arguments) {
		
		if(arguments.length < 1) {
			return new Object[] {false};
		}
		
		if(!(arguments[0] instanceof Double)) {
			return new Object[] {false};
		}
		
		int level = (int)Math.round(((Double)arguments[0]).doubleValue());
		if(level < 0 || 2 < level) {
			return new Object[] {false};
		}
		
		this.level = level;
		this.turtle.getUpgradeNBTData(this.side).setInteger("level", this.level);
		this.turtle.updateUpgradeNBTData(this.side);
		
		return new Object[] {true};
		
	}
	
	private synchronized Object[] activate(Object[] arguments) {
		
		if(arguments.length < 1) {
			return new Object[] {false};
		}
		
		if(!(arguments[0] instanceof Boolean)) {
			return new Object[] {false};
		}
		
		boolean flag = (Boolean)arguments[0];
		String label = this.getLabel(this.turtle.getWorld());
		if(label == null) {
			return new Object[] {false};
		}
		
		if(flag == true) {
			this.activated = ChunkManager.loadChunks(this.turtle.getWorld(), this.turtle.getPosition().posX,
					this.turtle.getPosition().posY, this.turtle.getPosition().posZ, this.level, label);
			return new Object[] {this.activated};
		}
		else if(flag == false){
			ChunkManager.unloadChunks(this.turtle.getWorld(), label);
			this.activated = false;
			return new Object[] {true};
		}
		
		return new Object[] {true};

	}
	
	private Object[] isActivated() {
		
		return new Object[] {this.activated};
		
	}
	
	public synchronized void updateChunks() {
		
		this.prevPosX = this.currentPosX;
		this.prevPosY = this.currentPosY;
		this.prevPosZ = this.currentPosZ;
		this.currentPosX = this.turtle.getPosition().posX;
		this.currentPosY = this.turtle.getPosition().posY;
		this.currentPosZ = this.turtle.getPosition().posZ;
		
		int prevChunkX = this.prevPosX>>4;
		int prevChunkZ = this.prevPosZ>>4;
		int currentChunkX = this.currentPosX>>4;
		int currentChunkZ = this.currentPosZ>>4;
		
		if((currentChunkX != prevChunkX || currentChunkZ != prevChunkZ) && this.activated == true) {
			String label = this.getLabel(this.turtle.getWorld());
			if(label != null) {
				this.activated = ChunkManager.loadChunks(this.turtle.getWorld(),
						this.currentPosX, this.currentPosY, this.currentPosZ, this.level, label);
			}
		}
		
	}
	
	private synchronized String getLabel(World world) {
		
		if(this.label != null) {
			return this.label;
		}
		
		String label = this.turtle.getUpgradeNBTData(this.side).getString("label");
		if(!label.equals("")) {
			this.label = label;
			return label;
		}
		
		String newLabel = ChunkManager.requestLabel(world);
		if(newLabel == null) {
			return null;
		}
		else {
			this.turtle.getUpgradeNBTData(this.side).setString("label", newLabel);
			this.turtle.updateUpgradeNBTData(this.side);
			this.label = newLabel;
			return newLabel;
		}
		
	}
	
}
