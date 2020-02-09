package io.github.ktasra.config;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.ktasra.Crystal;

public class ConfigHandler {

	private final Crystal plugin;
	private Set<IConfig> configs;

	public ConfigHandler(Crystal plugin) {
		this.plugin = plugin;
		configs = new HashSet<>();
	}

	public void register(IConfig config) {
		String fileName = config.getName();
		File file = new File(plugin.getDataFolder(), fileName);
		if (!file.exists()) {
			plugin.saveResource(fileName, false);
		}

		configs.add(config);
		load(config);
	}

	public FileConfiguration load(IConfig config) {
		String fileName = config.getName();
		File file = new File(plugin.getDataFolder(), fileName);
		FileConfiguration configFile = new YamlConfiguration();
		try {
			configFile.load(file);
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return configFile;
	}

	public void loadAll() {
		for (IConfig key : configs) {
			load(key);
		}
	}

	public IConfig forName(String name) {
		for(IConfig config : configs) {
			if(name.equals(config.getName())) {
				return config;
			}
		}
		return null;
	}
}
