package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.sbidoo.game.status.GameStatus;

public interface OnStartHandler extends RequestHandler {
    GameStatus startGame(String userId);
}
