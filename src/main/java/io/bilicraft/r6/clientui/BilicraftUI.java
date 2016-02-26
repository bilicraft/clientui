package io.bilicraft.r6.clientui;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.bilicraft.r6.clientui.handler.BilicraftDebugHandler;
import io.bilicraft.r6.clientui.handler.BilicraftMenuHandler;
import io.bilicraft.r6.clientui.handler.BilicraftPlayerHandler;
import io.bilicraft.r6.clientui.proxy.BilicraftCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = BilicraftUI.MODID, name = BilicraftUI.MODNAME, version = BilicraftUI.VERSION)
public class BilicraftUI {

	public static final String MODID = "bilicraftui";
	public static final String MODNAME = "Bilicraft UI";
	public static final String VERSION = "v1.0.3";
	private static final boolean Debug = false;

	public static String email;
	public static String password;
	@Mod.Instance
	public static BilicraftUI instance;

	@SidedProxy(clientSide = "io.bilicraft.r6.clientui.proxy.BilicraftClientProxy", serverSide = "io.bilicraft.r6.clientui.proxy.BilicraftCommonProxy", modId = MODID)
	public static BilicraftCommonProxy proxy;
	private static boolean showPlayerModel = false;
	@EventHandler
	public void onModPreInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new BilicraftMenuHandler());
		MinecraftForge.EVENT_BUS.register(new BilicraftPlayerHandler());
		
		if (isDebug()) {
			MinecraftForge.EVENT_BUS.register(new BilicraftDebugHandler());
		}
	}
	@EventHandler
	public void onModInit(FMLInitializationEvent event)
	{
		proxy.oninit(event);
	}
	public static boolean isDebug() {
		return Debug;
	}
	public static boolean isShowPlayer()
	{
		return showPlayerModel;
	}
	public static void setPlayerModelState()
	{
		showPlayerModel = !isShowPlayer();
	}
	public static void sendCommandExecute(String command) {
		Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
	}
}
