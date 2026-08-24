package org.samo_lego.antilogout.config;

import net.fabricmc.loader.api.FabricLoader;
import org.samo_lego.antilogout.AntiLogout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;

public record AntiLogoutConfig(
	boolean disableAllLogouts,
	boolean debug,

	String afkMessage,
	int permissionLevel,
	double maxAfkTime,
	String afkCombatMessage,
	String afkBroadcastMessage,

	boolean notifyOnCombat,
	String combatEndMessage,
	String inCombatMessage,
	int combatTimeout,
	boolean playerHurtOnly,
	String combatDisconnectMessage
) {
	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(AntiLogout.MOD_ID + ".properties").toFile();
	private static final AntiLogoutConfig DEFAULT_CONFIG = new AntiLogoutConfig(
		false,
		false,

		"You are now AFK!",
		0,
		300,
		"You disconnected while in combat!",
		"{player} is now AFK!",

		true,
		"You are no longer in combat!",
		"\uD83D\uDDE1 {time}",
		30,
		true,
		"disconnected while in combat!"
	);

	public static AntiLogoutConfig CONFIG = DEFAULT_CONFIG;

	public static void load() {
		if (!CONFIG_FILE.exists()) {
			save();
			return;
		}

		Properties properties = new Properties();
		try (InputStream inputStream = Files.newInputStream(CONFIG_FILE.toPath())) {
			properties.load(inputStream);

			CONFIG = new AntiLogoutConfig(
				Boolean.parseBoolean(properties.getProperty("disableAllLogouts", String.valueOf(DEFAULT_CONFIG.disableAllLogouts))),
				Boolean.parseBoolean(properties.getProperty("debug", String.valueOf(DEFAULT_CONFIG.debug))),

				properties.getProperty("afkMessage", DEFAULT_CONFIG.afkMessage),
				Integer.parseInt(properties.getProperty("permissionLevel", String.valueOf(DEFAULT_CONFIG.permissionLevel))),
				Double.parseDouble(properties.getProperty("maxAfkTime", String.valueOf(DEFAULT_CONFIG.maxAfkTime))),
				properties.getProperty(properties.getProperty("afkCombatMessage", DEFAULT_CONFIG.afkCombatMessage)),
				properties.getProperty(properties.getProperty("afkBroadcastMessage", DEFAULT_CONFIG.afkBroadcastMessage)),

				Boolean.parseBoolean(properties.getProperty("notifyOnCombat", String.valueOf(DEFAULT_CONFIG.notifyOnCombat))),
				properties.getProperty("combatEndMessage", DEFAULT_CONFIG.combatEndMessage),
				properties.getProperty("inCombatMessage", DEFAULT_CONFIG.inCombatMessage),
				Integer.parseInt(properties.getProperty("combatTimeout", String.valueOf(DEFAULT_CONFIG.combatTimeout))),
				Boolean.parseBoolean(properties.getProperty("playerHurtOnly", String.valueOf(DEFAULT_CONFIG.playerHurtOnly))),
				properties.getProperty("combatDisconnectMessage", DEFAULT_CONFIG.combatDisconnectMessage)
			);
		} catch (IOException exception) {
			save();
		}
	}

	public static void save() {
		Properties properties = new Properties();

		properties.setProperty("disableAllLogouts", String.valueOf(CONFIG.disableAllLogouts));
		properties.setProperty("debug", String.valueOf(CONFIG.debug));

		properties.setProperty("afkMessage", CONFIG.afkMessage);
		properties.setProperty("permissionLevel", String.valueOf(CONFIG.permissionLevel));
		properties.setProperty("maxAfkTime", String.valueOf(CONFIG.maxAfkTime));
		properties.setProperty("afkCombatMessage", CONFIG.afkCombatMessage);
		properties.setProperty("afkBroadcastMessage", CONFIG.afkBroadcastMessage);

		properties.setProperty("notifyOnCombat", String.valueOf(CONFIG.notifyOnCombat));
		properties.setProperty("combatEndMessage", String.valueOf(CONFIG.combatEndMessage));
		properties.setProperty("inCombatMessage", String.valueOf(CONFIG.inCombatMessage));
		properties.setProperty("combatTimeout", String.valueOf(DEFAULT_CONFIG.combatTimeout));
		properties.setProperty("playerHurtOnly", String.valueOf(CONFIG.playerHurtOnly));
		properties.setProperty("combatDisconnectMessage", CONFIG.combatDisconnectMessage);

		try (OutputStream outputStream = Files.newOutputStream(CONFIG_FILE.toPath())) {
			properties.store(outputStream, "AntiLogout Config");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
