package sh.reece.events;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import sh.reece.events.Commands.EventCMD;
import sh.reece.events.Listeners.Listeners;
import sh.reece.events.Placeholder.EventsExpansion;

public class EventsPlugin extends JavaPlugin {

	private static EventsPlugin instance;
	private Location jailLocation;
	private boolean isPapiInstalled;
	// Reece#3370 - www.reece.sh - all right reserved.
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		instance = this;
		getCommand("event").setExecutor(new EventCMD());
		getServer().getPluginManager().registerEvents(new Listeners(), this);				
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new EventsExpansion(this).register();
			isPapiInstalled = true;
		} else {
			getLogger().severe("PlaceholderAPI not found!");
			isPapiInstalled = false;
		}
	}
	
	public boolean getPAPIStatus() {
		return isPapiInstalled;
	}

	public String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&',
				getConfig().getString("prefix") + " " + getConfig().getString("primarycolor"));
	}

	public String getColorOne() {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("primarycolor"));
	}

	public String getColorTwo() {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("secondarycolor"));
	}

	public Location getJailLocation() {
		if (jailLocation == null) {
			reloadJailLoacation();
		}
		return jailLocation;
	}

	public void reloadJailLoacation() {
		reloadConfig();
		this.jailLocation = Util.stringToLocationWithPitch(getConfig().getString("jail"));
		
	}

	// createConfig("config.yml")
	public void createConfig(String name) {
		File file = new File(getDataFolder(), name);
		if (!new File(getDataFolder(), name).exists()) {
			saveResource(name, false);
		}
		@SuppressWarnings("static-access")
		FileConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
		if (!file.exists()) {
			try {
				configuration.save(file);
			}			
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration getConfigFile(String name) {
		return YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
	}
	
	
	public static EventsPlugin getInstance() {
		return instance;
	}
}
