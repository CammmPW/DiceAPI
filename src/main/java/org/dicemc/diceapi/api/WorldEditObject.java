package org.dicemc.diceapi.api;

import com.boydti.fawe.Fawe;
import com.boydti.fawe.object.clipboard.SimpleClipboard;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.command.SchematicCommands;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.dicemc.diceapi.DiceAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public void pasteSchematic(String world, String schematic, Location location) {
        try {
            pasteSchematicAtVector(world, schematic, toVector(location));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void savePlayerClipboardAsSchematic(Player player, File file) {
        LocalSession session = we.getSession(player);
        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream((file)))) {
            // For some reason clipboard is deprecated while it's still being used by the internals of FAWE.
            writer.write(session.getClipboard().getClipboard());
        } catch (EmptyClipboardException ex) {
            // Notify the player about empty clipboard
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void savePlayerSelectionAsSchematic(Player player, File file) {
        LocalSession session = we.getSession(player);
        Actor actor = we.wrapPlayer(player);
        Region region = session.getSelection(session.getSelectionWorld());
        Clipboard clipboard = new BlockArrayClipboard(region, actor.getUniqueId());
        clipboard.setOrigin(session.getPlacementPosition(actor));
        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream((file)))) {
            // For some reason clipboard is deprecated while it's still being used by the internals of FAWE.
            writer.write(session.getClipboard().getClipboard());
        } catch (EmptyClipboardException ex) {
            // Notify the player about empty clipboard
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}