package io.github.ktasra.command;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import io.github.ktasra.Crystal;
import io.github.ktasra.config.IConfig;

public class DevelopCommand extends ACommand {

	private Crystal plugin;

	public DevelopCommand(Crystal plugin) {
		this.plugin = plugin;
		addCompletionsFromIndex(1, "config");
	}

	@Override
	public void executeCommand(CommandSender sender, List<String> args) {
		if (args.size() < 1) {
			return;
		}
		if (args.get(0).equals("config")) {
			if (args.size() < 2) {
				return;
			}
			if (args.get(1).equals("reload")) {
				if (args.size() < 3) {
					plugin.getConfigHandler().loadAll();
					sender.sendMessage("Success to reload configurations");
				} else {
					IConfig config = plugin.getConfigHandler().forName(args.get(0));
					if(config != null) {
						plugin.getConfigHandler().load(config);
						sender.sendMessage("Success to reload configuration");
					}else {
						sender.sendMessage("Cloud not find configuration");
					}
				}
			}
		} else if (args.get(0).equals("list")) {
			for (Entry<String, String> entry : Aliases.getMap().entrySet()) {
				sender.sendMessage(entry.getKey() + " : " + entry.getValue());
			}
		}
	}
}
