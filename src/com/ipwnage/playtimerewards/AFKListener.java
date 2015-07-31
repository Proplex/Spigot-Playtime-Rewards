package com.ipwnage.playtimerewards;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class AFKListener implements Runnable {
	private PlayerData data;
	private CashMoney cm;

	public AFKListener(PlayerData data, CashMoney cm){
		this.data = data;
		this.cm = cm;
	}

	@Override
	public void run() {
		monitorAFK();
	}

	public void monitorAFK() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			String username = player.getName();
			float location = player.getLocation().getYaw();
			if (data.playerExists(username)) {
				if (data.getPlayerLocation(username).equals(location)) {
					if ((System.currentTimeMillis() / 1000L) - data.getPlayerTimestamp(username) > cm.timeout) {
						if(!data.isAFK(username)) {
							player.sendMessage(ChatColor.DARK_GREEN + "[iPwnAge] " + ChatColor.AQUA + "You are now AFK! You are " + ChatColor.RED + "NOT " + ChatColor.AQUA + "receiving money for playing.");
						}
						data.setAFK(username, true);
					}
				} else {
					data.storePlayerLocation(username, location);
					data.storePlayerTimestamp(username, System.currentTimeMillis() / 1000L);
					if(data.isAFK(username)) {
						player.sendMessage(ChatColor.DARK_GREEN + "[iPwnAge] " + ChatColor.AQUA + "You are no longer AFK! You are once again receiving money for playing.");
					}
					data.setAFK(username, false);
				}
			} else {
				data.storePlayerTimestamp(username, System.currentTimeMillis() / 1000L);
				data.storePlayerLocation(username, location);
				data.setAFK(username, false);
			}	
		}
	}

	public void purgePlayer(String username) {
		data.clearPlayer(username);
	}

	public PlayerData getData() {
		return data;
	}

}