package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.Player;

public interface HousePurchaseHandler extends RequestHandler {
    void purchaseHouse(GameStatus gameStatus, Player player, int numberOfHouses);
}
