package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.sbidoo.game.status.Player;

public interface DieRollHandler extends RequestHandler {
    void handleDiceRoll(Player userId, int dieOne, int dieTwo);
}
