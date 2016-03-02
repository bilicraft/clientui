package io.bilicraft.r6.clientui.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class BilicraftPasswordTextField extends GuiTextField {
    private static final int MAX_LENGTH = 30;
    private StringBuilder realPasswordSB;
    private String defaultString;

    public BilicraftPasswordTextField(FontRenderer fontRenderer, int xPos, int yPos, int width, int height,
                                      String defaultString) {
        super(fontRenderer, xPos, yPos, width, height);
        this.defaultString = defaultString;
        setText(defaultString);
        realPasswordSB = new StringBuilder(MAX_LENGTH);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean textboxKeyTyped(char keyedChar, int p_146201_2_) {
        if (BilicraftUI.isDebug()) {
            System.out.println("textboxKeyTyped: " + (int) keyedChar + ", " + p_146201_2_);
        }
        boolean result = super.textboxKeyTyped(keyedChar, p_146201_2_);
        if (!isFocused() || realPasswordSB.length() == MAX_LENGTH) {
            maskGuiText(realPasswordSB.length());
            return result;
        }

        if (keyedChar >= 33 && keyedChar <= 126) { // valid range for password chars
            realPasswordSB.append(keyedChar);
        } else if (keyedChar == 8 || keyedChar == 127) { // backspace or del
            if (realPasswordSB.length() > 0)
                realPasswordSB.deleteCharAt(realPasswordSB.length() - 1);
        } else if (keyedChar == 13 && p_146201_2_ == 28) { // carriage return
            setFocused(false);
        }

        maskGuiText(realPasswordSB.length());
        return result;
    }

    private void maskGuiText(int textLength) {
        setText(new String(new char[textLength]).replace("\0", "*"));
    }

    public String getName() {
        return defaultString;
    }

    public String getPassword() {
        return realPasswordSB.toString();
    }
}
