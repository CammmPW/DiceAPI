package org.dicemc.diceapi.obj;

import org.dicemc.diceapi.DiceAPI;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitCommandSender;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.schematic.SchematicFormat;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldEditObject {
    private WorldEditPlugin we;

    public WorldEditObject(WorldEditPlugin worldEdit) {
        this.we = worldEdit;
    }

    private LocalConfiguration getLocalConfig() {
        return this.we.getWorldEdit().getConfiguration();
    }

    private LocalSession getLocalSession() {
        return new LocalSession(getLocalConfig());
    }

    private LocalWorld getLocalWorld(String world) {
        return (LocalWorld)new BukkitWorld(DiceAPI.plugin.getServer().getWorld(world));
    }

    private LocalWorld getLocalWorld(World world) {
        return (LocalWorld)new BukkitWorld(world);
    }

    public Vector toVector(Location location) {
        return new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void pasteSchematic(String world, String schematic, Location location) {
        try {
            pasteSchematicAtVector(world, schematic, toVector(location));
        } catch (EmptyClipboardException e) {
            e.printStackTrace();
        } catch (DataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pasteSchematicAtVector(String world, String schematic, Vector location) throws DataException, IOException, EmptyClipboardException {
        LocalSession session = getLocalSession();
        LocalWorld localWorld = getLocalWorld(world);
        WorldVector worldVector = WorldVector.toBlockPoint(localWorld, location.getX(), location.getY(), location.getZ());
        File schema = new File(schematic);
        session.setClipboard(SchematicFormat.MCEDIT.load(schema));
        BlockBag blocks = null;
        EditSession es = new EditSession(localWorld, session.getBlockChangeLimit(), blocks);
        es.setFastMode(session.hasFastMode());
        es.setMask(session.getMask());
        try {
            session.getClipboard().paste(es, (Vector)worldVector, false);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

    public BaseBlock getBlock(String id, String world) throws WorldEditException {
        return getBlock(id, getLocalWorld(world));
    }

    public BaseBlock getBlock(String id, final LocalWorld world) throws WorldEditException {
        if (world == null)
            throw new IllegalArgumentException("world cannot be null");
        return this.we.getWorldEdit().getBlock((LocalPlayer)new BukkitCommandSender(this.we, this.we.getServerInterface(), (CommandSender)Bukkit.getConsoleSender()) {
            public LocalWorld getWorld() {
                return world;
            }
        }id, true);
    }

    public Selection getSelection(Player player) {
        return this.we.getSelection(player);
    }

    public void savePlayerClipboardAsSchematic(Player player, File f) throws EmptyClipboardException, IOException, DataException {
        if (f == null)
            throw new IllegalArgumentException("File cannot be null");
        if (f.exists())
            throw new IllegalArgumentException("File " + f.getAbsolutePath() + " already exists");
        SchematicFormat format = SchematicFormat.getFormat("mce");
        format.save(this.we.getSession(player).getClipboard(), f);
    }

    public void savePlayerSelectionAsSchematic(Player player, File f) throws IncompleteRegionException, IOException, DataException {
        LocalSession session = this.we.getSession(player);
        EditSession editSession = new EditSession(getLocalWorld(player.getWorld()), -1);
        Region region = session.getSelection(getLocalWorld(player.getWorld()));
        Vector min = region.getMinimumPoint();
        Vector max = region.getMaximumPoint();
        Vector pos = session.getPlacementPosition((LocalPlayer)this.we.wrapPlayer(player));
        CuboidClipboard clipboard = new CuboidClipboard(
                max.subtract(min).add(Vector.ONE),
                min, min.subtract(pos));
        clipboard.copy(editSession);
        SchematicFormat format = SchematicFormat.getFormat("mce");
        format.save(clipboard, f);
    }

    public int setAreaWithBlock(String world, Location minPoint, Location maxPoint, String blockId) {
        SingleBlockPattern singleBlockPattern;
        LocalSession session = getLocalSession();
        RegionSelector selector = session.getRegionSelector(getLocalWorld(world));
        selector.selectPrimary(toVector(minPoint));
        selector.selectSecondary(toVector(maxPoint));
        EditSession editSession = new EditSession(getLocalWorld(world), -1);
        Pattern pattern = null;
        try {
            singleBlockPattern = new SingleBlockPattern(getBlock(blockId, editSession.getWorld()));
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
        int affected = -1;
        if (singleBlockPattern instanceof SingleBlockPattern) {
            try {
                affected = editSession.setBlocks(selector.getRegion(), singleBlockPattern.getBlock());
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                affected = editSession.setBlocks(selector.getRegion(), (Pattern)singleBlockPattern);
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }
        }
        return affected;
    }
}
