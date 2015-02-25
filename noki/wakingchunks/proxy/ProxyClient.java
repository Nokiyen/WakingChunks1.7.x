package noki.wakingchunks.proxy;

import net.minecraftforge.common.MinecraftForge;
import noki.wakingchunks.event.EventWorldRender;
//import net.minecraftforge.common.MinecraftForge;
//import noki.wakingchunks.event.EventWorldRender;
import noki.wakingchunks.proxy.ProxyCommon;


/**********
 * @class ProxyClient
 *
 * @description クライアント用proxyクラス。
 * @description_en Proxy class for Client.
 */
public class ProxyClient extends ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void register() {
		
		MinecraftForge.EVENT_BUS.register(new EventWorldRender());
//		MinecraftForge.EVENT_BUS.register(new EventWorldRender());
		
	}
	
}
