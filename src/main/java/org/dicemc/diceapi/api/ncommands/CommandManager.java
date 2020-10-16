package org.dicemc.diceapi.api.ncommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.dicemc.diceapi.DiceAPI;
import org.dicemc.diceapi.api.DicePermissionHandler;
import org.dicemc.diceapi.util.ChatUtils;
import org.dicemc.diceapi.util.PageBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {
    private JavaPlugin _plugin;

    private HashMap<Method, String> _methods = new HashMap<>();

    private ArrayList<String> _registeredCommands = new ArrayList<>();

    private ArrayList<DicePermissionHandler> _permHandlers = new ArrayList<>();

    private HashMap<String, PageBuilder> _help = new HashMap<>();

    private ArrayList<TabCompleteHook> _tcHooks;

    public CommandManager(JavaPlugin plugin) {
        this._plugin = plugin;
    }

    public void addPermissionHandler(DicePermissionHandler handler) {
        this._permHandlers.add(handler);
    }

    protected boolean handlePerm(Player player, String perm) {
        if (player == null || perm.isEmpty() || perm == null)
            return true;
        for (DicePermissionHandler h : this._permHandlers) {
            String prefix = String.valueOf(h.getPrefix()) + ":";
            if (perm.startsWith(prefix) && perm.length() > prefix.length())
                return h.handlePerm(player, perm.substring(prefix.length()));
        }
        return DiceAPI.getPermAPI().hasPermission(player, perm);
    }

    public void reg(Class<?> clazz) {
        byte b;
        int i;
        Method[] arrayOfMethod;
        for (i = (arrayOfMethod = clazz.getDeclaredMethods()).length, b = 0; b < i; ) {
            Method m = arrayOfMethod[b];
            if (Modifier.isStatic(m.getModifiers())) {
                boolean parentPresent = m.isAnnotationPresent((Class)ParentCommand.class);
                boolean commandPresent = m.isAnnotationPresent((Class)Command.class);
                boolean simplePresent = m.isAnnotationPresent((Class)SimpleCommand.class);
                if (simplePresent || (parentPresent && commandPresent)) {
                    String command = simplePresent ? ((SimpleCommand)m.<SimpleCommand>getAnnotation(SimpleCommand.class)).name() : ((ParentCommand)m.<ParentCommand>getAnnotation(ParentCommand.class)).value();
                    if (!commandRegistered(command))
                        this._plugin.getCommand(command).setExecutor(this);
                    this._methods.put(m, command);
                }
            }
            b++;
        }
    }

    public boolean commandRegistered(String name) {
        for (String s : this._registeredCommands) {
            if (s.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    protected ArrayList<Method> getMethodsFor(String parent) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method m : this._methods.keySet()) {
            String test;
            boolean simple = m.isAnnotationPresent((Class)SimpleCommand.class);
            if (simple) {
                test = ((SimpleCommand)m.<SimpleCommand>getAnnotation(SimpleCommand.class)).name();
            } else {
                test = ((ParentCommand)m.<ParentCommand>getAnnotation(ParentCommand.class)).value();
            }
            if (test.equalsIgnoreCase(parent)) {
                methods.add(m);
                if (simple)
                    return methods;
            }
        }
        Collections.sort(methods, new Comparator<Method>() {
            public int compare(Method m1, Method m2) {
                String name1 = ((Command)m1.<Command>getAnnotation(Command.class)).name();
                String name2 = ((Command)m2.<Command>getAnnotation(Command.class)).name();
                return name1.compareTo(name2);
            }
        });
        return methods;
    }

    private boolean hasAlias(String alias, String name, String[] aliases) {
        if (alias.equalsIgnoreCase(name))
            return true;
        if (name.equalsIgnoreCase("#") || name.equalsIgnoreCase("[#]") || name.equalsIgnoreCase("<#>"))
            try {
                Integer.parseInt(alias);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        if (aliases == null)
            return false;
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = aliases).length, b = 0; b < i; ) {
            String s = arrayOfString[b];
            if (s.equalsIgnoreCase(alias))
                return true;
            b++;
        }
        return false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player)
            player = (Player)sender;
        String command = (args.length > 0) ? args[0] : "";
        String[] newArgs = removeFirstArg(args);
        for (Method m : getMethodsFor(cmd.getName())) {
            boolean onlyConsole, onlyPlayer;
            String name;
            boolean simple = m.isAnnotationPresent((Class)SimpleCommand.class);
            String[] aliases = null;
            if (simple) {
                SimpleCommand sc = m.<SimpleCommand>getAnnotation(SimpleCommand.class);
                onlyConsole = sc.onlyConsole();
                onlyPlayer = sc.onlyPlayer();
                name = sc.name();
            } else {
                Command c = m.<Command>getAnnotation(Command.class);
                onlyConsole = c.onlyConsole();
                onlyPlayer = c.onlyPlayer();
                name = c.name();
                aliases = c.aliases();
            }
            boolean hasAlias = simple ? name.equalsIgnoreCase(cmd.getName()) : hasAlias(command, name, aliases);
            if (hasAlias) {
                if (onlyPlayer && player == null) {
                    String msg = (new ChatUtils()).formatMessage("&cThis command must be run by a player");
                    sender.sendMessage(msg);
                    return true;
                }
                if (onlyConsole && player != null) {
                    String msg = (new ChatUtils()).formatMessage("&cThis command must be run by console");
                    player.sendMessage(msg);
                    return true;
                }
                try {
                    if (m.isAnnotationPresent((Class)CommandPermission.class) &&
                            !handlePerm(player, ((CommandPermission)m.<CommandPermission>getAnnotation(CommandPermission.class)).value())) {
                        String msg = (new ChatUtils()).formatMessage("&cYou don't have permission");
                        player.sendMessage(msg);
                        return true;
                    }
                    CommandContext cargs = simple ? new CommandContext(sender, cmd.getName(), args) : new CommandContext(sender, command, newArgs);
                    m.invoke((Object)null, new Object[] { cargs });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        if (command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help")) {
            int page = 1;
            if (newArgs.length > 0)
                try {
                    page = Integer.parseInt(newArgs[0]);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            showHelp(cmd.getName(), sender, page);
        } else {
            String msg = (new ChatUtils()).formatMessage("&cCommand \"&e/" + label + " " + command + "&c\" not found");
            sender.sendMessage(msg);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        ArrayList<String> possible = new ArrayList<>();
        String parent = cmd.getName();
        ArrayList<Method> methods = getMethodsFor(parent);
        if (args.length == 1) {
            for (Method m : methods) {
                if (m.isAnnotationPresent((Class)SimpleCommand.class))
                    continue;
                Command c = m.<Command>getAnnotation(Command.class);
                String name = c.name();
                String[] aliases = c.aliases();
                if (name.startsWith(alias))
                    possible.add(name);
                byte b;
                int i;
                String[] arrayOfString1;
                for (i = (arrayOfString1 = aliases).length, b = 0; b < i; ) {
                    String s = arrayOfString1[b];
                    if (s.startsWith(alias))
                        possible.add(s);
                    b++;
                }
            }
            TabCompletionLists.sort(possible);
            return possible;
        }
        return new ArrayList<>();
    }

    public void showHelp(String label, CommandSender sender, int page) {
        PageBuilder pages = this._help.get(label);
        if (pages == null) {
            pages = new PageBuilder("&b/" + label + " Help", "&3");
            ArrayList<Method> methods = getMethodsFor(label);
            String helpLine = "&b/" + label + " &3help&b/&3? [page]&7: &bShows help";
            pages.append(helpLine);
            for (Method m : methods) {
                Command c = m.<Command>getAnnotation(Command.class);
                if (c.invisible())
                    continue;
                if (m.isAnnotationPresent((Class)CommandPermission.class) &&
                        sender instanceof Player &&
                        !handlePerm((Player)sender, ((CommandPermission)m.<CommandPermission>getAnnotation(CommandPermission.class)).value()))
                    continue;
                String aliasString = getAliasString(c.name(), c.aliases());
                String line = "&b/" + label;
                if (!aliasString.isEmpty())
                    line = String.valueOf(line) + " &3" + aliasString;
                if (!c.args().isEmpty())
                    line = String.valueOf(line) + " &3" + c.args();
                line = String.valueOf(line) + "&7: &b" + c.desc();
                pages.append(line);
                if (c.name().isEmpty() && !hasCommand(methods, "help")) {
                    pages.remove(helpLine);
                    pages.append(helpLine);
                }
            }
            this._help.put(label, pages);
        }
        if (!pages.hasPage(page))
            page = 1;
        pages.showPage(sender, page);
    }

    private boolean hasCommand(ArrayList<Method> methods, String command) {
        for (Method m : methods) {
            Command c = m.<Command>getAnnotation(Command.class);
            if (hasAlias(command, c.name(), c.aliases()))
                return true;
        }
        return false;
    }

    private String getAliasString(String name, String[] aliases) {
        String str = "&3" + name;
        if (aliases == null || aliases.length == 0)
            return str;
        for (int i = 0; i < aliases.length; i++) {
            str = String.valueOf(str) + "&b/&3" + aliases[i];
            if (i < aliases.length - 1)
                str = String.valueOf(str) + "&b/&3";
        }
        return str;
    }

    private String[] removeArgs(String[] array, int startIndex) {
        if (array.length == 0)
            return array;
        if (array.length < startIndex)
            return new String[0];
        String[] newSplit = new String[array.length - startIndex];
        System.arraycopy(array, startIndex, newSplit, 0, array.length - startIndex);
        return newSplit;
    }

    private String[] removeFirstArg(String[] array) {
        return removeArgs(array, 1);
    }

    public int commandsRegistered() {
        return this._methods.size();
    }

    public void addTabCompleteHook(String parent, String sub, int index, List<String> list) {
        this._tcHooks.add(new TabCompleteHook(parent, sub, index, list));
    }

    class TabCompleteHook {
        private String _parent;

        private String _sub;

        private int _index;

        private List<String> _list;

        public TabCompleteHook(String parent, String sub, int index, List<String> list) {
            this._parent = parent;
            this._sub = sub;
            this._index = index;
            this._list = list;
        }

        public String getParent() {
            return this._parent;
        }

        public String getSub() {
            return this._sub;
        }

        public int getArgIndex() {
            return this._index;
        }

        public List<String> getList() {
            return this._list;
        }
    }
}
