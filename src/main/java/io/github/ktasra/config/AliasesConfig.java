package io.github.ktasra.config;

import java.util.Map.Entry;

import org.bukkit.configuration.MemorySection;

import io.github.ktasra.command.Aliases;

public class AliasesConfig implements IConfig {

	@Override
	public void load(MemorySection config) {
		Aliases.clear();
		for(Entry<String, Object> entry : config.getValues(false).entrySet()){
			Aliases.addAliases(entry.getKey(), (String) entry.getValue());
		}
	}

	@Override
	public String getName() {
		return "aliases.yml";
	}
}
