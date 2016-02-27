package io.bilicraft.r6.clientui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.render.BilicraftPlayerRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class GuiHelper {
	public static final ResourceLocation defaultSteveTexture = new ResourceLocation("textures/entity/steve.png");
	private static final String SKINADRESS = "http://bbs.bilicraft.io/s/data/skin/%s.png";

	public static BufferedImage loadImage(String image) {
		try {
			File f = new File(image).getAbsoluteFile();
			return ImageIO.read(f);
		} catch (IOException exception) {

		}
		return null;

	}

	/**
	 * 从BC皮肤站获取皮肤
	 * 
	 * @param resourceLocation
	 * @param name 
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocation, String name) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(resourceLocation);

		if (object == null) {
			object = new ThreadDownloadImageData((File) null,
					String.format(SKINADRESS, new Object[] { StringUtils.stripControlCodes(name) }),
					BilicraftPlayerRender.defaultSteveTexture, new ImageBufferDownload());
			texturemanager.loadTexture(resourceLocation, (ITextureObject) object);
		}

		return (ThreadDownloadImageData) object;
	}
}
