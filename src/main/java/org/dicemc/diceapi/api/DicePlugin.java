package org.dicemc.diceapi.api;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dicemc.diceapi.api.metrics.Metrics;
import org.dicemc.diceapi.obj.ChatObject;

import java.util.ArrayList;

public abstract class DicePlugin extends JavaPlugin {
    private ArrayList<Integer> tasks = new ArrayList<>();

    public ChatObject chat;

    private long timeStart;

    private long timeEnd;

    public void onEnable() {
        this.timeStart = System.currentTimeMillis();
        this.chat = new ChatObject(getDescription().getName());
        onStart();
        this.timeEnd = System.currentTimeMillis();
        this.chat.out("Enabled - " + timeTakenToEnable() + "s");
    }

    public void onDisable() {
        cancelAllTasks();
        onStop();
    }

    public abstract void onStart();

    public abstract void onStop();

    public void startMetrics() {
        Metrics metrics = new Metrics((Plugin)this);
        metrics.isEnabled();
    }

    public double timeTakenToEnable() {
        return (this.timeEnd - this.timeStart) / 1000.0D;
    }

    public void registerCommand(DiceCommandHandler handler) {
        getCommand(handler.getName()).setExecutor(handler);
    }

    public void registerEvents(Listener l) {
        getServer().getPluginManager().registerEvents(l, (Plugin)this);
    }

    public int registerDelayedTask(Runnable task, long delay) {
        int taskId = getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this, task, delay);
        this.tasks.add(Integer.valueOf(taskId));
        return taskId;
    }

    public int registerRepeatingTask(Runnable task, long delay, long interval) {
        int taskId = getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, task, delay, interval);
        this.tasks.add(Integer.valueOf(taskId));
        return taskId;
    }

    public void cancelTask(int task) {
        getServer().getScheduler().cancelTask(task);
        this.tasks.remove(task);
    }

    public void cancelAllTasks() {
        this.tasks.clear();
        getServer().getScheduler().cancelTasks((Plugin)this);
    }
}
