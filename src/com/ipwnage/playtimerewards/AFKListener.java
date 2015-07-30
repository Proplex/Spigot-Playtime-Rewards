package com.ipwnage.playtimerewards;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class AFKListener implements Runnable {
	CashMoney cashMoney;
	PlayerData data = new PlayerData();

	public AFKListener(CashMoney CashMoney){
		this.cashMoney  = CashMoney;
	}

	@Override
	public void run() {
		monitorAFK();
	}
	
	@SuppressWarnings("deprecation")
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
			
			if (!data.isAFK(username)){
				CashMoney.econ.depositPlayer(username, 1.0);
			}
			
		}
	}

	public void purgePlayer(String username) {
		data.clearPlayer(username);
	}

}