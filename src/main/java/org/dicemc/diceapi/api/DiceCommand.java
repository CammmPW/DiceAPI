package org.dicemc.diceapi.api;

import org.bukkit.entity.Player;

public abstract class DiceCommand implements Comparable<DiceCommand> {
    private final String[] aliases;

    private final String name;

    private final String args;

    private final String description;

    private final String permission;

    public DiceCommand(String name, String[] aliases, String args, String description, String permission) {
        this.name = name;
        this.aliases = aliases;
        this.args = args;
        this.description = description;
        this.permission = permission;
    }

    public int compareTo(DiceCommand command) {
        return getName().compareTo(command.getName());
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public String getName() {
        return this.name;
    }

    public String getArgs() {
        return this.args;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean hasAlias(String alias) {
        if (this.name.equalsIgnoreCase(alias))
            return true;
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = this.aliases).length, b = 0; b < i; ) {
            String s = arrayOfString[b];
            if (s.equalsIgnoreCase(alias))
                return true;
            b++;
        }
        return false;
    }

    public abstract void onPlayerCommand(Player paramPlayer, String[] paramArrayOfString);

    public abstract boolean onConsoleCommand(String[] paramArrayOfString);
}
