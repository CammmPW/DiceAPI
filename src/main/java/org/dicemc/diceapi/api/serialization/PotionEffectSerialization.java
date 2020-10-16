package org.dicemc.diceapi.api.serialization;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;

public class PotionEffectSerialization {
    public static String serializeEffects(Collection<PotionEffect> effects) {
        String serialized = "";
        for (PotionEffect e : effects)
            serialized = String.valueOf(serialized) + e.getType().getId() + ":" + e.getDuration() + ":" + e.getAmplifier() + ";";
        return serialized;
    }

    public static Collection<PotionEffect> getPotionEffects(String serializedEffects) {
        ArrayList<PotionEffect> effects = new ArrayList<>();
        if (serializedEffects.isEmpty())
            return effects;
        String[] effs = serializedEffects.split(";");
        for (int i = 0; i < effs.length; i++) {
            String[] effect = effs[i].split(":");
            if (effect.length < 3)
                throw new IllegalArgumentException(String.valueOf(serializedEffects) + " - PotionEffect " + i + " (" + effs[i] + "): split must at least have a length of 3");
            if (!Util.isNum(effect[0]))
                throw new IllegalArgumentException(String.valueOf(serializedEffects) + " - PotionEffect " + i + " (" + effs[i] + "): id is not an integer");
            if (!Util.isNum(effect[1]))
                throw new IllegalArgumentException(String.valueOf(serializedEffects) + " - PotionEffect " + i + " (" + effs[i] + "): duration is not an integer");
            if (!Util.isNum(effect[2]))
                throw new IllegalArgumentException(String.valueOf(serializedEffects) + " - PotionEffect " + i + " (" + effs[i] + "): amplifier is not an integer");
            int id = Integer.parseInt(effect[0]);
            int duration = Integer.parseInt(effect[1]);
            int amplifier = Integer.parseInt(effect[2]);
            PotionEffectType effectType = PotionEffectType.getById(id);
            if (effectType == null)
                throw new IllegalArgumentException(String.valueOf(serializedEffects) + " - PotionEffect " + i + " (" + effs[i] + "): no PotionEffectType with id of " + id);
            PotionEffect e = new PotionEffect(effectType, duration, amplifier);
            effects.add(e);
        }
        return effects;
    }

    public static void addPotionEffects(String code, LivingEntity entity) {
        entity.addPotionEffects(getPotionEffects(code));
    }

    public static void setPotionEffects(String code, LivingEntity entity) {
        for (PotionEffect effect : entity.getActivePotionEffects())
            entity.removePotionEffect(effect.getType());
        addPotionEffects(code, entity);
    }
}
