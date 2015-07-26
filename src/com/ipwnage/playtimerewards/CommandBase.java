package com.ipwnage.playtimerewards;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                        plugin.getConfig().set("semiCreativeAmountToGive", args2);


                    }else if(args[1] == "normalDonator"){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.donatorRate = args2;
                        plugin.getConfig().set("donatorSemiCreativeAmountToGive", args2);
                        //no u; Bukkit.broadcast(String.format("%s just set the rate of normal donator to %f",sender.getName(),args2),"ipwnage.alerts.recieve");


                    }else if(args[1] == "survival"){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.survivalWorldRate = args2;
                        plugin.getConfig().set("survivalAmountToGive", args2);

                    }else if(args[1] == "survivalDonator"){
                        if(args[2] == null){
                            player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                        }

                        double args2 = Double.parseDouble(args[2]);
                        plugin.survivalWorldDonatorRate = args2;
                        plugin.getConfig().set("donatorSurvivalAmountToGive", args2);


                    }else{
                        player.sendMessage("Proper usage is: /pr rate <variable> <decimal>");
                    }
                }


                if(args[0] == "reload"){

                    player.sendMessage(String.format("Reloading %s", plugin.getName()));
                    plugin.reloadConfig();
                    plugin.timeout = plugin.getConfig().getInt("timeout") / 2;
                    plugin.logConsole  = plugin.getConfig().getBoolean("logToConsole");
                    plugin.measeagePlayer = plugin.getConfig().getBoolean("messagePlayer");
                    plugin.checkAfk = plugin.getConfig().getBoolean("checkForAfk");
                    plugin.delay = plugin.getConfig().getInt("delay") * 20;
                    plugin.rate = plugin.getConfig().getDouble("semiCreativeAmountToGive");
                    plugin.donatorRate = plugin.getConfig().getDouble("donatorSemiCreativeAmountToGive");
                    plugin.survivalWorldRate = plugin.getConfig().getDouble("survivalAmountToGive");
                    plugin.survivalWorldDonatorRate = plugin.getConfig().getDouble("donatorSurvivalAmountToGive");

                    player.sendMessage(String.format("%s sucessfully reloaded configuration", plugin.getName()));

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
