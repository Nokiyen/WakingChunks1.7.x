package noki.wakingchunks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import noki.wakingchunks.cc.CCManager;
import noki.wakingchunks.chunkawaker.ChunkAwakerBlock;
import noki.wakingchunks.chunkawaker.ChunkAwakerItemBlock;
import noki.wakingchunks.chunkawaker.ChunkAwakerTile;


/**********
 * @class WakingChunksData
 *
 * @description
 * @description_en
 */
public class WakingChunksData {

	//******************************//
	// define member variables.
	//******************************//	
	//	creative tab.
	public static CreativeTabs tab;
	
	//	block.
	public static String chunkAwakerName = "chunk_awaker";
	public static Block chunkAwaker;
	
	//	cc.
	public static boolean ccLoaded = false;
	public static int chunkAwakerTurtlePID;
	public static Block cc_turtle;
	public static Block cc_turtleExpanded;
	public static Block cc_turtleAdvanced;

	//	debug.
	public static boolean debug = true;

	
	//******************************//
	// define member methods.
	//******************************//
	public static void initPre(FMLPreInitializationEvent event) {
		
		ccLoaded = Loader.isModLoaded("ComputerCraft");
		
		Property prop;
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		prop = cfg.get("upgrade", "chunkAwakerTurtlePID", 1109);
		chunkAwakerTurtlePID = prop.getInt();
		
		cfg.save();
		
		tab = new TabWakingChunks();
		
		chunkAwaker = new ChunkAwakerBlock(chunkAwakerName);
		GameRegistry.registerBlock(chunkAwaker, ChunkAwakerItemBlock.class, chunkAwakerName);
		GameRegistry.registerTileEntity(ChunkAwakerTile.class, "ChunkAwakerTile");
		
	}

	public static void init() {
				
		GameRegistry.addRecipe(new ItemStack(chunkAwaker, 1, 0),
				"xyx", "y y", "xyx", 'x', Blocks.glass, 'y', Items.iron_ingot);
		GameRegistry.addRecipe(new ItemStack(chunkAwaker, 1, 1),
				" y ", "yzy", " y ", 'y', Items.gold_ingot, 'z', new ItemStack(chunkAwaker, 1, 0));
		GameRegistry.addRecipe(new ItemStack(chunkAwaker, 1, 2),
				" y ", "yzy", " y ", 'y', Items.diamond, 'z', new ItemStack(chunkAwaker, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(chunkAwaker, 1, 1), new ItemStack(chunkAwaker, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(chunkAwaker, 1, 0), new ItemStack(chunkAwaker, 1, 1));		
		
		if(ccLoaded) {
			CCManager.register();
		}

	}
	
	public static class TabWakingChunks extends CreativeTabs {
		
		//******************************//
		// define member variables.
		//******************************//
		public static String label = "WakingChunks";

		
		//******************************//
		// define member methods.
		//******************************//
		public TabWakingChunks() {
			
			super(label);
			
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			
			return Item.getItemFromBlock(chunkAwaker);

		}
		
	}

}
