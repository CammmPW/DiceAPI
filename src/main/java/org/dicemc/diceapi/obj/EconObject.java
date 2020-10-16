package org.dicemc.diceapi.obj;

import org.dicemc.diceapi.DiceAPI;
import java.text.DecimalFormat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconObject {
    private Economy econ;

    public EconObject() {
        RegisteredServiceProvider<Economy> economyProvider =
                DiceAPI.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            this.econ = (Economy)economyProvider.getProvider();
    }

    public double getBalance(String name) {
        return this.econ.getBalance(name);
    }

    public String getFormattedBalance(String name) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        double bal = getBalance(name);
        if (bal == 1.0D)
            return String.valueOf(formatter.format(bal)) + " " + currencyName();
        return String.valueOf(formatter.format(bal)) + " " + currencyNamePlural();
    }

    public String getEconomyName() {
        return this.econ.getName();
    }

    public String currencyName() {
        return this.econ.currencyNameSingular();
    }

    public String currencyNamePlural() {
        return this.econ.currencyNamePlural();
    }

    public void deposit(String name, double amount) {
        this.econ.depositPlayer(name, amount);
    }

    public void withdraw(String name, double amount) {
        this.econ.withdrawPlayer(name, amount);
    }

    public boolean pay(String sender, String receiver, double amount) {
        if (this.econ.has(sender, amount)) {
            withdraw(sender, amount);
            deposit(receiver, amount);
            return true;
        }
        return false;
    }

    public boolean canPay(String player, double amount) {
        return this.econ.has(player, amount);
    }
}
