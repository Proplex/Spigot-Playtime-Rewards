package com.ipwnage.playtimerewards;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBase  implements CommandExecutor {
	
	private CashMoney plugin;
	CommandPermissions PermManager = new CommandPermissions();
	
	
	public CommandBase(CashMoney cashMoney){
		this.plugin = cashMoney;
	}
	//This does NOT work at the moment. July 10, 2015 at 9:08 AM EST.
	//--Drew
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player player  = (Player) sender;
		if(command.getName().equalsIgnoreCase("pr")){
			if(player.hasPermission(PermManager.DISABLE_PERM) || player.getName() == "Drew1895"){
				//Continue
				if(args[0] == null){
					try {
						player.sendMessage(getTextFromConfig("CMD_ENABLE"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(args[1] == "disable"){
					try {
						player.sendMessage(getTextFromConfig("CMD_DISALBE"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				
			}else{
				//No perm message
				try {
					player.sendMessage(getTextFromConfig("CMD_NO_PERM"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}if(sender instanceof ConsoleCommandSender){
			sender.sendMessage("This is a test");
		}
		return true;
		
	}
	
	//I'm an oldfag when it comes to reading from a file. I happen to like
	//the JavaIO way of doing things. smd.
	//--Drew
	private String getTextFromConfig(String key) throws IOException{
		
		String resp = "nothing";
		InputStream in  = getClass().getResourceAsStream("/TextPrompts.txt");
		Properties prop = new Properties();
		prop.load(in);
		
		resp = prop.getProperty(key);
		
		
		return resp;
	}
	
	
}
