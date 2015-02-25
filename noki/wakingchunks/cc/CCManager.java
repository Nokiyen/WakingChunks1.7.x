package noki.wakingchunks.cc;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;


public class CCManager {
	
	public static int cameraTurtlePID;
	public static Block cc_turtle;
	public static Block cc_turtleExpanded;
	public static Block cc_turtleAdvanced;
	
	public static void setCCConfig(FMLPreInitializationEvent event) {
		
		Property prop;
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		prop = cfg.get("upgrade", "cameraTurtlePID", 1110);
		cameraTurtlePID = prop.getInt();
		
		cfg.save();
		
	}
	
	
	public static void register() {
		
		cc_turtle = GameRegistry.findBlock("ComputerCraft", "CC-Turtle");
		cc_turtleExpanded = GameRegistry.findBlock("ComputerCraft", "CC-TurtleExpanded");
		cc_turtleAdvanced = GameRegistry.findBlock("ComputerCraft", "CC-TurtleAdvanced");
		
		GameRegistry.registerTileEntity(PeripheralChunkAwaker.class, "ChunkAwakerTileCC");					
		ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
		ComputerCraftAPI.registerTurtleUpgrade(new TurtleChunkAwaker());
		
	}

}
