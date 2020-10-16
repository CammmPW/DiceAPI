package org.dicemc.diceapi.obj;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class EffectObject {
    private final Random random = new Random();

    public void applyPotionEffect(Player player, PotionEffectType effectType, int duration, int strength) {
        player.addPotionEffect(new PotionEffect(effectType, duration * 20, strength));
    }

    public void createExplosion(Location location, float force) {
        location.getWorld().createExplosion(location, force);
    }

    public void killEntity(LivingEntity entity) {
        entity.setHealth(0.0D);
    }

    public void igniteEntity(Entity entity, int duration) {
        entity.setFireTicks(duration);
    }

    public void removePotionEffect(Player player, PotionEffectType effectType) {
        player.removePotionEffect(effectType);
    }

    public void strike(Location location) {
        location.getWorld().strikeLightning(location);
    }

    public void strikeEffect(Location location) {
        location.getWorld().strikeLightningEffect(location);
    }

    public void showSmoke(Location location) {
        showSmoke(location, 20);
    }

    public void showSmoke(Location location, int iterations) {
        for (int i = 0; i < iterations; i++)
            location.getWorld().playEffect(location, Effect.SMOKE, random.nextInt(9));
    }
}
