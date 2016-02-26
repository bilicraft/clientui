package io.bilicraft.r6.clientui.render;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

/**
 * 
 * @author szszss
 *
 */
@SideOnly(Side.CLIENT)
public class BilicraftPlayerRender {
	//玩家模型
    private static final ModelBiped steveModel;
    public static final ResourceLocation defaultSteveTexture = new ResourceLocation("textures/entity/steve.png");
    private static final Map<String, ResourceLocation> skinCache = new ConcurrentHashMap<String, ResourceLocation>();
     
    private ModelBiped model = steveModel;
    
    @SuppressWarnings("unused")
	private boolean isAlex = false;
    
    private float headPitch = 0f, headYaw = 0f;
    private float frame = 0f, walk = 0f;;
     
    static {
        steveModel = new ModelBiped();
    }
     
    /**
     * 设置皮肤贴图为默认贴图
     */
    public void setSkin() {
        Minecraft.getMinecraft().renderEngine.bindTexture(defaultSteveTexture);
    }
     
    /**
     * 根据玩家名称自动从网上获取贴图,在贴图读取完毕前,玩家贴图会显示为默认贴图. 
     */
    public void setSkin(String name) {
        ResourceLocation resourceLocation = skinCache.get(name);
        if(resourceLocation == null)
        {   //此处为net.minecraft.util下的StringUtils
            resourceLocation = new ResourceLocation("skins/" + StringUtils.stripControlCodes(name));
            AbstractClientPlayer.getDownloadImageSkin(resourceLocation, name);
            skinCache.put(name, resourceLocation);
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }
     
    /**
     * 设置皮肤贴图为一个已有的贴图
     */
    public void setSkin(ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }
     
    /**
     * 设置头部的朝向,默认是平视正前方
     * @param pitch 负数朝上看,正数朝下看,单位为角度制
     * @param yaw 负数朝左看,正数朝右看,单位为角度制
     */
    public void setHeadLook(float pitch, float yaw) {
        headPitch = pitch;
        headYaw = yaw;
    }
     
    /**
     * 手动设定当前帧数,通常来说是不需要的
     */
    public void setFrame(float frameTime) {
        frame = frameTime;
    }
     
    /**
     * 设定人物行走动作,0.0为原地站立不动,1.0为正常行走
     * @param speed
     */
    public void setWalk(float speed) {
        walk = speed;
    }
     
    public void render(int posX, int posY, float pitch, float yaw, float roll, float size) {
        render(posX, posY, pitch, yaw, roll, size, 0f);
    }
     
    /**
     * 在屏幕上渲染玩家.
     * @param posX 屏幕坐标上的原点位置的X坐标
     * @param posY 屏幕坐标上的原点位置的Y坐标
     * @param pitch 绕X轴旋转角度,单位为角度制.负数上仰,正数下俯.注意坐标原点是在玩家的脚下而不是正中心.
     * @param yaw 绕Y轴旋转的角度,单位为角度制.负数向左,正数向右.
     * @param roll 绕Z轴旋转的角度,单位为角度制
     * @param size 缩放倍数,0.0的话就看不见了...建议在2.0~4.0之间.
     * @param delta 递增帧数的参数,用于维持播放玩家的行走动作和手臂自然摆动等,通过控制数值大小可以控制动作速度.
     */
    public void render(int posX, int posY, float pitch, float yaw, float roll, float size, float delta) {
        frame += delta;
        model.setRotationAngles(frame * walk, walk, frame, headYaw, headPitch, 0f, null);
         
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        //这里是个很玄学的地方,需要把深度测试的方式颠倒一下才能正常渲染,我承认我也不知道这为什么...但它JustWork™!
        //不这样做的话,渲染出来的效果会跟没开深度测试似的
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_GREATER);
        GL11.glTranslatef(posX, posY, -500.0f);
        GL11.glScalef(size, size, size);        
        GL11.glRotatef(roll, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);      
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        model.bipedHead.render(1.0f);
        model.bipedBody.render(1.0f);
        model.bipedRightArm.render(1.0f);
        model.bipedLeftArm.render(1.0f);
        model.bipedRightLeg.render(1.0f);
        model.bipedLeftLeg.render(1.0f);
        model.bipedHeadwear.render(1.0f);
         
        GL11.glDepthFunc(GL11.GL_LEQUAL);;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
    }
}
