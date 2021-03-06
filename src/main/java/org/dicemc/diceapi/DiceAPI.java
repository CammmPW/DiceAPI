package org.dicemc.diceapi;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.plugin.Plugin;
import org.dicemc.diceapi.api.DicePlugin;
import org.dicemc.diceapi.api.ncommands.CommandManager;
import org.dicemc.diceapi.api.serialization.SerializationConfig;
import org.dicemc.diceapi.commands.DevCommands;
import org.dicemc.diceapi.database.Database;
import org.dicemc.diceapi.listener.PlayerListener;
import org.dicemc.diceapi.obj.*;
import org.dicemc.diceapi.util.ChatUtils;
import org.dicemc.diceapi.util.ItemUtils;
import org.dicemc.diceapi.util.TimeUtils;

import java.io.File;
import java.sql.SQLException;

public class DiceAPI extends DicePlugin {
    public static DiceAPI instance;

    public static DiceAPIConfig config;

    public static File playerData;

    private static ChatObject chatAPI;

    private static ChatUtils chatUtils;

    private static EconObject econAPI;

    private static EffectObject effectAPI;

    private static ItemUtils itemUtils;

    private static Database mysqlAPI;

    private static PermObject permAPI;

    private static PlayerObject playerAPI;

    private static ServerObject serverAPI;

    private static TimeUtils timeUtils;

    private static WorldEditObject weAPI;

    private static CommandManager commands;

    public static ChatObject getChatAPI() {
        return chatAPI;
    }

    public static ChatUtils getChatUtils() {
        return chatUtils;
    }

    public static Database getDB() {
        return mysqlAPI;
    }

    public static EconObject getEconAPI() {
        return econAPI;
    }

    public static EffectObject getEffectAPI() {
        return effectAPI;
    }

    public static ItemUtils getItemUtils() {
        return itemUtils;
    }

    public static PermObject getPermAPI() {
        return permAPI;
    }

    public static PlayerObject getPlayerAPI() {
        return playerAPI;
    }

    public static TimeUtils getTimeUtils() {
        return timeUtils;
    }

    public static ServerObject getServerAPI() {
        return serverAPI;
    }

    public static WorldEditObject getWorldEditAPI() {
        return weAPI;
    }

    public static boolean isEconAPIOnline() {
        return (econAPI != null);
    }

    public static boolean isWorldEditAPIOnline() {
        return (weAPI != null);
    }

    public void onStart() {
        instance = this;
        config = new DiceAPIConfig(new File(getDataFolder() + "/config.yml"));
        playerData = new File(getDataFolder() + "/playerData");
        if (!playerData.exists())
            playerData.mkdir();
        chatUtils = new ChatUtils();
        this.chat.out("[Utils] [Chat] Helper online");
        itemUtils = new ItemUtils();
        this.chat.out("[Utils] [Item] Helper online");
        timeUtils = new TimeUtils();
        this.chat.out("[Utils] [Time] Helper online");
        chatAPI = this.chat;
        this.chat.out("[Chat] API online");
        try {
            econAPI = new EconObject();
            this.chat.out("[Economy] API online. Using " + econAPI.getEconomyName() + " for EconAPI");
        } catch (NoClassDefFoundError e) {
            econAPI = null;
            this.chat.outWarn("[Economy] API not loaded");
        }
        effectAPI = new EffectObject();
        this.chat.out("[Effect] API Online");
        try {
            mysqlAPI = new Database();
            this.chat.out("[MySQL] API online");
        } catch (SQLException e) {
            mysqlAPI = null;
            getChatAPI().outWarn("[MySQL] Could not hook into MySQL. Did you update the config.yml?");
        }
        permAPI = new PermObject();
        this.chat.out("[Permissions] API Online");
        playerAPI = new PlayerObject();
        this.chat.out("[Player] API Online");
        serverAPI = new ServerObject();
        this.chat.out("[Server] API Online");
        initWorldEdit();
        SerializationConfig.reload();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        commands = new CommandManager(this);
        commands.reg(DevCommands.class);

        if (weAPI == null) {
            this.chat.out("WorldEdit has not been initialized.");
        } else {
            this.chat.out("WorldEdit has been initialized.");
        }
    }

    public void onStop() {
        this.chat.out("Disabled");
    }

    private void initWorldEdit() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null) {
            this.chat.outWarn("[WorldEdit] API not loaded");
        } else {
            weAPI = new WorldEditObject((WorldEditPlugin) plugin);
            this.chat.out("[WorldEdit] API online");
        }
    }
}
