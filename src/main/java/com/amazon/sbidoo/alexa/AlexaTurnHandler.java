package com.amazon.sbidoo.alexa;

import com.amazon.sbidoo.game.status.GameStatus;

public interface AlexaTurnHandler {
    AlexaTurnResult handleTurn(String userId, GameStatus gameStatus);
}
