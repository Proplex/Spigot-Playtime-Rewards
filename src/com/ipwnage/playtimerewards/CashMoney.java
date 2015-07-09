package com.ipwnage.playtimerewards;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;

public class CashMoney extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;
    @Override
    public void onEnable() {
    	if(!setupEconomy()){
    		log.severe((String.format("[%s] - Your server doesn't have Vault installed. Disabling plugin.", getDescription().getName())));
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
}