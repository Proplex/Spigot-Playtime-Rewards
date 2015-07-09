package com.ipwnage.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.ipwnage.playtimerewards.CashMoney;

public class CommandBase implements CommandExecutor {
	
	private CashMoney plugin;
	
	public CommandBase(CashMoney cashMoney){
		this.plugin = cashMoney;
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
