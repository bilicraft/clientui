package io.bilicraft.r6.clientui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class BilicraftConfig {
    public final static Logger logging = LogManager.getLogger("BilicraftConfig");
    private static boolean Debug = false;
    public static String password ="";



    public static void loadConfig(FMLPreInitializationEvent event) {
	Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
	try {
	    //名称 分类 默认值 描述
	    Debug = configuration.getBoolean("Debug", "boolean", false, "Debug Mode ON/OFF");
	    
	} catch(Exception ex)
	{
	    logging.warn("Load Configuration Error");
	} finally {
	    configuration.save();
	}
    }
    public static boolean isDebug() {
	return Debug;
    }
    public static void setDebug()
    {
	Debug = !Debug;
    }
}
