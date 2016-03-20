package io.bilicraft.r6.clientui.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.bilicraft.r6.clientui.BilicraftConfig;
import net.minecraft.client.Minecraft;

public class BilicraftClientProxy extends BilicraftCommonProxy {
    public static Logger logging = LogManager.getLogger("ClientProxy");

    @Override
    public void oninit(FMLInitializationEvent event) {
	super.oninit(event);

    }

    @Override
    public void onPreinit(FMLPreInitializationEvent event) {
	super.onPreinit(event);

	// config
	BilicraftConfig.loadConfig(event);

    }

    @Override
    public void onPostinit(FMLPostInitializationEvent event) {
	super.onPostinit(event);

	Minecraft mc = Minecraft.getMinecraft();
	logging.info("BidiFlag:"+mc.fontRenderer.getBidiFlag()+",UnicodeFlag:"+mc.fontRenderer.getUnicodeFlag());
	if (mc.fontRenderer.getBidiFlag()) {
	    mc.fontRenderer.setBidiFlag(false);
	}
	if (mc.fontRenderer.getUnicodeFlag()) {
	    mc.fontRenderer.setUnicodeFlag(false);
	}
	logging.info("ChangeFont !!!");
    }

}
