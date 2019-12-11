package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.Player;

public interface HotelPurchaseHandler extends RequestHandler {
    void purchaseHotel(GameStatus gameStatus, Player player, int numberOfHotels);
}
