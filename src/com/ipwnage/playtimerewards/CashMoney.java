package com.ipwnage.playtimerewards;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CashMoney extends JavaPlugin implements Listener {
    public static Economy econ = null;
    public static final Logger log = Logger.getLogger("Minecraft");
    public boolean debug = false;

    public int timeout = getConfig().getInt("timeout");
    public boolean logConsole  = getConfig().getBoolean("logToConsole");
    public boolean measeagePlayer = getConfig().getBoolean("messagePlayer");
    public boolean checkAfk = getConfig().getBoolean("checkForAfk");
    public int delay = getConfig().getInt("delay") * 20;
    public double regularRate = getConfig().getDouble("nonDonatorAmount");
    public double donatorRate = getConfig().getDouble("donatorAmount");
    public double survivalWorldRate = getConfig().getDouble("survivalAmountToGive");
    public double survivalWorldDonatorRate = getConfig().getDouble("donatorSurvivalAmountToGive");
    public String serverName = getConfig().getString("serverName");

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
        AFKListener afkcheck = new AFKListener(this);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, afkcheck, 20, 20);
        
    }
    @Override
    public void onDisable() {

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
