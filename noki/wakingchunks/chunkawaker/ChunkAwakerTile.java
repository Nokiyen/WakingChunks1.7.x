package noki.wakingchunks.chunkawaker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noki.wakingchunks.ChunkManager;


/**********
 * @class ChunkAwakerTile
 *
 * @description
 * @description_en
 */
public class ChunkAwakerTile extends TileEntity {
	
	//******************************//
	// define member variables.
	//******************************//
	private String label = "";

	
	//******************************//
	// define member methods.
	//******************************//	
	public void readFromNBT(NBTTagCompound nbt) {
		
		super.readFromNBT(nbt);
		this.label = nbt.getString("label");
		
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		
		super.writeToNBT(nbt);
		nbt.setString("label", this.label);
		
	}
	
	public String getLabel(World world) {
		
		if(this.label == "" || ChunkManager.existLabel(world, this.label) == false) {
			String newLabel = ChunkManager.requestLabel(world);
			if(newLabel == null) {
				return null;
			}
			this.label = newLabel;
		}
		return this.label;
		
	}

}
