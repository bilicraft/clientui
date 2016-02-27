package io.bilicraft.r6.clientui.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

@SideOnly(Side.CLIENT)
public class BilicraftPlayerHandler {
	private enum CmdType {
		REGISTER("§r§c请输入“/register <密码> <再输入一次以确定密码>”以注册，密码最短8位§r"), LOGIN("§r§c请输入“/login <密码>”以登录§"), BINDMAIL(
				"§r§c请输入“/email add <你的邮箱> <再输入一次以确认>”来绑定邮箱§r"), ERROR("§r§c错误的密码§r"), NONE("");
		private final String message;

		CmdType(final String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public static BilicraftPlayerHandler instance = new BilicraftPlayerHandler();
	public static Logger logging = LogManager.getLogger("PlayerHandler");

	private boolean passwordError = false;

	@SubscribeEvent
	public void onPlayerEnterWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			logging.info("Player:" + player.getDisplayName() + "EMAIL:" + BilicraftUI.email + "PASSWORD"
					+ BilicraftUI.password);
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientChatReceivedEvent(ClientChatReceivedEvent event) {
		String message = event.message.getFormattedText();
		if (!passwordError) {
			doCmd(message, event);

		}
		if (BilicraftUI.isDebug())
			logging.info(message);
	}

	private void doCmd(String message, ClientChatReceivedEvent event) {
		ChatComponentText bilicraft = new ChatComponentText("");
		switch (getCmdType(message)) {
		case REGISTER:
			registerUser(getPassword());
			bilicraft.appendText("§r§c[BilicraftUI]:已成功注册!§r");
			break;
		case LOGIN:
			loginUser(getPassword());
			bilicraft.appendText("§r§c[BilicraftUI]:已成功登陆!§r");
			break;
		case BINDMAIL:
			bindMail(getMail());
			bilicraft.appendText("§r§c[BilicraftUI]:邮箱已绑定成功!§r");
			break;
		case ERROR:
			this.passwordError = true;
			bilicraft.appendText("§r§c[BilicraftUI]:请返回主界面输入正确的密码!!!!!§r");
			break;
		case NONE:
			bilicraft.appendText(event.message.getFormattedText());
			if (BilicraftUI.isDebug())
				logging.info("CMDType:NONE");
			break;
		}
		event.message = bilicraft;
	}

	private CmdType getCmdType(String message) {
		if (message.contains(CmdType.REGISTER.getMessage())) {
			return CmdType.REGISTER;
		} else if (message.contains(CmdType.LOGIN.getMessage())) {
			return CmdType.LOGIN;
		} else if (message.contains(CmdType.BINDMAIL.getMessage())) {
			return CmdType.BINDMAIL;
		} else if (message.contains(CmdType.ERROR.getMessage())) {
			return CmdType.ERROR;
		}
		return CmdType.NONE;
	}

	private void registerUser(String password) {
		String command = "/register " + password + " " + password;
		if (BilicraftUI.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private void loginUser(String password) {
		String command = "/login " + password;
		if (BilicraftUI.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private void bindMail(String mail) {
		String command = "/email add " + mail + " " + mail;
		if (BilicraftUI.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private String getPassword() {
		return BilicraftUI.password;
	}

	private String getMail() {
		return BilicraftUI.email;
	}
}
