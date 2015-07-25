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
    public static boolean isAfk = false;

    private static final Logger log = Logger.getLogger("Minecraft");
    private File config = new File(getDataFolder(), "config.yml");
    private File TextPrompt = new File(getDataFolder(), "TextPrompts.txt");
    private BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    private Map<String, Integer> taskID = new HashMap<String, Integer>();

    //The formula for interval is secconds * ticks(Make this 20). I.E: 60 * 20
    protected long interval = 60 * 20;
    protected double rate = 0.5;
    protected double donatorRate = 1.5;
    protected double survivalWorldRate = 0.1;
    protected double survivalWorlDonatorRate = 0.2;

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
        Player p = event.getPlayer();

        if(p.hasPermission("ipwnage.rate.donator")){
            //Rate for donators.
            final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    //DEBUG:
                    //log.info("Giving " + donatorRate + " to " + event.getPlayer().getName());


                    //Yes. I know this method is deprecated, but it works.
                    econ.depositPlayer(event.getPlayer().getName(), donatorRate);
                }
            }, 0, interval);
            taskID.put(event.getPlayer().getName(), tid);

        }else{     //Ethier you have the permission or you don't. No need for this if/else to go any further.
            //Rate for non-donators.
            final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    //DEBUG:
                    //log.info("Giving " + rate + " to " + event.getPlayer().getName());

                    //Yes. I know this method is deprecated, but it works.
                    econ.depositPlayer(event.getPlayer().getName(), rate);
                }
            }, 0, interval);

            taskID.put(event.getPlayer().getName(), tid);
        }

    }


    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) throws InterruptedException {
        //Store there current x y and z in variables.
        final int xPos = (int) e.getFrom().getX();


        if(yawChange || pitchChange){
            e.getPlayer().sendMessage(ChatColor.AQUA + "You are not afk.");
        }else{
            e.getPlayer().sendMessage(ChatColor.RED  + "You are afk.");
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



    public boolean isAfk(String playerName){
        return true;
    }


}
