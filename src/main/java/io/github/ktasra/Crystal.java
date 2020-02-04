package io.github.ktasra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.ktasra.event.CentralEvent;

public class Crystal extends JavaPlugin {

	private static Logger logger;

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
			logger.log(Level.WARNING, "Failed database connection", e);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		CentralEvent centralEvent = new CentralEvent(this);
		getServer().getPluginManager().registerEvents(centralEvent, this);
	}

	private void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		}
	}

	public static Logger logger() {
		return logger;
	}

	public Connection getConnection() {
		return connection;
	}
}
