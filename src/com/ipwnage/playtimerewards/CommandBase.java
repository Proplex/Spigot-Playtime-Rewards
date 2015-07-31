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
	//This does NOT work at the moment. July 10, 2015 at 9:08 AM EST.
	//--Drew
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
					plugin.checkAfk = plugin.getConfig().getBoolean("checkForAfk");
					plugin.delay = plugin.getConfig().getInt("delay") * 20;
					plugin.regularRate = plugin.getConfig().getDouble("nonDonatorAmount");
					plugin.donatorRate = plugin.getConfig().getDouble("donatorAmount");
					plugin.survivalWorldRate = plugin.getConfig().getDouble("survivalAmount");
					plugin.survivalWorldDonatorRate = plugin.getConfig().getDouble("donatorSurvivalAmount");

					player.sendMessage(String.format(ChatColor.DARK_GREEN + "[Rewards] " + ChatColor.AQUA + "Successfully reloaded configuration"));
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
