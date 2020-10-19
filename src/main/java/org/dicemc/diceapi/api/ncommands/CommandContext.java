package org.dicemc.diceapi.api.ncommands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dicemc.diceapi.DiceAPI;
import org.dicemc.diceapi.util.ChatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class CommandContext {
    protected final CommandSender _sender;

    protected final String[] _args;

    protected final String _sub;

    protected static HashMap<String, String> _customMessages;

    public CommandContext(CommandSender sender, String sub, String[] args) {
        this._sender = sender;
        this._sub = sub;
        this._args = args;
        _customMessages = new HashMap<>();
    }

    public static void addMessage(String identifier, String message) {
        _customMessages.put(identifier, message);
    }

    public String getSub() {
        return this._sub;
    }

    @Nullable
    public Player getPlayer() {
        if (senderIsPlayer())
            return (Player)this._sender;
        return null;
    }

    public Player getPlayer(int index) {
        return Bukkit.getPlayer(this._args[index]);
    }

    public CommandSender getSender() {
        return this._sender;
    }

    public Material getMaterial(int index, Material def) {
        if (index >= this._args.length)
            return def;
        String s = this._args[index];
        try {
            int id = Integer.parseInt(s);
            return Material.getMaterial(String.valueOf(id));
        } catch (NumberFormatException e) {
            return Material.getMaterial(s.toUpperCase());
        }
    }

    public boolean senderIsConsole() {
        return !senderIsPlayer();
    }

    public boolean senderIsPlayer() {
        return this._sender instanceof Player;
    }

    public String[] getArgs() {
        return this._args;
    }

    public String[] getArgs(int beginIndex) {
        return DiceAPI.getChatUtils().removeArgs(this._args, beginIndex);
    }

    public boolean eic(int index, String... otherStrings) {
        if (index > this._args.length)
            return false;
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = otherStrings).length, b = 0; b < i; ) {
            String s = arrayOfString[b];
            if (s.equalsIgnoreCase(this._args[index]))
                return true;
            b++;
        }
        return false;
    }

    public void sendMessageToSender(String message) {
        message = (new ChatUtils()).formatMessage(message);
        this._sender.sendMessage(message);
    }

    public static void sendMessageToSender(String identifier, Object... args) {
        if (!_customMessages.containsKey(identifier))
            throw new IllegalArgumentException("Identifier \"" + identifier + "\" not found");
        String.format(_customMessages.get(identifier), args);
    }

    public void notInteger(int index) {
        sendMessageToSender("&e" + this._args[index] + " &cis not an integer");
    }

    public String getString(int index) {
        return this._args[index];
    }

    public String getString(int index, String def) {
        return (index < this._args.length) ? this._args[index] : def;
    }

    public String getJoinedArgs() {
        return getJoinedArgs(0);
    }

    public String getJoinedArgs(int initialIndex) {
        return getJoinedArgs(initialIndex, " ");
    }

    public String getJoinedArgs(int initialIndex, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = initialIndex; i < this._args.length; i++)
            result.append(this._args[i]).append(delimiter);
        return result.toString().trim();
    }

    public int getInteger(int index) throws NumberFormatException {
        return Integer.parseInt(this._args[index]);
    }

    public int getInteger(int index, int def) throws NumberFormatException {
        return (index < this._args.length) ? Integer.parseInt(this._args[index]) : def;
    }

    public double getDouble(int index) throws NumberFormatException {
        return Double.parseDouble(this._args[index]);
    }

    public double getDouble(int index, double def) throws NumberFormatException {
        return (index < this._args.length) ? Double.parseDouble(this._args[index]) : def;
    }

    public int length() {
        return this._args.length;
    }

    public boolean gt(int length) {
        return (length() > length);
    }

    public boolean lt(int length) {
        return (length() < length());
    }

    public boolean eq(int length) {
        return (length() == length);
    }

    public boolean lteq(int length) {
        return (length() <= length);
    }

    public boolean gteq(int length) {
        return (length() >= length);
    }

    public String getPlayerName() {
        if (senderIsPlayer())
            return getPlayer().getName();
        return "CONSOLE";
    }

    public void invokeCommand(String command) {
        this._sender.getServer().dispatchCommand(this._sender, command);
    }

    public boolean senderHasPermission(String perm) {
        if (this._sender instanceof Player)
            return DiceAPI.getPermAPI().hasPermission((Player)this._sender, perm);
        return true;
    }
}
