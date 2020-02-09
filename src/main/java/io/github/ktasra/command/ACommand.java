package io.github.ktasra.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public abstract class ACommand {

	private Map<Integer, Collection<String>> indexList;
	private Map<String, Collection<String>> lastList;

	public ACommand() {
		indexList = new HashMap<>();
		lastList = new HashMap<>();
	}

	public abstract void executeCommand(CommandSender sender, List<String> args);

	public List<String> onTabComplete(CommandSender sender, List<String> args) {
		final int index = args.size();
		final List<String> originals = new ArrayList<>();
		Collection<String> fromIndex = indexList.get(index);

		// List<String> out = new ArrayList<>();
		// for (int i = 0; i < args.size(); i++) {
		// out.add("[" + i + ": " + args.get(i) + "]");
		// }
		// Crystal.logger().info("");
		// Crystal.logger().info(String.join(", ", out));
		// Crystal.logger().info("size: " + index);

		if (fromIndex != null) {
			originals.addAll(fromIndex);
			// Crystal.logger().info("fromIndex != null: [" + String.join(", " , fromIndex)
			// + "]");
		}

		if (index > 1) {
			final String last = args.get(index - 2);
			Collection<String> fromLast = lastList.get(last);
			if (fromLast != null) {
				originals.addAll(fromLast);
				// Crystal.logger().info("fromLast != null: [" + String.join(", ", fromLast) +
				// "]");
			}
		}

		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args.get(index - 1), originals, completions);
		Collections.sort(completions);
		return completions;
	}

	public void addCompletionsFromIndex(int index, Collection<String> completions) {
		if (index > 0) {
			indexList.put(index, completions);
		}
	}

	public void addCompletionsFromIndex(int index, String... completions) {
		addCompletionsFromIndex(index, Arrays.asList(completions));
	}

	public void addCompletionsFromLastWord(String last, Collection<String> completions) {
		if (last.split(" ").length == 1) {
			lastList.put(last, completions);
		}
	}

	public void addCompletionsFromIndex(String last, String... completions) {
		addCompletionsFromLastWord(last, Arrays.asList(completions));
	}
}
