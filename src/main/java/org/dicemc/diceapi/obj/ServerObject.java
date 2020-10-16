package org.dicemc.diceapi.obj;

import org.dicemc.diceapi.DiceAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerObject {
    public Plugin getPluginFromCommand(String name) {
        String alias = name.toLowerCase();
        PluginCommand cmd = DiceAPI.instance.getServer().getPluginCommand(alias);
        if (cmd == null)
            return null;
        return cmd.getPlugin();
    }

    public String getShortestAlias(JavaPlugin plugin, String name) {
        ArrayList<String> aliases = new ArrayList<>();
        if (pluginOwnsCommand((Plugin)plugin, name))
            aliases.add(name);
        for (String s : plugin.getCommand(name).getAliases()) {
            if (pluginOwnsCommand((Plugin)plugin, s))
                aliases.add(s);
        }
        if (aliases.size() == 0)
            return null;
        if (aliases.size() > 1)
            Collections.sort(aliases, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    Integer l1 = Integer.valueOf(s1.length());
                    Integer l2 = Integer.valueOf(s2.length());
                    return l1.compareTo(l2);
                }
            });
        return aliases.get(0);
    }

    public boolean pluginOwnsCommand(Plugin plugin, String alias) {
        return getPluginFromCommand(alias).getName().equalsIgnoreCase(plugin.getName());
    }
}
