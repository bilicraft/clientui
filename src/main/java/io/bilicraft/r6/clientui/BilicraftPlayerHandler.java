package io.bilicraft.r6.clientui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

@SideOnly(Side.CLIENT)
public class BilicraftPlayerHandler {
	private enum CmdType {
		REGISTER, LOGIN, BINDMAIL, NONE
	}

	public static BilicraftPlayerHandler instance = new BilicraftPlayerHandler();
	public static Logger logging = LogManager.getLogger("PlayerHandler");
	private static final String REGISTERCMD = "§r§c请输入“/register <密码> <再输入一次以确定密码>”以注册，密码最短8位§r";
	private static final String LOGINCMD = "§r§c请输入“/login <密码>”以登录§r";
	private static final String BINDMAILCMD = "§r§c请输入“/email add <你的邮箱> <再输入一次以确认>”来绑定邮箱§r";

	@SubscribeEvent
	public void onPlayerEnterWorld(EntityJoinWorldEvent event) {
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			logging.info("Player:"+player.getDisplayName()+"EMAIL:"+BilicraftUI.email+"PASSWORD"+BilicraftUI.password);
		}
		
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientChatReceivedEvent(ClientChatReceivedEvent event) {
		String message =event.message.getFormattedText();
		doCmd(message);
		if (BilicraftUI.isDebug())
			logging.info(message);
	}

	private void doCmd(String message) {
		switch (getCmdType(message)) {
		case REGISTER:
			registerUser(getPassword());
			break;
		case LOGIN:
			loginUser(getPassword());
			break;
		case BINDMAIL:
			bindMail(getMail());
			break;
		case NONE:
			if(BilicraftUI.isDebug())
				logging.info("CMDType:NONE");
			break;
		}
	}

	private CmdType getCmdType(String message) {
			if (message.contains(REGISTERCMD)) {
				return CmdType.REGISTER;
			} else if (message.contains(LOGINCMD)) {
				return CmdType.LOGIN;
			} else if (message.contains(BINDMAILCMD)) {
				return CmdType.BINDMAIL;
			}
		return CmdType.NONE;
	}


	private void registerUser(String password) {
		String command = "/register "+password+" "+password;
		if(BilicraftUI.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private void loginUser(String password) {
		String command = "/login "+password;
		if(BilicraftUI.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}
	private void bindMail(String mail)
	{
		String command = "/email add "+mail+" "+mail;
		if(BilicraftUI.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}
	
	private String getPassword()
	{
		return BilicraftUI.password;
	}
	private String getMail()
	{
		return BilicraftUI.email;
	}
}
