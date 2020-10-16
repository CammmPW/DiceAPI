package org.dicemc.diceapi.obj;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.text.DecimalFormat;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconObject {
    private Economy econ;

    public EconObject() {
        RegisteredServiceProvider<Economy> economyProvider =
                Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            this.econ = economyProvider.getProvider();
    }

    public double getBalance(String name) {
        return this.econ.getBalance(name);
    }

    public double getBalance(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        return this.econ.getBalance(offlinePlayer);
    }

    public String getFormattedBalance(String name) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        double bal = getBalance(name);
        if (bal == 1.0D)
            return formatter.format(bal) + " " + currencyName();
        return formatter.format(bal) + " " + currencyNamePlural();
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

    public void deposit(UUID uuid, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.econ.depositPlayer(offlinePlayer, amount);
    }

    public void withdraw(UUID uuid, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.econ.withdrawPlayer(offlinePlayer, amount);
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

    public boolean pay(UUID sender, UUID receiver, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sender);
        if (this.econ.has(offlinePlayer, amount)) {
            withdraw(sender, amount);
            deposit(receiver, amount);
            return true;
        }
        return false;
    }

    public boolean canPay(UUID uuid, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        return this.econ.has(offlinePlayer, amount);
    }
}
