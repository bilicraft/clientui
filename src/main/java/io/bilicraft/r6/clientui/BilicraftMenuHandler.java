package io.bilicraft.r6.clientui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiOpenEvent;

public class BilicraftMenuHandler {
	public static BilicraftMenuHandler instance = new BilicraftMenuHandler();
	public static Logger logging = LogManager.getLogger("MenuHandler");
	@SubscribeEvent
	public void onMenuOpen(GuiOpenEvent event)
	{
		if(event.gui instanceof GuiMainMenu)
		{
			event.gui = new BilicraftMainMenu();
		}
		if(event.gui instanceof GuiConnecting)
		{
			logging.info(BilicraftUI.email+BilicraftUI.password);
		}
	}
}
