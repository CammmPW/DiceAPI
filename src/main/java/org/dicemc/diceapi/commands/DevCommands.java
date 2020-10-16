package org.dicemc.diceapi.commands;

import org.dicemc.diceapi.DiceAPI;
import org.dicemc.diceapi.api.ncommands.Command;
import org.dicemc.diceapi.api.ncommands.CommandContext;
import org.dicemc.diceapi.api.ncommands.CommandPermission;
import org.dicemc.diceapi.api.ncommands.ParentCommand;
import org.dicemc.diceapi.api.serialization.SingleItemSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DevCommands {
    @ParentCommand("dev")
    @Command(name = "item-as-string", args = "[printToConsole]", aliases = {"ias"}, desc = "Convert your held item to a TacoSerialization string")
    @CommandPermission("TacoAPI.dev.itemAsString")
    public static void itemAsString(CommandContext context) {
        ItemStack hand = context.getPlayer().getInventory().getItemInMainHand();
        if (hand == null) {
            context.sendMessageToSender("&cYou are not holding anything");
            return;
        }
        boolean console = context.gt(0) && Boolean.parseBoolean(context.getString(0));
        String str = SingleItemSerialization.serializeItemAsString(hand);
        if (console) {
            DiceAPI.instance.chat.out(str);
        } else {
            context.sendMessageToSender(str);
        }
    }

    @ParentCommand("dev")
    @Command(name = "gpy", desc = "Get your pitch/yaw angles")
    @CommandPermission("TacoAPI.dev.getPitchYaw")
    public static void getPitchYaw(CommandContext context) {
        Player p = context.getPlayer();
        double pitch = p.getLocation().getPitch();
        double yaw = p.getLocation().getYaw();
        context.sendMessageToSender("&aPitch&7: &2" + pitch + " &aYaw&7: &2" + yaw);
    }
}
