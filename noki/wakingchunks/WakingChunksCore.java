package noki.wakingchunks;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import noki.wakingchunks.WakingChunksData;
import noki.wakingchunks.event.EventJoinWorld;
import noki.wakingchunks.packet.PacketHandler;
import noki.wakingchunks.proxy.ProxyCommon;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;


/**********
 * @ModName WakingChunksCore
 * 
 * @description
 * 
 * @caution ここはコアファイルなので、原則、具体的な処理をしないよう気を付ける。
 */
@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCY)
public class WakingChunksCore {
	
	//******************************//
	// define member variables.
	//******************************//
	//	core.
	@Instance(value = "WakingChunks")
	public static WakingChunksCore instance;
	@Metadata
	public static ModMetadata metadata;
	public static VersionInfo versionInfo;
	@SidedProxy(clientSide = ModInfo.PROXY_LOCATION + "ProxyClient", serverSide = ModInfo.PROXY_LOCATION + "ProxyCommon")
	public static ProxyCommon proxy;

	
	//******************************//
	// define member methods.
	//******************************//
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkManager());
		PacketHandler.registerPre();
		WakingChunksData.initPre(event);
		
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		proxy.register();	// currently no function.
		MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
		WakingChunksData.init();
				
	}
        
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		//	nothing to do.
		
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		
		versionInfo.notifyUpdate(Side.SERVER);
		
	}

}
