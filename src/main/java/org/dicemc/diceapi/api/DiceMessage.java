package org.dicemc.diceapi.api;

import org.dicemc.diceapi.DiceAPI;

public abstract class DiceMessage {
    protected String message;

    public String getMessage() {
        return DiceAPI.getChatUtils().formatMessage(this.message);
    }

    public String toString() {
        return getMessage();
    }
}
