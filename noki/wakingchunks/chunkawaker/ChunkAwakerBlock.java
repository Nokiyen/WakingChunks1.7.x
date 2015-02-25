package noki.wakingchunks.chunkawaker;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noki.wakingchunks.ChunkManager;
import noki.wakingchunks.ModInfo;
import noki.wakingchunks.WakingChunksData;
import noki.wakingchunks.cc.PeripheralChunkAwaker;
import noki.wakingchunks.event.EventWorldRender;
import noki.wakingchunks.packet.PacketBreakMessage;
import noki.wakingchunks.packet.PacketHandler;
import noki.wakingchunks.packet.PacketRequestMessage;


/**********
 * @class ChunkAwakerBlock
 *
 * @description
 * @description_en
 */
public class ChunkAwakerBlock extends BlockContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private IIcon[] icons;


	//******************************//
	// define member methods.
	//******************************//
	public ChunkAwakerBlock(String unlocalizedName) {
		
		super(Material.glass);
		this.setBlockName(unlocalizedName);
		this.setHardness(0.3F);
		this.setStepSound(soundTypeGlass);
//		this.setCreativeTab(WakingChunksData.tab);
		this.setCreativeTab(WakingChunksData.tab);
		
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		
		if(WakingChunksData.ccLoaded) {
			return new PeripheralChunkAwaker(world);
		}
		return new ChunkAwakerTile();
		
	}
	
	@Override
	public int damageDropped(int metadata) {
		
		return MathHelper.clamp_int(metadata, 0, 5);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	//about List.
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		
		for(int i=0; i<=2; i++) {
			list.add(new ItemStack(item, 1, i));
		}
		
	}
	
	@Override
	public boolean isOpaqueCube() {
		
		return false;
		
	}
	
	@Override
	public void onBlockAdded(World world, int posX, int posY, int posZ) {
		
		if(world.isRemote) {
			return;
		}
		
		int metadata = MathHelper.clamp_int(world.getBlockMetadata(posX, posY, posZ), 0, 5);
		if(metadata > 2) {
			return;
		}
		
		String label = ((ChunkAwakerTile)world.getTileEntity(posX, posY, posZ)).getLabel(world);
		if(label == null) {
			return;
		}
		
		boolean flag = ChunkManager.loadChunks(world, posX, posY, posZ, metadata, label);
		if(flag == false) {
			world.setBlockMetadataWithNotify(posX, posY, posZ, metadata+3, 3);
		}
		
	}
	
	@Override
	public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int metadata,
			float f1, float f2, float f3) {

		//	turn on or off for showing chunks.
		if(player.isSneaking()) {
			if(world.isRemote) {
				if(EventWorldRender.loadedChunks != null) {
					EventWorldRender.loadedChunks = null;
				}
				else {
					PacketHandler.instance.sendToServer(new PacketRequestMessage(
							player.worldObj.provider.dimensionId,
							player.chunkCoordX, player.chunkCoordZ,
							Minecraft.getMinecraft().gameSettings.renderDistanceChunks));
				}
			}
			return true;
		}
		//	turn on or off for loading chunks.
		else {
			if(world.isRemote) {
				return true;
			}
			
			String label = ((ChunkAwakerTile)world.getTileEntity(posX, posY, posZ)).getLabel(world);
			if(label == null) {
				return false;
			}
			metadata = MathHelper.clamp_int(world.getBlockMetadata(posX, posY, posZ), 0, 5);
			
			if(metadata < 3) {
				ChunkManager.unloadChunks(world, label);
				world.setBlockMetadataWithNotify(posX, posY, posZ, metadata+3, 3);
			}
			else {
				boolean flag =  ChunkManager.loadChunks(world, posX, posY, posZ, metadata-3, label);
				if(flag == true) {
					world.setBlockMetadataWithNotify(posX, posY, posZ, metadata-3, 3);
				}
				else {
					world.setBlockMetadataWithNotify(posX, posY, posZ, metadata, 3);
				}
			}
			return true;			
		}
		
	}
	
	@Override
	public void breakBlock(World world, int posX, int posY, int posZ, Block block, int metadata) {

		if(world.isRemote) {
			//	seems to be uncalled on client...
			ChunkManager.log("break block.");
			EventWorldRender.loadedChunks = null;
		}
		else {
			String label = ((ChunkAwakerTile)world.getTileEntity(posX, posY, posZ)).getLabel(world);
			if(label != null) {
				ChunkManager.releaseTicket(world, label);
			}
			PacketHandler.instance.sendToAll(new PacketBreakMessage());
		}
		world.removeTileEntity(posX, posY, posZ);
		
	}
		
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		
		metadata = MathHelper.clamp_int(metadata, 0, 5);
		return this.icons[metadata];
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		
		this.icons = new IIcon[6];
		for(int i=0; i<=5; i++) {
			this.icons[i] = icon.registerIcon(ModInfo.ID.toLowerCase()+":ChunkAwaker_"+i);
		}
		
	}

}
