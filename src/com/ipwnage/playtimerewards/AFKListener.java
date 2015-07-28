package com.ipwnage.playtimerewards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by andrewhartpence on 7/25/15.
 */
public class AFKListener implements Runnable{
    CashMoney cashMoney;
    Player p;
    int tid;
    boolean isAfk = false;


    public AFKListener(CashMoney CashMoney, Player p){
        this.cashMoney  = CashMoney;
        this.p = p;
    }
    public AFKListener(){

    }


    @Override
    public void run() {
        if (!cashMoney.pi.containsKey(p)) {
            cashMoney.pi.put(p, 1.0);
        } else {
            double value = cashMoney.pi.get(p) + 1;
            cashMoney.pi.remove(p);
            cashMoney.pi.put(p, value);
        }
        if (cashMoney.pi.containsKey(p)) {
            double value = cashMoney.pi.get(p);
            if (value >= cashMoney.timeout) {
                isAfk = true;
                if(cashMoney.taskID.containsKey(p.getName())){
                	p.sendMessage(String.format(ChatColor.DARK_GREEN + "[%s]" + ChatColor.AQUA +  " You are AFK; you are" + ChatColor.RED + " not"+ ChatColor.AQUA+ " receiving money for playing.", cashMoney.serverName));
                    int tid = cashMoney.taskID.get(p.getPlayer().getName());
                    cashMoney.getServer().getScheduler().cancelTask(tid);
                    cashMoney.taskID.remove(p.getName());
                }
            }


            if (isAfk && value <= 1.0){
                isAfk = false;
                if(p.hasPermission("playertime.rate.donator")){
                    //Rate for donators.
                    if(cashMoney.debug){
                        CashMoney.log.info(String.format("Adding %s to the money scheduler", p.getPlayer().getName()));
                    }
                    final int tid = cashMoney.getServer().getScheduler().scheduleSyncRepeatingTask(cashMoney, new Runnable() {
                        public void run() {
                            if(cashMoney.logConsole){
                                CashMoney.log.info(String.format("[%s] %s just received payment of: %f",cashMoney.getName(), p.getName(), cashMoney.donatorRate));
                            }
                            //Yes. I know this method is deprecated, but it works.
                            if(cashMoney.measeagePlayer){
                                p.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GREEN + String.format("[%s] You just received %f for playing on the server! Thanks!",cashMoney.serverName, cashMoney.donatorRate));
                            }
                            CashMoney.econ.depositPlayer(p.getName(), cashMoney.donatorRate);
                        }
                    }, cashMoney.delay, cashMoney.delay);
                    cashMoney.taskID.put(p.getName(), tid);
                    p.sendMessage(String.format(ChatColor.DARK_GREEN + "[%s]" + ChatColor.AQUA +  " You are no longer AFK; you are once again receiving money for playing.",cashMoney.serverName));

                }else{
                    if(cashMoney.debug){
                        CashMoney.log.info(String.format("Adding %s to the money scheduler", p.getPlayer().getName()));
                    }
                    //Ethier you have the permission or you don't. No need for this if/else to go any further
                    //Rate for non-donators.
                    final int tid = cashMoney.getServer().getScheduler().scheduleSyncRepeatingTask(cashMoney, new Runnable() {
                        @Override
                        public void run() {

                            if(cashMoney.logConsole){
                                CashMoney.log.info(String.format("[%s] %s just recieved payment of: %f",cashMoney.getName(), p.getName(), cashMoney.regularRate));
                            }

                            if(cashMoney.measeagePlayer){
                                p.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GREEN + String.format("[%s] You just received %f for playing on the server! Thanks!",cashMoney.serverName, cashMoney.regularRate));
                            }
                            //Yes. I know this method is deprecated, but it works.
                            CashMoney.econ.depositPlayer(p.getName(), cashMoney.regularRate);
                        }
                    }, cashMoney.delay, cashMoney.delay);
                    cashMoney.taskID.put(p.getName(), tid);
                    p.sendMessage(String.format(ChatColor.DARK_GREEN + "[%s]" + ChatColor.AQUA +  " You are no longer AFK; you are once again receiving money for playing.", cashMoney.serverName));
                }

            }
        }
    }
}