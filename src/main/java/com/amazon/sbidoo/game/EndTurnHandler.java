package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.sbidoo.alexa.AlexaTurnResult;
import com.amazon.sbidoo.game.status.GameStatus;

public interface EndTurnHandler extends RequestHandler {
    void updatePlayerStatus(GameStatus gameStatus);
    AlexaTurnResult processAlexaTurn(String userId, GameStatus gameStatus);
}
