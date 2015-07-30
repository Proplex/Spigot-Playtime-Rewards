package com.ipwnage.playtimerewards;

import java.util.HashMap;

import org.bukkit.Location;

public class PlayerData {
	private HashMap<String, Location> playerlocationdata = new HashMap<String, Location>();
	private HashMap<String, Long> playertimestampdata = new HashMap<String, Long>();
	private HashMap<String, Boolean> playerafk = new HashMap<String, Boolean>();
	
	public void storePlayerLocation(String username, Location location) {
		playerlocationdata.put(username, location);
	}
	
	public boolean playerExists(String username) {
		return playerlocationdata.containsKey(username);
	}
	
	public Location getPlayerLocation(String username) {
		return playerlocationdata.get(username);
	}
	
	public void storePlayerTimestamp(String username, Long timestamp) {
		playertimestampdata.put(username, timestamp);
	}
	
	public Long getPlayerTimestamp(String username) {
		return playertimestampdata.get(username);
	}

	public void setAFK(String username, Boolean status) {
		playerafk.put(username, status);
	}
	
	public Boolean isAFK(String username) {
		return playerafk.get(username);
	}
	
	public HashMap<String, Boolean> getAFKdata() {
		return playerafk;
	}
	
	public void clearPlayer(String username) {
		playerlocationdata.remove(username);
		playerafk.remove(username);
		playertimestampdata.remove(username);
	}
}
