package org.dicemc.diceapi.api.messages;

import org.dicemc.diceapi.api.DiceMessage;

public class TooFewArgumentsMessage extends DiceMessage {
    public TooFewArgumentsMessage(String usage) {
        this.message = "&cToo few arguments&7: &6" + usage;
    }
}
