package org.dicemc.diceapi.api.messages;

import org.dicemc.diceapi.api.DiceMessage;

public class InvalidPermissionsMessage extends DiceMessage {
    public InvalidPermissionsMessage() {
        this.message = "&cYou don't have permission";
    }

    public InvalidPermissionsMessage(String message) {
        this.message = message;
    }
}
