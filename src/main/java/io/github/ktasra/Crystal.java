package io.github.ktasra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.ktasra.command.Branch;
import io.github.ktasra.command.DevelopCommand;
import io.github.ktasra.config.AliasesConfig;
import io.github.ktasra.config.ConfigHandler;
import io.github.ktasra.config.IConfig;
import io.github.ktasra.event.CentralEvent;

public class Crystal extends JavaPlugin {

	private static Logger logger;

	private ConfigHandler configHandler;
	public ConfigHandler getConfigHandler() {
		return configHandler;
	}

	private IConfig aliasConfig;

	private Branch branch;

	private Connection connection;
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;

	@Override
	public void onEnable() {
		logger = getLogger();

		saveDefaultConfig();

		FileConfiguration config = getConfig();
		host = config.getString("mysql.host");
		port = config.getInt("mysql.port");
		database = config.getString("mysql.database");
		username = config.getString("mysql.username");
		password = config.getString("mysql.password");

		try {
			openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			logger.log(Level.WARNING, "Failed to connect database", e);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		aliasConfig = new AliasesConfig();
		configHandler = new ConfigHandler(this);
		configHandler.register(aliasConfig);

		branch = new Branch(this);
		branch.add("develop", new DevelopCommand(this));
		CentralEvent centralEvent = new CentralEvent(this);
		getServer().getPluginManager().registerEvents(centralEvent, this);
	}

	@Override
	public void onDisable() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Failed to disconnect database", e);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String request = command.getName();
		branch.runCommand(sender, request, Arrays.asList(args));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		String request = command.getName();
		return branch.onTabComplete(sender, request, Arrays.asList(args));
	}

	private void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
					password);
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static Logger logger() {
		return logger;
	}
}
