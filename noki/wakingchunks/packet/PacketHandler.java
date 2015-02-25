package noki.wakingchunks.packet;

import noki.wakingchunks.ModInfo;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	
	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.ID);
	
	public static void registerPre() {
		
		instance.registerMessage(PacketRequestMessageHandler.class, PacketRequestMessage.class, 1, Side.SERVER);
		instance.registerMessage(PacketResponseMessageHandler.class, PacketResponseMessage.class, 2, Side.CLIENT);
		instance.registerMessage(PacketBreakMessageHandler.class, PacketBreakMessage.class, 3, Side.CLIENT);
		
	}

}
