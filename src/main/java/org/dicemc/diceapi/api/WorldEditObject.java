package org.dicemc.diceapi.api;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.World;
import org.dicemc.diceapi.DiceAPI;

import java.io.File;
import java.io.IOException;

public class WorldEditObject {
    private final WorldEditPlugin we;

    public WorldEditObject(WorldEditPlugin worldEdit) {
        this.we = worldEdit;
    }

    private LocalConfiguration getLocalConfig() {
        return this.we.getWorldEdit().getConfiguration();
    }

    private LocalSession getLocalSession() {
        return new LocalSession(getLocalConfig());
    }

    private BukkitWorld getLocalWorld(String worldName) {
        World world = DiceAPI.instance.getServer().getWorld(worldName);
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world);
    }

    private BukkitWorld getLocalWorld(World world) {
        return new BukkitWorld(world);
    }

    public BlockVector3 toVector(Location location){
        return BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void pasteSchematicAtVector(String world, String schematic, BlockVector3 location) throws IOException {
        File schematicFile = new File(schematic);
        BukkitWorld localWorld = getLocalWorld(world);
        if (localWorld == null) {
            // We got an issue here!
            return;
        }
        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicFile);
        if (clipboardFormat == null) {
            // We got an issue here!
            return;
        }
        clipboardFormat.load(schematicFile).paste(localWorld, location, true, false, null);
    }
}
