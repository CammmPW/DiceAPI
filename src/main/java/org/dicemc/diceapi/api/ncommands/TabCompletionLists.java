package org.dicemc.diceapi.api.ncommands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class TabCompletionLists {
    private static final ArrayList<String> _entityNames = new ArrayList<>();

    private static final ArrayList<String> _mobNames = new ArrayList<>();

    private static final ArrayList<String> _materialNames = new ArrayList<>();

    private static ArrayList<String> _playerNames;

    static {
        byte b;
        int i;
        EntityType[] arrayOfEntityType;
        for (i = (arrayOfEntityType = EntityType.values()).length, b = 0; b < i; ) {
            EntityType e = arrayOfEntityType[b];
            String name = e.toString();
            _entityNames.add(name);
            if (e.isAlive())
                _mobNames.add(e.toString());
            b++;
        }
        sort(_entityNames);
        sort(_mobNames);
        loopAddSort(Material.values(), _materialNames);
    }

    public static ArrayList<String> entities() {
        return _entityNames;
    }

    public static ArrayList<String> mobs() {
        return _mobNames;
    }

    public static ArrayList<String> materials() {
        return _materialNames;
    }

    public static ArrayList<String> players() {
        _playerNames = new ArrayList<>();
        byte b;
        int i;
        Player[] arrayOfPlayer;
        for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
            Player p = arrayOfPlayer[b];
            _playerNames.add(p.getName());
            b++;
        }
        sort(_playerNames);
        return _playerNames;
    }

    private static <E> void loopAddSort(Object[] array, ArrayList<String> list) {
        byte b;
        int i;
        Object[] arrayOfObject;
        for (i = (arrayOfObject = array).length, b = 0; b < i; ) {
            E e = (E) arrayOfObject[b];
            list.add(e.toString());
            sort(list);
            b++;
        }
    }

    public static void sort(ArrayList<String> list) {
        list.sort(Comparator.naturalOrder());
    }
}
