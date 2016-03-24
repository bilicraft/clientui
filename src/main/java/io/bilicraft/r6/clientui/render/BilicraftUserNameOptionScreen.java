package io.bilicraft.r6.clientui.render;

import org.lwjgl.input.Keyboard;

import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class BilicraftUserNameOptionScreen extends GuiScreen {

    private final GuiScreen superScreen;
    private GuiTextField username_r;

    public BilicraftUserNameOptionScreen(GuiScreen superScreen) {
	this.superScreen = superScreen;
	System.out.println(this.getClass().getSimpleName() + " init!");
	
    }

    @Override
    public void updateScreen() {
	this.username_r.updateCursorCounter();

    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
	Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, I18n.format("gui.cancel", new Object[0])));
        this.username_r = new GuiTextField(fontRendererObj, this.width / 2 - 100, 106, 200, 20);
        username_r.setFocused(false);
        username_r.setMaxStringLength(24);
        username_r.setText("");
	((GuiButton) this.buttonList.get(0)).enabled = this.username_r.getText().length() > 2
		&& this.username_r.getText().length() < 20;
    }

    @Override
    public void onGuiClosed() {
	Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
	if (button.enabled) {
	    if (button.id == 0) {
		if (BilicraftUI.setUsername(username_r.getText())) {
		    superScreen.confirmClicked(true, 0);
		} else {
		    superScreen.confirmClicked(false, 0);
		}
		this.mc.displayGuiScreen(superScreen);
	    }
	    if (button.id == 1) {
		superScreen.confirmClicked(false, 0);
		this.mc.displayGuiScreen(superScreen);
	    }
	}
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	this.username_r.textboxKeyTyped(p_73869_1_, p_73869_2_);
	if (p_73869_2_ == 15) {
	    this.username_r.setFocused(this.username_r.isFocused());
	}
	if (p_73869_2_ == 28 || p_73869_2_ == 156) {
	    this.actionPerformed((GuiButton) this.buttonList.get(0));
	}
	super.keyTyped(p_73869_1_, p_73869_2_);
	((GuiButton) this.buttonList.get(0)).enabled = this.username_r.getText().length() > 2
		&& this.username_r.getText().length() < 20;
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
	super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	this.username_r.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
	this.drawDefaultBackground();
	this.drawCenteredString(this.fontRendererObj, I18n.format("更改用户名", new Object[0]), this.width / 2, 17,
		16777215);
	// TODO 添加文本信息 原本的用户名
	String usernameString = Minecraft.getMinecraft().getSession().getUsername();
	this.drawString(this.fontRendererObj, "UserName: "+usernameString, this.width / 2 - 100, 94, 10526880);
	this.username_r.drawTextBox();
	super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
