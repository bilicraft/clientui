package io.bilicraft.r6.clientui.handler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import io.bilicraft.r6.clientui.BilicraftConfig;
import io.bilicraft.r6.clientui.BilicraftUI;
import io.bilicraft.r6.clientui.Info;
import io.bilicraft.r6.clientui.render.BilicraftInGameMenu;
import io.bilicraft.r6.clientui.render.BilicraftMainMenu;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.GuiOpenEvent;

public class BilicraftMenuHandler {
    public static BilicraftMenuHandler instance = new BilicraftMenuHandler();
    public static Logger logging = LogManager.getLogger("MenuHandler");

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
