package io.bilicraft.r6.clientui.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class BilicraftCustomButton extends GuiButton {
    private ResourceLocation buttonTextures;
    private ResourceLocation buttonTextures_1;

    public BilicraftCustomButton(int id, int xPos, int yPos, int width, int height, String texturePath,
                                 String texturePath_change, String name) {
        super(id, xPos, yPos, width, height, name);
        this.buttonTextures = new ResourceLocation(texturePath);
        buttonTextures_1 = new ResourceLocation(texturePath_change);
    }

    public BilicraftCustomButton(int id, int xPos, int yPos, String texturePath, String texturePath_change,
                                 String name) {
        super(id, xPos, yPos, 40, 40, name);
        this.buttonTextures = new ResourceLocation(texturePath);
        buttonTextures_1 = new ResourceLocation(texturePath_change);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {

            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
                    && mouseY < this.yPosition + this.height;
            mc.getTextureManager().bindTexture(getTextures(flag));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            Gui.func_146110_a(this.xPosition, this.yPosition, 0, 0, this.width, this.height, this.width, this.height);

        }
    }

    protected ResourceLocation getTextures(boolean flag) {
        return flag ? buttonTextures_1 : buttonTextures;
    }

}
