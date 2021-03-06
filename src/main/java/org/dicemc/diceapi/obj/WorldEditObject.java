package org.dicemc.diceapi.obj;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.dicemc.diceapi.DiceAPI;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.bukkit.Bukkit.getLogger;

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

    @Nullable
    private BukkitWorld getLocalWorld(String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world);
    }

    private BukkitWorld getLocalWorld(World world) {
        return new BukkitWorld(world);
    }

    public BlockVector3 toVector(Location location) {
        return BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
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

    public void pasteSchematicAtVector(World world, String schematic, BlockVector3 vector) throws UnsupportedFormatException {
        File schema = new File(schematic);
        getLogger().info(schematic);

        ClipboardFormat format = ClipboardFormats.findByFile(schema);
        if (format == null) {
            throw new UnsupportedFormatException("This schematic format is not recognised or supported.");
        }
        try (Clipboard clipboard = format.load(schema)) {

            BukkitWorld adaptedWorld = getLocalWorld(world);
            EditSession editSession = new EditSessionBuilder(adaptedWorld).limitUnlimited().build();

            // Saves our operation and builds the paste - ready to be completed.
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(vector).ignoreAirBlocks(true).build();

            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushQueue();

            } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                getLogger().severe("OOPS! Something went wrong, please contact an administrator");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pasteSchematic(World world, String schematic, BlockVector3 vector) throws UnsupportedFormatException {
        pasteSchematicAtVector(world, schematic, vector);
    }

    public static class UnsupportedFormatException extends Exception {

        UnsupportedFormatException(String message) {
            super(message);
        }
    }
}