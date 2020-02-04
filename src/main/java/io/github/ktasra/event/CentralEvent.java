package io.github.ktasra.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.ktasra.Crystal;

public class CentralEvent implements Listener {

	private Crystal plugin;

	public CentralEvent(Crystal plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");

		BukkitRunnable run = new BukkitRunnable() {

			@Override
			public void run() {
				final String sql_select = "SELECT login_count FROM status WHERE uuid = ?";
				final String sql_update = "UPDATE status SET login_count = ? WHERE uuid = ?";
				final String sql_insert = "INSERT INTO status (uuid, login_count) VALUES (?, ?)";

				Player target = event.getPlayer();
				String uuid = target.getUniqueId().toString();
				target.sendMessage("Welcome to CrystalNetwork.");

				ResultSet result;
				int loginCount;
				boolean hasNext;
				try (PreparedStatement state = plugin.getConnection().prepareStatement(sql_select)) {
					state.setString(1, uuid);
					result = state.executeQuery();
					hasNext = result.next();
					loginCount = result.getInt("login_count");
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
				if (hasNext) {
					try (PreparedStatement state = plugin.getConnection().prepareStatement(sql_update)) {
						loginCount++;
						state.setInt(1, loginCount);
						state.setString(2, uuid);
						state.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
						return;
					}
				} else {
					loginCount = 1;
					try (PreparedStatement state = plugin.getConnection().prepareStatement(sql_insert)) {
						state.setString(1, uuid);
						state.setInt(2, loginCount);
						state.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();

						return;
					}
				}
				Crystal.logger().info(target.getDisplayName() + ">> login count: " + loginCount);
			}
		};
		run.runTaskAsynchronously(plugin);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player target = event.getPlayer();
		String msg = ChatColor.translateAlternateColorCodes('&', event.getMessage());
		for (Player player : target.getServer().getOnlinePlayers()) {
			player.sendMessage(target.getDisplayName() + "§e>>§r " + msg);
		}
		target.getServer().getConsoleSender()
		.sendMessage(target.getDisplayName() + " typed \"" + msg + "§r\" (" + event.getMessage() + ")");
	}
}
