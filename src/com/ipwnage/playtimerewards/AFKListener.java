package com.ipwnage.playtimerewards;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class AFKListener {
    CashMoney cashMoney;
    PlayerData data = new PlayerData();

    public AFKListener(CashMoney CashMoney){
        this.cashMoney  = CashMoney;
        
    	for(Player player : Bukkit.getOnlinePlayers()) {
    		String username = player.getName();
    		Location location = player.getLocation();
    		if (data.playerExists(username)) {
    			if (data.getPlayerLocation(username) == location) {
    				if (data.getPlayerTimestamp(username) - System.currentTimeMillis() / 1000L > 5) {
    					data.setAFK(username, true);
    				}
    			} else {
    				data.storePlayerLocation(username, location);
    				data.setAFK(username, false);
    			}
    		} else {
    			data.storePlayerTimestamp(username, System.currentTimeMillis() / 1000L);
    			data.storePlayerLocation(username, location);
    			data.setAFK(username, false);
    		}
    	}
    }
}