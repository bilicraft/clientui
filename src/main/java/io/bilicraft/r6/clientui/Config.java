package io.bilicraft.r6.clientui;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static void LoadConfig(FMLPreInitializationEvent event){
	Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
    try {
        cfg.load();
        BilicraftUI.Debug = cfg.getBoolean("Debug", "boolean", false, "Debug Mode ON/OFF");
        //名称，类型，默认值，描述
        //同样的方法有getBoolean，getString,getInt
     } catch (Exception var10) {
        FMLLog.warning( "123", new Object[0]);
     } finally {
        cfg.save();
     }
	}
}
