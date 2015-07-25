package com.ipwnage.playtimerewards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by andrewhartpence on 7/25/15.
 */
public class AFKListener implements Runnable{
    CashMoney cashMoney;
    Player p;

    public AFKListener(CashMoney CashMoney, Player p){
        this.cashMoney  = CashMoney;
        this.p = p;
    }

    @Override
    public void run() {
        if (!cashMoney.pi.containsKey(p)) {
            cashMoney.pi.put(p, 1);
        } else {
            int value = cashMoney.pi.get(p) + 1;
            cashMoney.pi.remove(p);
            cashMoney.pi.put(p, value);
        }
        if (cashMoney.pi.containsKey(p)) {
            int value = cashMoney.pi.get(p);
            if (value == cashMoney.timeout) {
                p.sendMessage(ChatColor.DARK_GREEN + "[iPwnAge]: You are afk; You are not recieving money for playing.");
            }
        }
    }
}
