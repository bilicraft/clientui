package io.bilicraft.r6.clientui.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class BilicraftDebugHandler {
	public static BilicraftDebugHandler instance = new BilicraftDebugHandler();
	public static Logger logging = LogManager.getLogger("DEBUG");

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerLoginEvent(Event event) {

		if (event instanceof RenderGameOverlayEvent || event instanceof EntityViewRenderEvent
				|| event instanceof RenderWorldEvent || event instanceof RenderPlayerEvent
				|| event instanceof RenderLivingEvent || event instanceof DrawBlockHighlightEvent
				|| event instanceof RenderHandEvent || event instanceof RenderWorldLastEvent
				|| event instanceof LivingUpdateEvent || event instanceof PlaySoundEvent17
				|| event instanceof PlaySoundSourceEvent || event instanceof BiomeEvent
				|| event instanceof GuiScreenEvent || event instanceof FOVUpdateEvent
				|| event instanceof EntityConstructing || event instanceof PlaySoundAtEntityEvent
				|| event instanceof ChunkEvent.Load) {
			return;
		}
		if(event instanceof EntityEvent.EnteringChunk){return;}
		logging.info("Event:" + event.getClass().getTypeName() + "|Phase" + event.getPhase().name() + "|Result"
				+ event.getResult().name());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onButtonPress(ActionPerformedEvent event)
	{
		logging.info("Event:ActionPerformedEvent"+"|button:"+event.button.displayString+"|buttontype:"+event.button.getClass().getName()+"|"+event.gui );
		
	}
	
}
