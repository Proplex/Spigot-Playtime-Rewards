package com.ipwnage.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.ipwnage.playtimerewards.CashMoney;

public class CommandBase implements CommandExecutor {
	
	private CashMoney plugin;
	//Permission nodes for each rank.
	private String faggotPermission = "ipwn.fag";
	private String normalPermission = "ipwn.norm";
	private String memberPermission = "ipwn.member";
	private String trustedPermission = "ipwn.trus";
	private String veteranPermission = "ipwn.vet";
	private String seniorPermission = "ipwn.senior";
	private String elderPermission = "ipwn.elder";
	private String tribunePermission = "ipwn.trib";
	private String staffPermission = "ipwn.staff";
	private String adminPerm = "ipwn.admin";
	
	
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
