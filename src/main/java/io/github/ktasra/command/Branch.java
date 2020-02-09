package io.github.ktasra.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import io.github.ktasra.Crystal;

public class Branch {

	private Map<String, ACommand> map;

	public Branch(Crystal plugin) {
		map = new HashMap<>();
	}

	public void add(String key, ACommand command) {
		map.put(key, command);
	}

	public void runCommand(CommandSender sender, String command, List<String> args) {
		ACommand iCommand = getCommand(command, args);
		if (iCommand != null) {
			Crystal.logger().info(sender.getName() + "executed: /" + command + " " + String.join(" ", Aliases.convertAll(args)));
			iCommand.executeCommand(sender, Aliases.convertAll(args));
		}
	}

	public List<String> onTabComplete(CommandSender sender, String command, List<String> args) {
		ACommand iCommand = getCommand(command, args);
		if (iCommand != null) {
			return iCommand.onTabComplete(sender, Aliases.convertAll(args));
		}
		return null;
	}

	private ACommand getCommand(String command, List<String> args) {
		List<String> list = new ArrayList<>();
		list.add(command);
		for (String arg1 : args) {
			list.addAll(Arrays.asList(Aliases.convert(arg1).split(" ")));
		}
		String line = String.join(" ", list).toLowerCase();
		String result = null;
		for (String key : map.keySet()) {
			if (line.startsWith(key)) {
				if (result == null || key.length() < result.length()) {
					result = key;
				}
			}
		}
		return map.get(result);
	}
}