package com.ipwnage.playtimerewards;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class AFKListener implements Runnable {
	private PlayerData data;

	public AFKListener(PlayerData data){
		this.data = data;
	}

	@Override
	public void run() {
		monitorAFK();
	}
	
	public void monitorAFK() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			String username = player.getName();
			Location location = player.getLocation();
			if (data.playerExists(username)) {
				if (data.getPlayerLocation(username).toString().equals(location.toString())) {
					if ((System.currentTimeMillis() / 1000L) - data.getPlayerTimestamp(username) > 5) {
						data.setAFK(username, true);
					}
				} else {
					data.storePlayerLocation(username, location);
					data.storePlayerTimestamp(username, System.currentTimeMillis() / 1000L);
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