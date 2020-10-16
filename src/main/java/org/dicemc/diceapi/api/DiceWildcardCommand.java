package org.dicemc.diceapi.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dicemc.diceapi.DiceAPI;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class DiceWildcardCommand implements CommandExecutor {
    public final String cmdName;

    public final String arguments;

    public final String description;

    public final String permission;

    public DiceWildcardCommand(String commandName, String arguments, String description, String permission) {
        this.cmdName = commandName;
        this.arguments = arguments;
        this.description = description;
        this.permission = permission;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.permission == null || this.permission.equalsIgnoreCase("") || player.hasPermission(this.permission)) {
                if (args.length == 0) {
                    onPlayerCommand(player);
                } else {
                    String subcommand = args[0];
                    if (subcommand.equalsIgnoreCase("?") || subcommand.equalsIgnoreCase("help")) {
                        String command = "&b/" + this.cmdName + " &3" + Arrays.toString(args) + "&7: &b" + this.description;
                        DiceAPI.getChatAPI().sendPlayerMessageNoHeader(player, command);
                    } else {
                        args = DiceAPI.getChatUtils().removeFirstArg(args);
                        onPlayerCommand(player, subcommand, args);
                    }
                }
            } else {
                DiceAPI.getChatAPI().sendInvalidPermissionsMessage(player);
            }
        } else if (args.length == 0) {
            onConsoleCommand();
        } else {
            String subcommand = args[0];
            args = DiceAPI.getChatUtils().removeFirstArg(args);
            onConsoleCommand(subcommand, args);
        }
        return true;
    }

    public abstract void onConsoleCommand();

    public abstract void onConsoleCommand(String paramString, String[] paramArrayOfString);

    public abstract void onPlayerCommand(Player paramPlayer);

    public abstract void onPlayerCommand(Player paramPlayer, String paramString, String[] paramArrayOfString);

    public String getName() {
        return this.cmdName;
    }
}
