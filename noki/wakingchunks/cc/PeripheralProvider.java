package noki.wakingchunks.cc;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import noki.wakingchunks.chunkawaker.ChunkAwakerBlock;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;


/**********
 * @class PeripheralProvider
 * 
 * @description
 * @description_en
 */
public class PeripheralProvider implements IPeripheralProvider {

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		
		Block block = world.getBlock(x, y, z);
		if(block instanceof ChunkAwakerBlock) {
			return (IPeripheral)world.getTileEntity(x, y, z);
		}		
		return null;
		
	}

}
