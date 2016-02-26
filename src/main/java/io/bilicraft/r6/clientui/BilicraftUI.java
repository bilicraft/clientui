package io.bilicraft.r6.clientui;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "bilicraftui",name="Bilicraft UI",version = "alpha")
public class BilicraftUI {
	public static String email;
	public static String password;
	@Mod.Instance
	public static BilicraftUI instance;
	private static final boolean Debug = true;
	@EventHandler
	public void onModPreInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BilicraftMenuHandler());
		MinecraftForge.EVENT_BUS.register(new BilicraftPlayerHandler());
		if (isDebug()) {
			MinecraftForge.EVENT_BUS.register(new BilicraftDebugHandler());
		}
		
	}
	public static boolean isDebug()
	{
		return Debug;
	}
	public static void sendCommandExecute(String command)
	{
		Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
	}
}

