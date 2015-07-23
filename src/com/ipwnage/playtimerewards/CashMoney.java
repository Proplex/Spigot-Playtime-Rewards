package com.ipwnage.playtimerewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.ipwnage.playtimerewards.CommandBase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.scheduler.BukkitScheduler;

public class CashMoney extends JavaPlugin implements Listener {
    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    private File config = new File(getDataFolder(), "config.yml");
    private File TextPrompt = new File(getDataFolder(), "TextPrompts.txt");
    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    public Map<String, Integer> taskID = new HashMap<String, Integer>();


    //The formula for interval is secconds * ticks(Make this 20). I.E: 60 * 20
    protected long interval = 60 * 20;
    //The amount of money each player will get each interval.
    protected double rate = 0.5;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

        if(!config.exists()) {
            this.saveDefaultConfig();
            log.info((String.format("[%s] - Didn't find a configuration file, will make one.", getDescription().getName())));
        }
        if(!TextPrompt.exists()) {
            this.saveResource("TextPrompts.txt", false);
            log.info((String.format("[%s] - Didn't find a Text Prompt file, will make one.", getDescription().getName())));
        }
        if(!setupEconomy()){
            log.severe((String.format("[%s] - Your server doesn't have Vault installed. Disabling plugin.", getDescription().getName())));
            //getServer().getPluginManager().disablePlugin(this);
        }

        //Setup the base command--/pr
        getCommand("pr").setExecutor(new CommandBase(this));

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




    @EventHandler
    public void onPLayerJoin(final PlayerJoinEvent event){
        final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                //log.info("Giving " + rate + " to " + event.getPlayer().getName());
                econ.depositPlayer(event.getPlayer().getName(), rate);
            }
        }, 0, interval);

        taskID.put(event.getPlayer().getName(), tid);
    }
    
    @EventHandler
    public void onPLayerLeave(PlayerQuitEvent e){
        if(taskID.containsKey(e.getPlayer().getName())){
            int tid = taskID.get(e.getPlayer().getName());
            getServer().getScheduler().cancelTask(tid);
            taskID.remove(e.getPlayer().getName());
        }
    }



}
