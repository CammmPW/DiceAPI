package org.dicemc.diceapi.api.messages;

import org.dicemc.diceapi.api.DiceMessage;

public class InvalidSubCommandMessage extends DiceMessage {
    public InvalidSubCommandMessage(String attempt) {
        this.message = "&cYou entered an &6invalid&c subcommand&7:&6 " + attempt;
    }
}
