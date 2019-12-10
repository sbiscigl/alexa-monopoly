package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;

public interface OnEndHandler extends RequestHandler {
    void endGame(String userId);
}
