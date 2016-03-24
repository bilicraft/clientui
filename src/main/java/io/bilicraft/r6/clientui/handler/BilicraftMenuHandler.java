package io.bilicraft.r6.clientui.handler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import io.bilicraft.r6.clientui.BilicraftConfig;
import io.bilicraft.r6.clientui.BilicraftUI;
import io.bilicraft.r6.clientui.Info;
import io.bilicraft.r6.clientui.render.BilicraftCustomButton;
import io.bilicraft.r6.clientui.render.BilicraftInGameMenu;
import io.bilicraft.r6.clientui.render.BilicraftMainMenu;
import io.bilicraft.r6.clientui.render.BilicraftUserNameOptionScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;

public class BilicraftMenuHandler {
    public static BilicraftMenuHandler instance = new BilicraftMenuHandler();
    public static Logger logging = LogManager.getLogger("MenuHandler");
    private static BilicraftCustomButton bilicraftMapButton;
    private static BilicraftCustomButton bilicraftUserNameButton;
    @SubscribeEvent
    public void onMenuOpen(GuiOpenEvent event) {
	if (event.gui instanceof GuiMainMenu) {
	    event.gui = new BilicraftMainMenu();
	    if (BilicraftConfig.isDebug()) {
		getBlockList();
		getItemList();
	    }
	}
	if (event.gui instanceof GuiIngameMenu) {
	    event.gui = new BilicraftInGameMenu();
	}
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
	if (event.gui instanceof GuiInventory) {
	    GuiInventory inventory = (GuiInventory) event.gui;
	    bilicraftMapButton = new BilicraftCustomButton(25,
		    inventory.width - (20 * (inventory.width / inventory.height)),
		    inventory.height - (20 * (inventory.width / inventory.height)), 20, 20,
		    "bcgui:buttons/btn-bc-normal.png", "bcgui:buttons/btn-bc-hover.png", "大地图开关");
	    if (Loader.isModLoaded("NotEnoughItems")) {
		logging.info("Found NEI Plugin,So {} Change!",bilicraftMapButton.displayString);
		bilicraftMapButton.xPosition = (80 * (inventory.width / inventory.height));
	    }
	    event.buttonList.add(bilicraftMapButton);
	}
	if(event.gui instanceof GuiOptions)
	{
	    GuiOptions options = (GuiOptions) event.gui;
	    bilicraftUserNameButton = new BilicraftCustomButton(25,
		    options.width - (20 * (options.width / options.height)),
		    options.height - (20 * (options.width / options.height)), 20, 20,
		    "bcgui:buttons/btn-bc-normal.png", "bcgui:buttons/btn-bc-hover.png", "用户名设置开关");
	    event.buttonList.add(bilicraftUserNameButton);
	}
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onGuiButtonAction(GuiScreenEvent.ActionPerformedEvent event) {
	if (event.gui instanceof GuiInventory) {
		if (event.button.equals(bilicraftMapButton)) {
		    try {
			@SuppressWarnings("rawtypes")
			Class oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
				new URI("http://mc.bilicraft.com:8123/"));
		    } catch (Throwable throwable) {
			logging.error("Couldn\'t open link", throwable);
		    }
		}
	    }
	if(event.gui instanceof GuiOptions)
	{
	    if(event.button.equals(bilicraftUserNameButton))
	    {
		GuiOptions options = (GuiOptions) event.gui;
		options.mc.displayGuiScreen(new BilicraftUserNameOptionScreen(options));
	    }
	}
	}
    
    // modid,item

    private void getBlockList() {
	Gson gson = new Gson();
	StringBuilder blockMessage = new StringBuilder();
	StringBuilder jsonMessage = new StringBuilder();
	for (int id = 0; id < 4096; id++) {
	    Block block = GameData.getBlockRegistry().getObjectById(id);
	    if (!(block instanceof BlockAir)) {
		String name = GameData.getBlockRegistry().getNameForObject(block);
		if (name != null) {
		    blockMessage.append(
			    "id:" + id + ",Name:" + name + ",Texture:" + getBlockTextures(block).toString() + "\n");
		    String[] infoS = name.split("\\:", 2);
		    String[] texture = getBlockTextures(block).toString().split(",");
		    Info info = new Info(id, infoS[1], infoS[0], "");
		    for (String s : texture) {
			info.addTexture(s);
		    }

		    jsonMessage.append(gson.toJson(info) + ",");

		}
	    }
	}
	try {
	    writeForGameData("block.txt", blockMessage);
	    writeForGameData("block_json.txt", jsonMessage);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void getItemList() {
	Gson gson = new Gson();
	StringBuilder itemMessage = new StringBuilder();
	StringBuilder jsonMessage = new StringBuilder();
	for (int id = 4096; id < 32000; id++) {
	    Item item = GameData.getItemRegistry().getObjectById(id - 4096);
	    String name = GameData.getItemRegistry().getNameForObject(item);
	    if (name != null) {
		itemMessage
			.append("id:" + id + ",Name:" + name + ",Texture:" + getItemTextures(item).toString() + "\n");
		String[] infoS = name.split("\\:", 2);
		String[] texture = getItemTextures(item).toString().split(",");
		Info info = new Info(id, infoS[1], infoS[0], "");
		for (String s : texture) {
		    info.addTexture(s);
		}
		jsonMessage.append(gson.toJson(info) + ",");
	    }

	}

	try {
	    writeForGameData("item.txt", itemMessage);
	    writeForGameData("item_json.txt", jsonMessage);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private StringBuilder getBlockTextures(Block block) {
	StringBuilder message = new StringBuilder("");
	Set<IIcon> iconSet = new HashSet<IIcon>();
	IIcon blockicon = null;
	for (int side = 0; side < 8; side++) {
	    for (int meta = 0; meta < 50; meta++) {

		try {
		    blockicon = block.getIcon(side, meta);
		} catch (ArrayIndexOutOfBoundsException exception) {
		} finally {
		    iconSet.add(blockicon);
		}

	    }
	}
	for (IIcon icon : iconSet) {
	    if (icon == null) {
		continue;
	    }
	    String iconName = icon.getIconName();
	    if (iconName.contains(":")) {
		iconName = iconName.split(":", 2)[1];
	    }
	    message.append(iconName + ",");
	}
	return message;
    }

    private StringBuilder getItemTextures(Item item) {
	StringBuilder message = new StringBuilder("");
	Set<IIcon> iconSet = new HashSet<IIcon>();
	IIcon itemicon = null;
	for (int meta = 0; meta < 50; meta++) {
	    try {
		itemicon = item.getIconFromDamage(meta);
	    } catch (ArrayIndexOutOfBoundsException exception) {
	    } finally {
		iconSet.add(itemicon);
	    }
	}
	for (IIcon icon : iconSet) {
	    if (icon == null) {
		continue;
	    }
	    String iconName = icon.getIconName();
	    if (iconName.contains(":")) {
		iconName = iconName.split(":", 2)[1];
	    }
	    message.append(iconName + ",");
	}
	return message;
    }

    private void writeForGameData(String filename, StringBuilder stringBuilder) throws IOException {
	File outFile = new File(BilicraftUI.configFile, filename);
	if (!outFile.exists()) {
	    outFile.createNewFile();
	}
	FileWriterWithEncoding fWithEncoding = new FileWriterWithEncoding(outFile, "UTF-8");
	fWithEncoding.write(stringBuilder.toString());
	fWithEncoding.close();

    }

}
