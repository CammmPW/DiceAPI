package org.dicemc.diceapi.api.messages;

import org.dicemc.diceapi.api.DiceMessage;

public class TooManyArgumentsMessage extends DiceMessage {
    public TooManyArgumentsMessage(String usage) {
        this.message = "&cToo many arguments&7: &6" + usage;
    }
}
