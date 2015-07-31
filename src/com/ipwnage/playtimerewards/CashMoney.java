package com.ipwnage.playtimerewards;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.logging.Logger;

public class CashMoney extends JavaPlugin implements Listener {
	public static Economy econ = null;
	public static final Logger log = Logger.getLogger("Minecraft");
	public boolean debug = false;
	public int timeout = getConfig().getInt("timeout");
	public boolean logConsole  = getConfig().getBoolean("logToConsole");
	public boolean measeagePlayer = getConfig().getBoolean("messagePlayer");
	public boolean checkAFK = getConfig().getBoolean("checkForAfk");
	public int delay = getConfig().getInt("delay") * 20;
	public double regularRate = getConfig().getDouble("nonDonatorAmount");
	public double donatorRate = getConfig().getDouble("donatorAmount");

	private AFKListener afkcheck;
	private PlayerData data = new PlayerData();
	private File config = new File(getDataFolder(), "config.yml");



	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		if(!config.exists()) {
			this.saveDefaultConfig();
			log.info((String.format("[%s] - Didn't find a configuration file, will make one.", getDescription().getName())));
		}
		if(!setupEconomy()){
			log.severe((String.format("[%s] - Your server doesn't have Vault installed. Disabling plugin.", getDescription().getName())));
			getServer().getPluginManager().disablePlugin(this);
		}

		getCommand("pr").setExecutor(new CommandBase(this));
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		if(checkAFK) {
			afkcheck = new AFKListener(data, this);
			scheduler.scheduleSyncRepeatingTask(this, afkcheck, 10, 10);
		}
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for(String player : data.getPlayers()) {
					if (!data.isAFK(player)){
						//If player has the playertime.earn.regular permission, give them the regular rate.
						if (getServer().getPlayer(player).hasPermission("playertime.earn.regular") && !getServer().getPlayer(player).hasPermission("playertime.earn.donor")) {
							CashMoney.econ.depositPlayer(player, regularRate);
							if(measeagePlayer) {
								getServer().getPlayer(player).sendMessage(ChatColor.DARK_GREEN + String.format("[Rewards] You just received %f %s for playing on the server! Thanks!",regularRate, CashMoney.econ.currencyNamePlural()));
							}
							if(logConsole) {
								log.info((ChatColor.DARK_GREEN + String.format("[PlaytimeRewards] " + player + " just received %f %s for playing on the server.",regularRate, CashMoney.econ.currencyNamePlural())));
							}
						}
						//If player has the playertime.earn.donor permission, give them the donator rate.
						if (getServer().getPlayer(player).hasPermission("playertime.earn.donor") && !getServer().getPlayer(player).hasPermission("playertime.earn.regular")) {
							CashMoney.econ.depositPlayer(player, donatorRate);
							if(measeagePlayer) {
								getServer().getPlayer(player).sendMessage(ChatColor.DARK_GREEN + String.format("[Rewards] You just received %f %s for playing on the server! Thanks!",donatorRate, CashMoney.econ.currencyNamePlural()));
							}
							if(logConsole) {
								log.info((ChatColor.DARK_GREEN + String.format("[PlaytimeRewards] " + player + " just received %f %s for playing on the server.",donatorRate, CashMoney.econ.currencyNamePlural())));
							}
						}
						//If player has both the playertime.earn.donor and playertime.earn.regular permissions, give them the donator rate. (The dual permission is most likely a result of inheritance and players shouldn't receive both)
						if (getServer().getPlayer(player).hasPermission("playertime.earn.donor") && getServer().getPlayer(player).hasPermission("playertime.earn.regular")) {
							CashMoney.econ.depositPlayer(player, donatorRate);
							if(measeagePlayer) {
								getServer().getPlayer(player).sendMessage(ChatColor.DARK_GREEN + String.format("[Rewards] You just received %f %s for playing on the server! Thanks!",donatorRate, CashMoney.econ.currencyNamePlural()));
							}
							if(logConsole) {
								log.info((ChatColor.DARK_GREEN + String.format("[PlaytimeRewards] " + player + " just received %f %s for playing on the server.",donatorRate, CashMoney.econ.currencyNamePlural())));
							}
						}
					}
				}
			}
		}, delay, delay);

	}
	@Override
	public void onDisable() {

	}

	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerLeave(PlayerQuitEvent e) {
		afkcheck.purgePlayer(e.getPlayer().getName());
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}


}
