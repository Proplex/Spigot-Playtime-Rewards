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
    public Map<Player, Double> pi = new HashMap<Player, Double>();
    public Map<String, Integer> taskID = new HashMap<String, Integer>();
    public static final Logger log = Logger.getLogger("Minecraft");

    public int timeout = getConfig().getInt("timeout") / 2;
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
    private BukkitScheduler scheduler = Bukkit.getServer().getScheduler();



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
        final Player p = event.getPlayer();

        if(checkAfk){
            log.info("True!");
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AFKListener(this, event.getPlayer()), 0, 2*20);

        if(p.hasPermission("playertime.rate.donator")){
            //Rate for donators.
            final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    if(logConsole){
                        log.info(String.format("[%s] %s just recieved payment of: %f", getName(), p.getName(), donatorRate));
                    }
                    //Yes. I know this method is deprecated, but it works.
                    if(measeagePlayer){
                        p.sendMessage(ChatColor.DARK_GREEN + String.format("[%s] You just recieved %f for playing on the server! Thanks!",serverName, donatorRate));
                    }
                    econ.depositPlayer(event.getPlayer().getName(), donatorRate);
                }
            }, 0, delay);
            taskID.put(event.getPlayer().getName(), tid);
            new AFKListener().tid = tid;

        }else{
        //Ethier you have the permission or you don't. No need for this if/else to go any further
        //Rate for non-donators.
            final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {

                    if(logConsole){
                        log.info(String.format("[%s] %s just received payment of: %f", getName(), event.getPlayer().getName(), regularRate));
                    }

                    if(measeagePlayer){
                        p.sendMessage(ChatColor.DARK_GREEN + String.format("[%s] You just receiving %f for playing on the server! Thanks!",serverName, regularRate));
                    }
                    //Yes. I know this method is deprecated, but it works.
                    econ.depositPlayer(event.getPlayer().getName(), regularRate);
                }
            }, 0, delay);
            taskID.put(event.getPlayer().getName(), tid);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getFrom().getBlockX()==e.getTo().getBlockX() && e.getFrom().getBlockZ()==e.getTo().getBlockZ()){
            return;
        }
        if(pi.containsKey(e.getPlayer())){
            pi.remove(e.getPlayer());
            pi.put(e.getPlayer(), 0.0);
        }else{
            pi.put(e.getPlayer(), 0.0);
        }

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
