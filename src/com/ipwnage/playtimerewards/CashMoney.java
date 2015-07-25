package com.ipwnage.playtimerewards;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    public Map<Player, Integer> pi = new HashMap<Player, Integer>();

    public int timeout = getConfig().getInt("timeout") / 2;
    public boolean logConsole  = getConfig().getBoolean("logToConsole");
    public boolean measeagePlayer = getConfig().getBoolean("messagePlayer");
    public boolean checkAfk = getConfig().getBoolean("checkForAfk");
    public int delay = getConfig().getInt("delay");
    protected double rate = getConfig().getInt("semiCreativeAmountToGive");
    protected double donatorRate = getConfig().getDouble("donatorSemiCreativeAmountToGive");
    protected double survivalWorldRate = getConfig().getDouble("survivalAmountToGive");
    protected double survivalWorldDonatorRate = getConfig().getDouble("donatorSurvivalAmountToGive");

    private static final Logger log = Logger.getLogger("Minecraft");
    private File config = new File(getDataFolder(), "config.yml");
    private BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    private Map<String, Integer> taskID = new HashMap<String, Integer>();


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        if(!config.exists()) {
            this.saveDefaultConfig();
            log.info((String.format("[%s] - Didn't find a configuration file, will make one.", getDescription().getName())));
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
        final Player p = event.getPlayer();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AFKListener(this, event.getPlayer()), 0, 2*20);

        if(p.hasPermission("ipwnage.rate.donator")){
            //Rate for donators.
            final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    if(logConsole){
                        log.info(String.format("[%s] %s just recieved payment of: %d", getName(), p.getName(), donatorRate));
                    }
                    //Yes. I know this method is deprecated, but it works.
                    if(measeagePlayer){
                        p.sendMessage(ChatColor.DARK_GREEN + String.format("[iPwnAge Rewards] You just recieved %d for playing on the server! Thanks!", donatorRate));
                    }
                    econ.depositPlayer(event.getPlayer().getName(), donatorRate);
                }
            }, 0, delay);
            taskID.put(event.getPlayer().getName(), tid);

        }else{
        //Ethier you have the permission or you don't. No need for this if/else to go any further.
            //Rate for non-donators.
            final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {

                    if(logConsole){
                        log.info(String.format("[%s] %s just recieved payment of: %d", getName(), event.getPlayer(), rate));
                    }

                    if(measeagePlayer){
                        p.sendMessage(ChatColor.DARK_GREEN + String.format("[iPwnAge Rewards] You just recieved %d for playing on the server! Thanks!", rate));
                    }
                    //Yes. I know this method is deprecated, but it works.
                    econ.depositPlayer(event.getPlayer().getName(), rate);
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
            pi.put(e.getPlayer(), 0);
        }else{
            pi.put(e.getPlayer(), 0);
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
