package noki.wakingchunks.cc;
 
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import noki.wakingchunks.WakingChunksData;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class TurtleChunkAwaker
 *
 * @description
 * @descriptoin_en
 */
public class TurtleChunkAwaker implements ITurtleUpgrade {
	
	//******************************//
	// define member variables.
	//******************************//
	private ItemStack upgradeItem = new ItemStack(WakingChunksData.chunkAwaker, 1, 2);
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public int getUpgradeID() {
		
		return WakingChunksData.chunkAwakerTurtlePID;
		
	}
 
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Chunk Awaker";
		
	}

	@Override
	public TurtleUpgradeType getType() {
		
		return  TurtleUpgradeType.Peripheral;
		
	}
 
	@Override
	public ItemStack getCraftingItem() {
		
		return this.upgradeItem;
		
	}
 
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		
		return new TurtleChunkAwakerHosted(turtle, side);
		
	}
	  
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
				
		return WakingChunksData.chunkAwaker.getIcon(0, 2);
		
	}
 
	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		
		return null;
		
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		
		IPeripheral peripheral = turtle.getPeripheral(side);
		if(peripheral != null && peripheral instanceof TurtleChunkAwakerHosted) {
			((TurtleChunkAwakerHosted)peripheral).updateChunks();
		}
		
	}
 
}
