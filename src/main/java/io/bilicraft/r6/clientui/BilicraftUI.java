package io.bilicraft.r6.clientui;

import java.io.File;
import java.lang.reflect.Field;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.bilicraft.r6.clientui.handler.BilicraftDebugHandler;
import io.bilicraft.r6.clientui.handler.BilicraftMenuHandler;
import io.bilicraft.r6.clientui.handler.BilicraftPlayerHandler;
import io.bilicraft.r6.clientui.proxy.BilicraftCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = BilicraftUI.MODID, name = BilicraftUI.MODNAME, version = BilicraftUI.VERSION)
public class BilicraftUI {

    public static final String MODID = "bilicraftui";
    public static final String MODNAME = "Bilicraft UI";
    public static final String VERSION = "v1.0.3";
    @Mod.Instance
    public static BilicraftUI instance;
    public static File configFile;
    @SidedProxy(clientSide = "io.bilicraft.r6.clientui.proxy.BilicraftClientProxy", serverSide = "io.bilicraft.r6.clientui.proxy.BilicraftCommonProxy", modId = MODID)
    public static BilicraftCommonProxy proxy;
    public static boolean isDev = false;
    private static boolean showPlayerModel = false;

    public static boolean isShowPlayer() {
	return showPlayerModel;
    }

    public static void setPlayerModelState() {
	showPlayerModel = !isShowPlayer();
    }

    public static void sendCommandExecute(String command) {
	Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
    }

    public static String username;
    public static String email;

    @EventHandler
    public void onModPreInit(FMLPreInitializationEvent event) {

	proxy.onPreinit(event);
	System.out.println("Check Runtime Environment Start");
	try {
	    Minecraft.class.getDeclaredField("field_71449_j");
	} catch (NoSuchFieldException e) {
	    isDev = true;
	}
	System.out.println("Check Runtime Environment Result: " + (isDev ? "Dev" : "Game"));
	configFile = event.getModConfigurationDirectory();
	MinecraftForge.EVENT_BUS.register(new BilicraftMenuHandler());
	MinecraftForge.EVENT_BUS.register(BilicraftPlayerHandler.INSTANCE);

	if (BilicraftConfig.isDebug()) {
	    MinecraftForge.EVENT_BUS.register(new BilicraftDebugHandler());
	}
    }

    @EventHandler
    public void onModInit(FMLInitializationEvent event) {
	proxy.oninit(event);

    }

    public static boolean setUsername(String username) {
	System.out.println("try Get Minecraft Seesion Username start");
	Session session = Minecraft.getMinecraft().getSession();
	System.out.println("Username: " + session.getUsername());
	System.out.println("PlayerID: " + session.getPlayerID());
	System.out.println("Token: " + session.getToken());
	System.out.println("SessionID: " + session.getSessionID());

	String sessionFieldName = isDev ? "session" : "field_71449_j";

	try {
	    Field session_field = Minecraft.class.getDeclaredField(sessionFieldName);
	    session_field.setAccessible(true);
	    session_field.set(Minecraft.getMinecraft(),
		    new Session(username, username, session.getToken(), session.func_152428_f().name().toLowerCase()));
	    System.out.println("try Get Minecraft Seesion Username end");
	    Session session_r = Minecraft.getMinecraft().getSession();
	    System.out.println("Username: " + session_r.getUsername());
	    System.out.println("PlayerID: " + session_r.getPlayerID());
	    System.out.println("Token: " + session_r.getToken());
	    System.out.println("SessionID: " + session_r.getSessionID());
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    @EventHandler
    public void onModPostInit(FMLPostInitializationEvent event) {
	proxy.onPostinit(event);
    }
}
