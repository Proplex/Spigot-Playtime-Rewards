package com.ipwnage.playtimerewards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandBase implements CommandExecutor {
	
	public CommandBase(CashMoney cashMoney){
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player player  = (Player) sender;
		if(command.getName().equalsIgnoreCase("pr")){
			player.sendMessage("You executed command 'pr'. Congratulations.");
			return true;
		}else{
			return false;
		}
		
	}
}
