package com.ipwnage.playtimerewards;

import java.util.HashMap;

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
			CashMoney.log.info("LOCATION " + location.toString());
			Long time = System.currentTimeMillis() / 1000L;
			CashMoney.log.info("TIME NOW " + time.toString());
			if (data.playerExists(username)) {
				Long afkcount = (System.currentTimeMillis() / 1000L) - data.getPlayerTimestamp(username);
				CashMoney.log.info("PLAYER SAVED LOCATION " + data.getPlayerLocation(username));
				CashMoney.log.info("PLAYER SAVED TIME " + data.getPlayerTimestamp(username));
				CashMoney.log.info("AFK COUNT " + afkcount.toString());
				if (data.getPlayerLocation(username).toString().equals(location.toString())) {
					if ((System.currentTimeMillis() / 1000L) - data.getPlayerTimestamp(username) > 5) {
						CashMoney.log.info(username + " is now AFK");
						data.setAFK(username, true);
					}
					CashMoney.log.info(username + " is in the same location but not 5 seconds.");
				} else {
					CashMoney.log.info(username + " is moving.");
					data.storePlayerLocation(username, location);
					data.storePlayerTimestamp(username, System.currentTimeMillis() / 1000L);
					data.setAFK(username, false);
				}
			} else {
				CashMoney.log.info(username + " is new");
				data.storePlayerTimestamp(username, System.currentTimeMillis() / 1000L);
				data.storePlayerLocation(username, location);
				data.setAFK(username, false);
			}
			
			if (!data.isAFK(username)){
				CashMoney.econ.depositPlayer(username, 1.0);
			}
			
		}
	}


}