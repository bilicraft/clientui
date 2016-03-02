package io.bilicraft.r6.clientui.render;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.BilicraftConfig;
import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class BilicraftMainMenu extends GuiScreen implements GuiYesNoCallback {
    public static final String field_96138_a = "请点" + EnumChatFormatting.UNDERLINE + "这里"
            + EnumChatFormatting.RESET + "获取更多信息";
    private static final Logger logger = LogManager.getLogger();
    /**
     * 主界面随机生成器
     */
    private static final Random rand = new Random();
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
            "textures/gui/title/minecraft.png");
    /**
     * 主界面背景图片数组
     */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{
            new ResourceLocation("textures/gui/title/background/panorama_0.png"),
            new ResourceLocation("textures/gui/title/background/panorama_1.png"),
            new ResourceLocation("textures/gui/title/background/panorama_2.png"),
            new ResourceLocation("textures/gui/title/background/panorama_3.png"),
            new ResourceLocation("textures/gui/title/background/panorama_4.png"),
            new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private final Object field_104025_t = new Object();
    private BilicraftPlayerRender bilicraftPlayerRender;
    /**
     * 界面刷新计数器
     */
    private float updateCounter;
    /**
     * 就,显示在Minecraft图标旁边的那一行字
     */
    private String splashText;
	private GuiButtonExt bilicraftbutton;
    private GuiButton buttonResetDemo;
    /**
     * 主界面背景旋转计时,按每刻(tick)增加
     */
    private int panoramaTimer;
    /**
     * Texture allocated for the current viewport of the main menu's panorama
     * background.
     */
    private DynamicTexture viewportTexture;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiTextField email;
    //private GuiTextField password;
    private BilicraftPasswordTextField password;
    private boolean error = false;
    private int tempTimer = -1;

    public BilicraftMainMenu() {
        logger.info("init BilicraftMainMenu");
        this.field_146972_A = field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;

        try {
            ArrayList<String> splashTexts = new ArrayList<>();
            bufferedreader = new BufferedReader(new InputStreamReader(
                    Minecraft.getMinecraft().getResourceManager().getResource(BilicraftMainMenu.splashTexts).getInputStream(),
                    Charsets.UTF_8));

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                
                if (!s.isEmpty()) {
                    splashTexts.add(s);
                }
            }
            if (!splashTexts.isEmpty()) {
				do {
					this.splashText = (String) splashTexts.get(rand.nextInt(splashTexts.size()));
				} while (this.splashText.hashCode() == 125780783);
			}
        } catch (IOException ioexception1) {

        } finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                } catch (IOException ioexception) {

                }
            }
        }

        this.updateCounter = rand.nextFloat();
        this.field_92025_p = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
            this.field_92025_p = I18n.format("title.oldgl1");
            this.field_146972_A = I18n.format("title.oldgl2");
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        this.email.updateCursorCounter();
        this.password.updateCursorCounter();
        if (email.getText().equals("E-Mail") && email.isFocused()) {
            email.setText("");
        } else if (email.getText().length() == 0 && !email.isFocused()) {
            email.setText("E-Mail");
        }
        if (password.getText().equals("Password") && password.isFocused()) {
            password.setText("");
        } else if (password.getText().length() == 0 && !password.isFocused()) {
            password.setText("Password");
        }
        BilicraftUI.email = this.email.getText();
        //BilicraftUI.password = this.password.getText();
        BilicraftConfig.password = this.password.getPassword();
        ++this.panoramaTimer;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in
     * single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        this.password.textboxKeyTyped(p_73869_1_, p_73869_2_);
        this.email.textboxKeyTyped(p_73869_1_, p_73869_2_);
        if (p_73869_2_ == 15) {
            this.password.setFocused(!this.password.isFocused());
        }
		if(p_73869_1_ ==13 &&p_73869_2_ ==28) {
			actionPerformed(bilicraftbutton);
		}
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings({"unchecked"})
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        } else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        } else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 18) {
            this.splashText = "Happy birthday, Bilicraft!";
        } else if (rand.nextInt(100) == 42) {
            this.splashText = "The Answer to the Ultimate Question of Life, the Universe, and Everything";
        }

        boolean flag = true;
        int i = this.height / 4 + 48;

        if (this.mc.isDemo()) {
            this.addDemoButtons(i, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(i, 24);

        }
        addTextField(i);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, i + 72 + 12, 98, 20,
                I18n.format("menu.options")));
        this.buttonList.add(
                new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, i + 72 + 12));
        this.buttonList.add(new BilicraftCustomButton(23, this.width / 2 + 104, i + 72 + 12, 20, 20,
                "bcgui:buttons/btn-bc-normal.png", "bcgui:buttons/btn-bc-hover.png", "皮肤显示开关"));
        Object object = this.field_104025_t;

        bilicraftPlayerRender = new BilicraftPlayerRender();

        synchronized (this.field_104025_t) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
            int j = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - j) / 2;
            this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + j;
            this.field_92019_w = this.field_92021_u + 24;
        }
        BilicraftUI.username = this.mc.getSession().getUsername();
    }

    private void addTextField(int i) {
        Keyboard.enableRepeatEvents(true);
        email = new GuiTextField(this.fontRendererObj, this.width / 2 - 98, i, 95, 15);
        email.setMaxStringLength(128);
        email.setText("E-Mail");
        password = new BilicraftPasswordTextField(this.fontRendererObj, this.width / 2 + 4, i, 95, 15, "Password");
        //password.setText("Password");

    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who
     * have bought the game.
     */
    @SuppressWarnings("unchecked")
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        /**
         * this.buttonList.add( new GuiButton(1, this.width / 2 - 100,
         * p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
         **/

        GuiButtonExt singleplayer = new GuiButtonExt(1, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2,
                I18n.format("menu.singleplayer"));
        bilicraftbutton= new GuiButtonExt(22, this.width / 2 - 100, p_73969_1_ + p_73969_2_,
                "Enter the Bilicraft");

        /**
         * GuiButton realmsButton = new GuiButton(14, this.width / 2 - 100,
         * p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online", new
         * Object[0]));
         **/
        GuiButton fmlModButton = new GuiButton(6, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, "Mods");
        // fmlModButton.xPosition = this.width / 2 + 2;

        singleplayer.width = 98;
        // bilicraftbutton.width = 98;
        // realmsButton.width = 98;
        fmlModButton.width = 98;
        this.buttonList.add(singleplayer);
        if (BilicraftConfig.isDebug()) {
            GuiButtonExt multibutton = new GuiButtonExt(2, this.width / 2 + 102, p_73969_1_ + p_73969_2_,
                    I18n.format("menu.multiplayer"));
            this.buttonList.add(multibutton);
            multibutton.width = 60;
        }

        this.buttonList.add(bilicraftbutton);

        // this.buttonList.add(realmsButton);
        this.buttonList.add(fmlModButton);
    }

    /**
     * Adds Demo buttons on Main Menu for players who are playing Demo.
     */
    @SuppressWarnings("unchecked")
    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList
                .add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo")));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_,
                I18n.format("menu.resetdemo")));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_) {
        if (p_146284_1_.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (p_146284_1_.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (p_146284_1_.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (p_146284_1_.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (p_146284_1_.id == 22) {
            BilicraftUI.email = this.email.getText();
            BilicraftConfig.password = this.password.getPassword();
            if (BilicraftConfig.password.length() < 8 || BilicraftConfig.password.equals("Password")) {
                error = true;
                this.password.setFocused(true);
                // System.out.println(BilicraftUI.password.length());
                return;
            } else {
                error = false;
            }
            if (BilicraftConfig.isDebug()) {
                System.out.println("Email|Pre:" + email.getText() + ",Password|Pre:" + password.getPassword());
            }

            FMLClientHandler.instance().setupServerList();
            FMLClientHandler.instance().connectToServer(this, new ServerData("", "mc.bilicraft.io"));
        }

        if (p_146284_1_.id == 23) {
            // TODO 完成功能
            BilicraftUI.setPlayerModelState();

        }

        if (p_146284_1_.id == 14) {
            this.func_140005_i();
        }

        if (p_146284_1_.id == 4) {
            this.mc.shutdown();
        }

        if (p_146284_1_.id == 6) {
            this.mc.displayGuiScreen(new GuiModList(this));
        }

        if (p_146284_1_.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (p_146284_1_.id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }
    }

    private void func_140005_i() {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
        if (p_73878_1_ && p_73878_2_ == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (p_73878_2_ == 13) {
            if (p_73878_1_) {
                try {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI(this.field_104024_v));
                } catch (Throwable throwable) {
                    logger.error("Couldn\'t open link", throwable);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        byte b0 = 8;

        for (int k = 0; k < b0 * b0; ++k) {
            GL11.glPushMatrix();
            float f1 = ((float) (k % b0) / (float) b0 - 0.5F) / 64.0F;
            float f2 = ((float) (k / b0) / (float) b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GL11.glTranslatef(f1, f2, f3);
            GL11.glRotatef(MathHelper.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F,
                    0.0F, 0.0F);
            GL11.glRotatef(-((float) this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GL11.glPushMatrix();

                if (l == 1) {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double) (0.0F + f4), (double) (0.0F + f4));
                tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, (double) (1.0F - f4), (double) (0.0F + f4));
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, (double) (1.0F - f4), (double) (1.0F - f4));
                tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double) (0.0F + f4), (double) (1.0F - f4));
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        byte b0 = 3;

        for (int i = 0; i < b0; ++i) {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float) (i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (float) (i - b0 / 2) / 256.0F;
            tessellator.addVertexWithUV((double) j, (double) k, (double) this.zLevel, (double) (0.0F + f1), 1.0D);
            tessellator.addVertexWithUV((double) j, 0.0D, (double) this.zLevel, (double) (1.0F + f1), 1.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel, (double) (1.0F + f1), 0.0D);
            tessellator.addVertexWithUV(0.0D, (double) k, (double) this.zLevel, (double) (0.0F + f1), 0.0D);
        }

        tessellator.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GL11.glViewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float f2 = (float) this.height * f1 / 256.0F;
        float f3 = (float) this.width * f1 / 256.0F;
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        tessellator.addVertexWithUV(0.0D, (double) l, (double) this.zLevel, (double) (0.5F - f2), (double) (0.5F + f3));
        tessellator.addVertexWithUV((double) k, (double) l, (double) this.zLevel, (double) (0.5F - f2),
                (double) (0.5F - f3));
        tessellator.addVertexWithUV((double) k, 0.0D, (double) this.zLevel, (double) (0.5F + f2), (double) (0.5F - f3));
        tessellator.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel, (double) (0.5F + f2), (double) (0.5F + f3));
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.renderSkybox(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        Tessellator tessellator = Tessellator.instance;
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        byte b0 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if ((double) this.updateCounter < 1.0E-4D) {
            this.drawTexturedModalRect(k, b0, 0, 0, 99, 44);
            this.drawTexturedModalRect(k + 99, b0, 129, 0, 27, 44);
            this.drawTexturedModalRect(k + 99 + 26, b0, 126, 0, 3, 44);
            this.drawTexturedModalRect(k + 99 + 26 + 3, b0, 99, 0, 26, 44);
            this.drawTexturedModalRect(k + 155, b0, 0, 45, 155, 44);
        } else {
            this.drawTexturedModalRect(k, b0, 0, 0, 155, 44);
            this.drawTexturedModalRect(k + 155, b0, 0, 45, 155, 44);
        }

        tessellator.setColorOpaque_I(-1);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.8F - MathHelper.abs(
                MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f1 = f1 * 100.0F / (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GL11.glScalef(f1, f1, f1);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GL11.glPopMatrix();
        String s = "Minecraft 1.7.10";

        if (this.mc.isDemo()) {
            s += " Demo";
        }

        drawBilicraftInfo();
        if (BilicraftUI.isShowPlayer()) {
            renderPlayerModel();
        }

        List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings(true));
        for (int i = 0; i < brandings.size(); i++) {
            String brd = brandings.get(i);
            if (!Strings.isNullOrEmpty(brd)) {
                this.drawString(this.fontRendererObj, brd, 2,
                        this.height - (10 + i * (this.fontRendererObj.FONT_HEIGHT + 1)), 16777215);
            }
        }

        String s1 = "Copyright Mojang AB. Do not distribute!";
        // System.out.println("Pre1->2:"+tempTimer);

        this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,
                this.height - 10, -1);
        if (!error) {
            tempTimer = -1;
        }
        if (error) {
            String errorPassword = "§r§cPassword Length must >= 8";
            if (tempTimer == -1) {
                tempTimer = this.panoramaTimer;
            }
            // System.out.println(panoramaTimer - tempTimer);
            // 20tick为1s
            if (tempTimer != -1 && panoramaTimer - tempTimer >= 20 * 4) {
                // System.out.println("Pre2->1:" + tempTimer + "|" +
                // panoramaTimer);
                error = !error;
            } else {
                this.drawString(this.fontRendererObj, errorPassword, this.width / 2 - 48, this.height / 4 + 32, -1);
            }

        }

        if (this.field_92025_p != null && this.field_92025_p.length() > 0) {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1,
                    1428160512);
            this.drawString(this.fontRendererObj, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.field_146972_A, (this.width - this.field_92024_r) / 2,
                    ((GuiButton) this.buttonList.get(0)).yPosition - 12, -1);
        }
        this.password.drawTextBox();
        this.email.drawTextBox();
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    private void renderPlayerModel() {
        bilicraftPlayerRender.setSkin(this.mc.getSession().getUsername());
        if (BilicraftConfig.isDebug()) {
            System.out.println(this.mc.getSession().getUsername());
        }
        bilicraftPlayerRender.setWalk(0.3F);
        bilicraftPlayerRender.render(55, 110, 10F, -15F, 0.0F, 3.0F);
    }

    private void drawBilicraftInfo() {
        // TODO 待完成
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        Object object = this.field_104025_t;
        this.email.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);

        this.password.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);

        synchronized (this.field_104025_t) {
            if (this.field_92025_p.length() > 0 && p_73864_1_ >= this.field_92022_t && p_73864_1_ <= this.field_92020_v
                    && p_73864_2_ >= this.field_92021_u && p_73864_2_ <= this.field_92019_w) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                guiconfirmopenlink.func_146358_g();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }
}
