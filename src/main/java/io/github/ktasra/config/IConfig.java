package io.github.ktasra.config;

import org.bukkit.configuration.MemorySection;

public interface IConfig {

	void load(MemorySection config);

	String getName();
}
