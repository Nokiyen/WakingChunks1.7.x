package noki.wakingchunks.cc;

import cpw.mods.fml.common.Optional.Interface;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noki.wakingchunks.ChunkManager;
import noki.wakingchunks.chunkawaker.ChunkAwakerTile;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;


/**********
 * @class PeripheralChunkAwaker
 *
 * @description
 * @description_en
 */
@Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft", striprefs = true)
public class PeripheralChunkAwaker extends ChunkAwakerTile implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private int blockState;
	private int level;
	private boolean activated;

	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralChunkAwaker() {
		
	}
	
	public PeripheralChunkAwaker(World world) {
		
		this.worldObj = world;
		
		this.updateBlockMetadata();
		if(this.blockState < 3) {
			this.level = this.blockState;
			this.activated = true;
		}
		else {
			this.level = -1;
			this.activated = false;
		}
		
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
	public void attach(IComputerAccess computer) {
 
	}

	@Override
	public void detach(IComputerAccess computer) {
		
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
		
		this.updateBlockMetadata();
		
		int maxLevel = this.blockState<3 ? this.blockState : this.blockState-3;
		if(level > maxLevel) {
			return new Object[] {false};
		}
		
		this.level = level;
		
		return new Object[] {true};
		
	}
	
	private Object[] activate(Object[] arguments) {
		
		if(arguments.length < 1) {
			return new Object[] {false};
		}
		
		if(!(arguments[0] instanceof Boolean)) {
			return new Object[] {false};
		}
		
		this.updateBlockMetadata();
		boolean flag = (Boolean)arguments[0];
		String label = this.getLabel(this.worldObj);
		if(label == null) {
			return new Object[] {false};
		}
		
		if(flag == true) {
			boolean res = ChunkManager.loadChunks(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.level, label);
			if(res == true) {
				this.activated = true;
				if(this.blockState > 2) {
					this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.blockState-3, 3);
				}
				return new Object[] {true};
			}
			else {
				this.activated = false;
				if(this.blockState < 3) {
					this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.blockState+3, 3);
				}
				return new Object[] {false};
			}
		}
		else {
			ChunkManager.unloadChunks(this.worldObj, label);
			this.activated = false;
			if(this.blockState < 3) {
				this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.blockState+3, 3);
			}
			return new Object[] {true};
		}
		
	}
	
	private Object[] isActivated() {
		
		return new Object[] {this.activated};
		
	}
	
	private void updateBlockMetadata() {
		
		this.blockState = MathHelper.clamp_int(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord), 0, 5);
		
	}
	
	
}
