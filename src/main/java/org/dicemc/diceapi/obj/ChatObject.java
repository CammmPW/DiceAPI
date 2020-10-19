package org.dicemc.diceapi.obj;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.dicemc.diceapi.DiceAPI;
import org.dicemc.diceapi.api.DiceMessage;
import org.dicemc.diceapi.api.messages.InvalidPermissionsMessage;
import org.dicemc.diceapi.api.messages.InvalidSubCommandMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatObject {
    private final String name;

    private ConsoleCommandSender console;

    public ChatObject(String name) {
        this.name = name;
        this.console = Bukkit.getServer().getConsoleSender();
    }

    public void sendInvalidPermissionsMessage(Player player) {
        sendPlayerMessageNoHeader(player, new InvalidPermissionsMessage());
    }

    public void sendInvalidSubCommandMessage(Player player, String attempt) {
        sendPlayerMessageNoHeader(player, new InvalidSubCommandMessage(attempt));
    }

    public void sendInvalidSubCommandMessage(CommandSender sender, String attempt) {
        if (sender instanceof Player) {
            sendInvalidSubCommandMessage((Player) sender, attempt);
            return;
        }
        outNoHeader(new InvalidSubCommandMessage(attempt));
    }

    public void sendPlayerMessage(Player player, String message) {
        message = DiceAPI.getChatUtils().formatMessage("&7[&9" + this.name + "&7]&f " + message);
        player.sendMessage(message);
    }

    public void sendPlayerMessageNoHeader(Player player, String message) {
        message = DiceAPI.getChatUtils().formatMessage(message);
        player.sendMessage(message);
    }

    public void sendPlayerMessage(Player player, DiceMessage message) {
        String msg = DiceAPI.getChatUtils().formatMessage("&7[&9" + this.name + "&7]&f " + message);
        player.sendMessage(msg);
    }

    public void sendPlayerMessageNoHeader(Player player, DiceMessage message) {
        String msg = DiceAPI.getChatUtils().formatMessage(message.getMessage());
        player.sendMessage(msg);
    }

    public void sendGlobalMessage(String message) {
        message = DiceAPI.getChatUtils().formatMessage("&7[&9" + this.name + "&7]&f " + message);
        Bukkit.getServer().broadcastMessage(message);
    }

    public void sendGlobalMessageNoHeader(String message) {
        message = DiceAPI.getChatUtils().formatMessage(message);
        Bukkit.getServer().broadcastMessage(message);
    }

    public void sendGlobalMessage(DiceMessage message) {
        String msg = DiceAPI.getChatUtils().formatMessage("&7[&9" + this.name + "&7]&f " + message);
        Bukkit.getServer().broadcastMessage(msg);
    }

    public void sendGlobalMessageNoHeader(DiceMessage message) {
        Bukkit.getServer().broadcastMessage(message.getMessage());
    }

    public void out(String message) {
        outNoHeader("[" + this.name + "] " + message);
    }

    public void outNoHeader(String message) {
        outNoHeader(message, Level.INFO);
    }

    public void outNoHeader(String message, Level logLevel) {
        if (this.console == null)
            this.console = Bukkit.getServer().getConsoleSender();
        if (this.console != null) {
            message = DiceAPI.getChatUtils().formatMessage(message);
            this.console.sendMessage(message);
        } else {
            message = DiceAPI.getChatUtils().removeColorCodes(message);
            Logger logger = Logger.getLogger("Minecraft");
            logger.log(logLevel, message);
        }
    }

    public void outSevere(String message) {
        outNoHeader("&4[" + this.name + "] &r" + message, Level.SEVERE);
    }

    public void outWarn(String message) {
        outNoHeader("&e[" + this.name + "] &r" + message, Level.WARNING);
    }

    public void out(DiceMessage message) {
        outNoHeader(message);
    }

    public void outNoHeader(DiceMessage message) {
        outNoHeader(message.getMessage());
    }

    public void outSevere(DiceMessage message) {
        outSevere(message.getMessage());
    }

    public void outWarn(DiceMessage message) {
        outWarn(message.getMessage());
    }
}
