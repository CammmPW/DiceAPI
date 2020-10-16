package org.dicemc.diceapi.api;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class WorldEditObject {
    private WorldEditPlugin we;

    public WorldEditObject(WorldEditPlugin worldEdit) {
        this.we = worldEdit;
    }

    private LocalConfiguration getLocalConfig() {
        return this.we.getWorldEdit().getConfiguration();
    }
}
