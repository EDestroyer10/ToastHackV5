package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;

import static net.minecraft.client.resource.language.I18n.translate;

public enum ChatUtils
{
	;
	private static final String prefix = "§f[§9CwHack§f] ";

	public static void log(String message)
	{
		if (Optimizer.CWHACK.isGhostMode())
			return;
		LogManager.getLogger().info("[CWHACK] {}", message.replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
	}
	public static void info(String message)
	{
		if (Optimizer.CWHACK.isGhostMode())
			return;
		String string = prefix + "Info: " + message;
		sendPlainMessage(string);
	}
	public static void error(String message)
	{
		if (Optimizer.CWHACK.isGhostMode())
			return;
		String string = prefix + "§4Error: §f" + message;
		sendPlainMessage(string);
		log(message);
	}
	public static void sendPlainMessage(String message)
	{
		if (Optimizer.CWHACK.isGhostMode())
			return;
		if (Optimizer.MC.inGameHud != null)
			Optimizer.MC.inGameHud.getChatHud().addMessage(Text.literal(message));
	}
	public static void plainMessageWithPrefix(String message) {
		String string = prefix + message;
		ChatUtils.sendPlainMessage(string);
	}
	public void sendMsg(String text) {
		if (Optimizer.MC.player != null) Optimizer.MC.player.sendMessage(Text.of(translate(text)));
	}
}
