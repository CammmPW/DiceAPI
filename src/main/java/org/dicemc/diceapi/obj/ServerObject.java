package org.dicemc.diceapi.obj;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dicemc.diceapi.DiceAPI;

import java.util.ArrayList;

public class ServerObject {
    public Plugin getPluginFromCommand(String name) {
        String alias = name.toLowerCase();
        PluginCommand cmd = Bukkit.getServer().getPluginCommand(alias);
        return cmd == null ? null : cmd.getPlugin();
    }

    public String getShortestAlias(JavaPlugin plugin, String name) {
        ArrayList<String> aliases = new ArrayList<>();
        if (pluginOwnsCommand(plugin, name))
            aliases.add(name);
        for (String s : plugin.getCommand(name).getAliases()) {
            if (pluginOwnsCommand(plugin, s))
                aliases.add(s);
        }
        if (aliases.size() == 0)
            return null;
        if (aliases.size() > 1)
            aliases.sort((s1, s2) -> {
                Integer l1 = s1.length();
                Integer l2 = s2.length();
                return l1.compareTo(l2);
            });
        return aliases.get(0);
    }

    public boolean pluginOwnsCommand(Plugin plugin, String alias) {
        return getPluginFromCommand(alias).getName().equalsIgnoreCase(plugin.getName());
    }
}
