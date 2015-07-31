package com.ipwnage.playtimerewards;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBase  implements CommandExecutor {

	private CashMoney plugin;

	public CommandBase(CashMoney cashMoney){
		this.plugin = cashMoney;
	}

	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("pr")) {
			if(player.hasPermission("playertime.admin") || player.isOp()) {
				if(args.length == 1){
				if(args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					plugin.timeout = plugin.getConfig().getInt("timeout") / 2;
					plugin.logConsole  = plugin.getConfig().getBoolean("logToConsole");
					plugin.measeagePlayer = plugin.getConfig().getBoolean("messagePlayer");
					plugin.checkAFK = plugin.getConfig().getBoolean("checkForAFK");
					plugin.delay = plugin.getConfig().getInt("delay") * 20;
					plugin.regularRate = plugin.getConfig().getDouble("nonDonatorAmount");
					plugin.donatorRate = plugin.getConfig().getDouble("donatorAmount");

					player.sendMessage(String.format(ChatColor.DARK_GREEN + "[Rewards] " + ChatColor.AQUA + "Successfully reloaded configuration"));
				}
				if(args[0].equalsIgnoreCase("updateconfig")) {
					plugin.saveDefaultConfig();
					player.sendMessage(String.format(ChatColor.DARK_GREEN + "[Rewards] " + ChatColor.AQUA + "Successfully updated configuration. You should update the values and then run /pr reload"));
				}
				}

				else{
					player.sendMessage("Unknown command! Try /pr for a list of available commands.");
				}

			} else {
				player.sendMessage("You do not have permission!");
				return false;
			}
		}
		return true;
	}
}
