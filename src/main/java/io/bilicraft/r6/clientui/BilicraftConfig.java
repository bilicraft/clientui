package io.bilicraft.r6.clientui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class BilicraftConfig {
    public final static Logger logging = LogManager.getLogger("BilicraftConfig");
    private static boolean Debug = false;
    public static String password ="";
    public static String ServerIp = "";
    private static Configuration configuration = null;

    public static void loadConfig(FMLPreInitializationEvent event) {
	configuration = new Configuration(event.getSuggestedConfigurationFile());
	try {
	    //名称 分类 默认值 描述
	    Debug = configuration.getBoolean("Debug", "boolean", false, "Debug Mode ON/OFF");
	    
	    ServerIp = configuration.getString("Default Server IP", "String value", "mc.bilicraft.com", "Set Enter the Bilicraft Server Default IP");
	} catch(Exception ex)
	{
	    logging.warn("Load Configuration Error");
	} finally {
	    configuration.save();
	}
    }
    public static void setString(String key,String category,String defaultValue,String s)
    {
	configuration.getString(key, category, defaultValue,s);
	configuration.save();
    }
    public static String getServerIp()
    {
	return ServerIp;
    }
    public static boolean isDebug() {
	return Debug;
    }
    public static void setDebug()
    {
	Debug = !Debug;
    }
}
