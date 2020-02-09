package io.github.ktasra.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.ktasra.Crystal;

public class Aliases {

	private static Map<String, String> map;

	public static Map<String, String> getMap() {
		return map;
	}

	static {
		map = new HashMap<>();
	}

	public static void addAliases(String key, String value) {
		map.put(key, value);
		Crystal.logger().info("added: " + key + " = " + value);
	}

	public static void clear() {
		map.clear();
	}

	public static String convert(String argument) {
		Crystal.logger().info("request: " + argument);
		String str = argument.toLowerCase();
		return map.getOrDefault(str, str);
	}

	public static List<String> convertAll(List<String> args) {
		List<String> result = new ArrayList<>();
		for (String arg : args) {
			result.addAll(Arrays.asList(convert(arg).split(" ")));
		}
		return result;
	}
}
