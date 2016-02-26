package io.bilicraft.r6.clientui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GuiHelper {
	
	
	public static BufferedImage loadImage(String image)
	{
		try
		{
			File f = new File(image).getAbsoluteFile();
			return ImageIO.read(f);
		}catch(IOException exception)
		{
			
		}
		return null;
		
	}
}
