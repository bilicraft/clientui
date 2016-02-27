package io.bilicraft.r6.clientui.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class BilicraftPasswordTextField extends GuiTextField {
    private String realPassword = "";
    private String defaultString;
    // private String lastInputString;
    // private int lastLength;

    public BilicraftPasswordTextField(FontRenderer fontRenderer, int xPos, int yPos, int width, int height,
                                      String defaultString) {
        super(fontRenderer, xPos, yPos, width, height);
        this.defaultString = defaultString;
        setText(defaultString);
    }

    /*
     * @Override public void updateCursorCounter() {
     *
     * super.updateCursorCounter(); // TODO 做些什么少年 我们需要在这里把text替换为“*” boolean
     * flag = getText().length() == 0 || getText().equals(defaultString) ||
     * !isFocused();
     *
     * if (!flag) { lastLength = getPassword().length(); int length =
     * getText().length(); if (BilicraftUI.isDebug()) {
     *
     * @SuppressWarnings("unused") String testString = getText(); } boolean
     * flag2 = getText().endsWith("*"); if (!flag2) {
     *
     * String temp = getText().substring(length - 1); lastInputString =
     * getPassword(); if(lastLength>length) { for(int i =
     * 0;i<=lastLength-length;i++) lastInputString =
     * lastInputString.substring(0, length -1); } setPassword(lastInputString +
     * temp); java.lang.StringBuilder tempSB = new java.lang.StringBuilder();
     *
     * for (int i = 0; i < length; i++) { tempSB.append("*"); }
     * setText(tempSB.toString()); if (BilicraftUI.isDebug()) {
     * System.out.println("BilicraftPasswordTextField|" + getName() +
     * "--> Password:" + getPassword() + ",Text:" + getText()); } }
     *
     * }
     *
     * };
     **/
    @Override
    @SideOnly(Side.CLIENT)
    public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
        boolean result = super.textboxKeyTyped(p_146201_1_, p_146201_2_);
        if (!isFocused()) {
            return result;
        }
        String text = getText() != null ? getText() : "";
        if (BilicraftUI.isDebug()) {
            System.out.println(text);
        }
        int textLength = text.length();

        String endString = textLength > 0 ? text.substring(textLength - 1) : "";
        replaceGuiText(text);
        if (BilicraftUI.isDebug()) {
            System.out.println(endString);
        }

        switch (p_146201_1_) {
            case 8:
                if (realPassword.length() > 0) {
                    realPassword = getPassword().substring(0, realPassword.length() - 1);
                }
                if (BilicraftUI.isDebug()) {
                    System.out.println(realPassword);
                }
                break;
            case 13:
                break;
            case 0:
                break;
            default:
                realPassword += endString;

                if (BilicraftUI.isDebug()) {
                    System.out.println(realPassword);
                }
                break;
        }
        if (BilicraftUI.isDebug()) {
            System.out.println((int) p_146201_1_);
            System.out.println(p_146201_2_);
        }
        return result;
    }

    private void replaceGuiText(String text) {
        char[] temp = text.toCharArray();
        for (int j = 0; j < temp.length; j++) {
            temp[j] = '*';
            if (BilicraftUI.isDebug()) {
                System.out.println(temp[j]);
            }

        }
        if (BilicraftUI.isDebug()) {
            System.out.println(String.valueOf(temp));
        }
        setText(String.valueOf(temp));
    }

    public String getName() {
        return defaultString;
    }

    public String getPassword() {
        return realPassword == null ? "" : realPassword;
    }
}
