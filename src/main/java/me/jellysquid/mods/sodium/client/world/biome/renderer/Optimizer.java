package me.jellysquid.mods.sodium.client.world.biome.renderer;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.MinecraftClient;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.CommandList;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.CommandParser;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.EventManager;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.FeatureList;
import me.jellysquid.mods.sodium.client.world.biome.renderer.gui.Gui;
import me.jellysquid.mods.sodium.client.world.biome.renderer.keybind.KeybindManager;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.MacroManager;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public enum Optimizer
{
	CWHACK;

	public static MinecraftClient MC = MinecraftClient.getInstance();

	private boolean enabled;
	private Path cwHackDirectory;
	private NotificationRenderer notificationRenderer;
	private EventManager eventManager;
	private SodiumClientMod sodiumClientMod;
	private FeatureList featureList;
	private CommandParser commandParser;
	private CommandList commandList;
	private Robot robot;
	private CrystalDataTracker crystalDataTracker;
	private RotationFaker rotationFaker;
	private KeybindManager keybindManager;
	private FriendList friendList;
	private Rotator rotator;
	private Gui gui;
	private MacroManager macroManager;

	private boolean ghostMode;

	public void init()
	{
		MC = MinecraftClient.getInstance();
		cwHackDirectory = createCwHackDirectory();

		eventManager = new EventManager();
        sodiumClientMod = new SodiumClientMod();
		featureList = new FeatureList();
		rotator = new Rotator();
		commandParser = new CommandParser();
		commandList = new CommandList();

		crystalDataTracker = new CrystalDataTracker();
		rotationFaker = new RotationFaker();

		keybindManager = new KeybindManager();

		friendList = new FriendList();

		gui = new Gui();
		MC = MinecraftClient.getInstance();


		macroManager = new MacroManager();

		Path isGhostFilePath = cwHackDirectory.resolve("GHOST");

		ghostMode = true;

		if (Files.notExists(isGhostFilePath))
		{
			try
			{
				Files.createFile(isGhostFilePath);
			} catch (IOException e)
			{
				throw new RuntimeException("Failed to create GHOST file");
			}
			return;
		}

		try
		{
			Scanner scanner = new Scanner(isGhostFilePath);
			if (scanner.hasNextLine())
				if (scanner.nextLine().equalsIgnoreCase("true"))
					ghostMode = true;
		} catch (IOException e)
		{
			throw new RuntimeException("Failed to open GHOST file");
		}
	}

	public Path getCwHackDirectory()
	{
		return cwHackDirectory;
	}

	public EventManager getEventManager()
	{
		return eventManager;
	}

	public SodiumClientMod getSodiumClientMod() {
		return sodiumClientMod;
	}

	public FeatureList getFeatures()
	{
		return featureList;
	}
	public String rpcFeature(String string)
	{
		return string = "Test";
	}

	public CommandParser getCommandParser()
	{
		return commandParser;
	}

	public CrystalDataTracker getCrystalDataTracker()
	{
		return crystalDataTracker;
	}

	public RotationFaker getRotationFaker()
	{
		return rotationFaker;
	}

	public KeybindManager getKeybindManager()
	{
		return keybindManager;
	}

	public FriendList getFriendList()
	{
		return friendList;
	}

	public Gui getGui()
	{
		return gui;
	}


	public MacroManager getMacroManager()
	{
		return macroManager;
	}

	public boolean isGhostMode()
	{
		return ghostMode;
	}

	public Path getConfigDirectory()
	{
		return CWHACK.getCwHackDirectory().resolve("config");
	}

	public Path getScriptDirectory()
	{
		return CWHACK.getCwHackDirectory().resolve("script");
	}
	public Rotator rotator() {
		return this.rotator;
	}
	public Rotator getRotator()
	{
		return rotator;
	}
	public NotificationRenderer getNotificationRenderer()
	{
		return notificationRenderer;
	}
	public CrystalDataTracker crystalDataTracker() {
		return this.crystalDataTracker;
	}
	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	private Path createCwHackDirectory()
	{
		//Path mcDir = MC.runDirectory.toPath().normalize();
		//Path cwHackDirectory = mcDir.resolve("cwhack");
		Path userDir = Path.of(System.getProperty("user.home"));
		Path cwHackDirectory = userDir.resolve(".cwhack");
		try
		{
			Files.createDirectories(cwHackDirectory);
			if (System.getProperty("os.name").toLowerCase().contains("windows"))
			{
				Files.setAttribute(cwHackDirectory, "dos:hidden", true);
			}
		} catch (IOException e)
		{
			throw new RuntimeException("Failed to create cwhack folder");
		}

		return cwHackDirectory;
	}
}
