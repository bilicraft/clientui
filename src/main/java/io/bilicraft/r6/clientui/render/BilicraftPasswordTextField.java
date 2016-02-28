package io.bilicraft.r6.clientui.render;

import com.google.common.collect.Range;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class BilicraftPasswordTextField extends GuiTextField {
    /**
     * 忘了服务器密码允许最长多少了,先放个30在这里
     */
    private StringBuilder realPasswordSB = new StringBuilder(30);
    private String realPassword = "";
    private String defaultString;

    public BilicraftPasswordTextField(FontRenderer fontRenderer, int xPos, int yPos, int width, int height,
                                      String defaultString) {
        super(fontRenderer, xPos, yPos, width, height);
        this.defaultString = defaultString;
        setText(defaultString);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean textboxKeyTyped(char keyedChar, int p_146201_2_) {
        if (BilicraftUI.isDebug()) {
            System.out.println("textboxKeyTyped: " + (int) keyedChar + ", " + p_146201_2_);
        }
        boolean result = super.textboxKeyTyped(keyedChar, p_146201_2_);
        if (!isFocused()) {
            return result;
        }

        if (keyedChar >= 33 && keyedChar <= 126) {
            realPasswordSB.append(keyedChar);
        } else if (keyedChar == 8 || keyedChar == 127) { // backspace or del
            if (realPasswordSB.length() > 0)
                realPasswordSB.deleteCharAt(realPasswordSB.length() - 1);
        } else if (keyedChar == 13 && p_146201_2_ == 28) { // carriage return
            setFocused(false);
        }

        maskGuiText(realPasswordSB.length());
        realPassword = realPasswordSB.toString();
        return result;
    }

    private void maskGuiText(int textLength) {
        setText(new String(new char[textLength]).replace("\0", "*"));
    }

    public String getName() {
        return defaultString;
    }

    public String getPassword() {
        return realPassword == null ? "" : realPassword;
    }
}
