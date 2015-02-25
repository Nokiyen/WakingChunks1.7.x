package noki.wakingchunks.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import noki.wakingchunks.ChunkManager;
import noki.wakingchunks.WakingChunksCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;


/**********
 * @class EventJoinWorld
 *
 * @description
 * @description_en
 */
public class EventJoinWorld {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent event) {
		
		if(event.world.isRemote) {
			if(event.entity instanceof EntityPlayer &&
				((EntityPlayer)event.entity).getDisplayName() == Minecraft.getMinecraft().thePlayer.getDisplayName()) {
				WakingChunksCore.versionInfo.notifyUpdate(Side.CLIENT);
				
				EventWorldRender.loadedChunks = null;
			}
		}
/*		else {
			if(event.entity instanceof EntityPlayer) {
				ChunkManager.checkIllegalChunks(event.world);
			}
		}*/
		
	}

}
