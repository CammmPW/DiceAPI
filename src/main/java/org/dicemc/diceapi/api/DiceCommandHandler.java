package org.dicemc.diceapi.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dicemc.diceapi.DiceAPI;
import org.dicemc.diceapi.util.PageBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public abstract class DiceCommandHandler implements CommandExecutor {
    private final String cmdName;

    private final String description;

    private final String permission;

    private final ArrayList<DiceCommand> commands;

    public DiceCommandHandler(String cmdName, String description, String permission) {
        this.commands = new ArrayList<>();
        this.cmdName = cmdName;
        this.description = description;
        this.permission = permission;
        registerCommands();
    }

    protected abstract void registerCommands();

    protected void showHelp(Player player, int page) {
        Collections.sort(this.commands);
        PageBuilder help = new PageBuilder("&b" + DiceAPI.getChatUtils().toProperCase(this.cmdName) + " Help", "&3");
        help.append("&b/" + this.cmdName + "&7: &b" + this.description);
        help.append("&b/" + this.cmdName + " &3<help/?> [page]&7: &bShows help");
        for (DiceCommand tc : this.commands) {
            if (DiceAPI.getPermAPI().hasPermission(player, tc.getPermission()))
                help.append("&b/" + this.cmdName + " &3" + tc.getName() + " " + tc.getArgs() + "&7: &b" + tc.getDescription());
        }
        help.showPage(player, page);
    }

    protected void showHelp(CommandSender sender, int page) {
        if (sender instanceof Player) {
            showHelp((Player)sender, page);
            return;
        }
        Collections.sort(this.commands);
        PageBuilder help = new PageBuilder("&b" + DiceAPI.getChatUtils().toProperCase(this.cmdName) + " Help", "&3");
        help.append("&b/" + this.cmdName + "&7: &b" + this.description);
        help.append("&b/" + this.cmdName + " &3<help/?> [page]&7: &bShows help");
        for (DiceCommand tc : this.commands) {
            String delimiter = "&b/&3";
            String aliases = ((tc.getAliases()).length > 1) ? DiceAPI.getChatUtils().join(tc.getAliases(), delimiter) : "";
            help.append("&b/" + this.cmdName + " &3" + tc.getName() + aliases + " " + tc.getArgs() + "&7: &b" + tc.getDescription());
        }
        help.showPage(sender, page);
    }

    protected void registerCommand(DiceCommand command) {
        this.commands.add(command);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (DiceAPI.getPermAPI().hasPermission((Player)sender, this.permission)) {
                    onPlayerCommand((Player)sender);
                } else {
                    DiceAPI.getChatAPI().sendInvalidPermissionsMessage((Player)sender);
                }
            } else if (!onConsoleCommand()) {
                DiceAPI.getChatAPI().outNoHeader("&cThis command must be run by a player");
            }
        } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            if (args.length == 1) {
                showHelp(sender, 1);
            } else {
                try {
                    int page = Integer.parseInt(args[1]);
                    showHelp(sender, page);
                } catch (NumberFormatException e) {
                    showHelp(sender, 1);
                }
            }
        } else {
            runCommand(sender, args);
        }
        return true;
    }

    private void runCommand(CommandSender sender, String[] args) {
        String subcommand = args[0];
        DiceCommand tc = getCommandByAlias(subcommand);
        args = DiceAPI.getChatUtils().removeFirstArg(args);
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (tc != null) {
                if (DiceAPI.getPermAPI().hasPermission(player, tc.getPermission())) {
                    tc.onPlayerCommand(player, args);
                } else {
                    DiceAPI.getChatAPI().sendInvalidPermissionsMessage(player);
                }
            } else {
                DiceAPI.getChatAPI().sendInvalidSubCommandMessage((Player)sender, subcommand);
            }
        } else if (tc != null) {
            if (!tc.onConsoleCommand(args))
                DiceAPI.getChatAPI().outNoHeader("&cThis command must be run by a player");
        } else {
            DiceAPI.getChatAPI().sendInvalidSubCommandMessage(sender, subcommand);
        }
    }

    public String getName() {
        return this.cmdName;
    }

    protected abstract boolean onConsoleCommand();

    protected abstract void onPlayerCommand(Player paramPlayer);

    public DiceCommand getCommandByAlias(String alias) {
        for (DiceCommand tc : this.commands) {
            if (tc.hasAlias(alias))
                return tc;
        }
        return null;
    }

    public boolean onNumberGiven(int num) {
        return false;
    }

    public boolean onNumberGiven(Player player, int num) {
        return false;
    }
}
