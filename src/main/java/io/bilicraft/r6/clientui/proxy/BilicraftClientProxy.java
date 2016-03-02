package io.bilicraft.r6.clientui.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.bilicraft.r6.clientui.BilicraftConfig;

public class BilicraftClientProxy extends BilicraftCommonProxy {

    @Override
    public void oninit(FMLInitializationEvent event) {
        super.oninit(event);
        
    }

    @Override
    public void onPreinit(FMLPreInitializationEvent event) {
        super.onPreinit(event);
        BilicraftConfig.loadConfig(event);
        
    }
}
