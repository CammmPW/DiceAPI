package org.dicemc.diceapi.api.messages;

import org.dicemc.diceapi.api.DiceMessage;

public class PlayerNotOnlineMessage extends DiceMessage {
    public PlayerNotOnlineMessage(String player) {
        this.message = "&cPlayer &6" + player + " &c does not exist or is not online";
    }
}
