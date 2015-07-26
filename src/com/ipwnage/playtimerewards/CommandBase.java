package com.ipwnage.playtimerewards;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.permissions.CommandPermissions;

public class CommandBase  implements CommandExecutor {
	
	private CashMoney plugin;
	
	public CommandBase(CashMoney cashMoney){
		this.plugin = cashMoney;
	}
	//This does NOT work at the moment. July 10, 2015 at 9:08 AM EST.
	//--Drew
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("pr")) {
            if (player.isOp()) {
                if (args[0] == null) {
                    //Explain how to use the command here.

                    player.sendMessage("Available commands are: rate, reload, cancelplayer, enableplayer");
                    player.sendMessage("Ex: /pr rate normal 1.5");
                }

                if (args[0] == "rate") {
                    if(args[1] == null){
                        //Explain how to use rate here
                        player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        player.sendMessage("Available variables: normal, normalDonator, survival, survivalDonator");
                    }


                    if(args[1] == "normal" ){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.rate = args2;


                    }else if(args[1] == "normalDonator"){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.donatorRate = args2;

                        Bukkit.broadcast(String.format("%s just set the rate of normal donator to %f",sender.getName(),args2),"ipwnage.alerts.recieve");


                    }else if(args[1] == "survival"){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.survivalWorldRate = args2;

                    }else if(args[1] == "survivalDonator"){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.survivalWorldDonatorRate = args2;


                    }else{
                        player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                    }
                }


                if(args[0] == "reload"){

                    player.sendMessage(String.format("Reloading %s", plugin.getName()));

                    plugin.getPluginLoader().disablePlugin(plugin);
                    plugin.getPluginLoader().enablePlugin(plugin);

                    player.sendMessage(String.format("%s was successfully reloaded", plugin.getName()));

                }

                if(args[0] == "canelplayer"){
                    if(args[1] == null){
                        //Explain how to use cancelplayer
                    }
                }

                else{
                    player.sendMessage("Unknown command! Try /pr for a list of available commands.");
                }

            }
            player.sendMessage("You do not have permission!");
            return true;
        }
        return false;
    }
}
