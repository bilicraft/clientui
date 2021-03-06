package io.bilicraft.r6.clientui.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.bilicraft.r6.clientui.BilicraftConfig;
import io.bilicraft.r6.clientui.BilicraftUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public enum BilicraftPlayerHandler {
	INSTANCE;
	public final static Logger logging = LogManager.getLogger("PlayerHandler");

	private static PlayerStatus status = PlayerStatus.INIT;

	private BilicraftPlayerHandler() {
	}

	@SubscribeEvent
	public void onPlayerEnterWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			// refresh status if first login attempt failed
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.getDisplayName().equals(BilicraftUI.username)) {
				status = PlayerStatus.INIT;

				if (BilicraftConfig.isDebug()) {
					logging.info("player: " + player.getDisplayName() + ", email: " + BilicraftUI.email + ", password: "
							+ BilicraftConfig.password);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientChatReceivedEvent(ClientChatReceivedEvent event) {
		if (status != PlayerStatus.LOGGED_IN) {
			String message = event.message.getFormattedText();
			doCmd(message, event);
			if (BilicraftConfig.isDebug())
				logging.info(message);
		}
	}

	private static void doCmd(String message, ClientChatReceivedEvent event) {
		ChatComponentText bilicraft = new ChatComponentText("");
		switch (getCmdType(message)) {
		case REGISTER:
			registerUser(getPassword());
			// TODO: UI主界面只检查了长度要求 仍然有可能返回错误信息
			bilicraft.appendText("§r§c[BilicraftUI] 已成功注册!§r");
			break;
		case LOGIN_PROMPT:
			switch (status) {
			case FAILED:
				bilicraft.appendText("§r§c[BilicraftUI] 请返回主界面输入正确的密码!!!!!§r");
				break;
			case INIT:
				loginUser(getPassword());
				break;
			default:
				break;
			}
			break;
		case LOGIN_SUCCESS:
			status = PlayerStatus.LOGGED_IN;
			bilicraft.appendText("§r§c[BilicraftUI] 已成功登录!§r");
			break;
		case BINDMAIL:
			bindMail(getMail());
			bilicraft.appendText("§r§c[BilicraftUI] 邮箱已绑定成功!§r");
			break;
		case ERROR:
			status = PlayerStatus.FAILED;
			bilicraft.appendText("§r§c[BilicraftUI] 请返回主界面输入正确的密码!!!!!§r");
			break;
		case NONE:
			bilicraft.appendText(event.message.getFormattedText());
			if (BilicraftConfig.isDebug())
				logging.info("CMDType:NONE");
			break;
		}
		event.message = bilicraft;
	}

	private static CmdType getCmdType(String message) {
		if (message.contains(CmdType.REGISTER.getMessage())) {
			if (BilicraftConfig.isDebug()) {
				logging.info("reg|" +message);
			}
			return CmdType.REGISTER;
		} else if (message.contains(CmdType.LOGIN_PROMPT.getMessage())) {
			if (BilicraftConfig.isDebug()) {
				logging.info("l_p|"+message);
			}
			return CmdType.LOGIN_PROMPT;
		} else if (message.contains(CmdType.LOGIN_SUCCESS.getMessage())) {
			if (BilicraftConfig.isDebug()) {
				logging.info("l_s|"+message);
			}
			return CmdType.LOGIN_SUCCESS;
		} else if (message.contains(CmdType.BINDMAIL.getMessage())) {
			if (BilicraftConfig.isDebug()) {
				logging.info("b|"+message);
			}
			return CmdType.BINDMAIL;
		} else if (message.contains(CmdType.ERROR.getMessage())) {
			if (BilicraftConfig.isDebug()) {
				logging.info("e|"+message);
			}
			return CmdType.ERROR;
		} else if (message.length() == 0) {
			if (BilicraftConfig.isDebug()) {
				logging.info("n");
			}
			return CmdType.NONE;
		} else {
			if (BilicraftConfig.isDebug()) {
				logging.info("no_match: " + message);
			}
			return CmdType.NONE;
		}
	}

	private static void registerUser(String password) {
		String command = "/register " + password + " " + password;
		if (BilicraftConfig.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private static void loginUser(String password) {
		String command = "/login " + password;
		if (BilicraftConfig.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private static void bindMail(String mail) {
		String command = "/email add " + mail + " " + mail;
		if (BilicraftConfig.isDebug())
			logging.info(command);
		BilicraftUI.sendCommandExecute(command);
	}

	private static String getPassword() {
		return BilicraftConfig.password;
	}

	private static String getMail() {
		return BilicraftUI.email;
	}

	/**
	 * 用于处理服务器返回的信息
	 */
	private enum CmdType {
		REGISTER("“/register"), LOGIN_PROMPT("“/login"), LOGIN_SUCCESS("已成功登录"), BINDMAIL("“/email"), ERROR(
				"错误的密码"), NONE("");

		private final String message;

		CmdType(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	/**
	 * 登录状态
	 */
	private enum PlayerStatus {
		INIT, LOGGED_IN, FAILED
	}
}
