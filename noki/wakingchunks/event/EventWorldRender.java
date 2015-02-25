package noki.wakingchunks.event;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import noki.wakingchunks.packet.PacketRequestMessageHandler.ChunkData;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class EventWorldRender
 *
 * @description
 * @description_en
 */
public class EventWorldRender {
	
	//******************************//
	// define member variables.
	//******************************//
	public static ArrayList<ChunkData> loadedChunks = null;

	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event) {
		
		if(loadedChunks == null) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		EntityLivingBase entity = mc.thePlayer;
//		PacketHandler.instance.sendToServer(new PacketSampleMessage());
				
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glPushMatrix();
		GL11.glTranslated(
				-(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks),
				-(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks),
				-(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks));
		Tessellator t = Tessellator.instance;
		
		
		double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks);
		
		t.startDrawingQuads();
//		ChunkManager.log("Start loop / %s.", loadedChunks.size());
		for(int i=0; i<loadedChunks.size(); i++) {
//			ChunkManager.log("loop / %s.", i);
			ChunkData chunk = loadedChunks.get(i);
//			ChunkManager.log("set color.");
//			ChunkManager.log("awaker is %s.", String.valueOf(chunk.awaker));
//			ChunkManager.log("other is %s.", String.valueOf(chunk.other));
			if(chunk.awaker && chunk.other) {
				t.setColorRGBA(0, 255, 0, 120);
			}
			else if(chunk.awaker && !chunk.other) {
				t.setColorRGBA(0, 0, 255, 120);				
			}
			else {
				t.setColorRGBA(255, 0, 0, 120);
			}

//			ChunkManager.log("set quods.");
			double x = chunk.chunkX*16;
			double z = chunk.chunkZ*16;
			t.addVertex(x, y+20, z);
			t.addVertex(x+16, y+20, z);
			t.addVertex(x+16, y+20, z+16);
			t.addVertex(x, y+20, z+16);
		}
		t.draw();
		
		t.startDrawing(GL11.GL_LINES);
		GL11.glLineWidth(3);
		for(int i=0; i<loadedChunks.size(); i++) {
			ChunkData chunk = loadedChunks.get(i);
			if(chunk.awaker && chunk.other) {
				t.setColorRGBA(0, 255, 0, 40);
			}
			else if(chunk.awaker && !chunk.other) {
				t.setColorRGBA(0, 0, 255, 40);				
			}
			else {
				t.setColorRGBA(255, 0, 0, 40);
			}

			double x = chunk.chunkX*16;
			double z = chunk.chunkZ*16;
			t.addVertex(x, y+20, z);
			t.addVertex(x+16, y+20, z);
			t.addVertex(x+16, y+20, z);
			t.addVertex(x+16, y+20, z+16);
			t.addVertex(x+16, y+20, z+16);
			t.addVertex(x, y+20, z+16);
			t.addVertex(x, y+20, z+16);
			t.addVertex(x, y+20, z);
		}
		t.draw();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);	//	necessary to correctly render perspectives.
		t.startDrawing(GL11.GL_LINES);
		t.setColorRGBA(0, 255, 0, 80);
		GL11.glLineWidth(3);
		int range = mc.gameSettings.renderDistanceChunks;
		for(int i = entity.chunkCoordX-range; i <= entity.chunkCoordX+range; i++) {
			for(int j = entity.chunkCoordZ-range; j <= entity.chunkCoordZ+range; j++) {
//				ChunkManager.log("current chunk is %s/%s.", i, j);
				t.addVertex(i*16, 0, j*16);
				t.addVertex(i*16, y+30, j*16);
			}
		}
		t.draw();
		
		t.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		t.setColorRGBA(0, 0, 255, 40);
//		ChunkManager.log("player pos is %s/%s.", entity.chunkCoordX, entity.chunkCoordZ);
		int currentX = entity.chunkCoordX*16;
		int currentZ = entity.chunkCoordZ*16;
//		ChunkManager.log("currentPos is %s/%s.", currentX, currentZ);
			
		t.addVertex(currentX, 0, currentZ);
		t.addVertex(currentX, y, currentZ);
		t.addVertex(currentX+16, y, currentZ);
		t.addVertex(currentX+16, 0, currentZ);
		
		t.addVertex(currentX+16, 0, currentZ);
		t.addVertex(currentX+16, y, currentZ);
		t.addVertex(currentX+16, y, currentZ+16);
		t.addVertex(currentX+16, 0, currentZ+16);
		
		t.addVertex(currentX+16, 0, currentZ+16);
		t.addVertex(currentX+16, y, currentZ+16);
		t.addVertex(currentX, y, currentZ+16);
		t.addVertex(currentX, 0, currentZ+16);
		
		t.addVertex(currentX, 0, currentZ+16);
		t.addVertex(currentX, y, currentZ+16);
		t.addVertex(currentX, y, currentZ);
		t.addVertex(currentX, 0, currentZ);
		
		t.draw();
		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		
	}

}
