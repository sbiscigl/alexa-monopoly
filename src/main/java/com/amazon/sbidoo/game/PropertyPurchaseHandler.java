package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.sbidoo.game.property.PropertyPurchaseReturn;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.Player;

public interface PropertyPurchaseHandler extends RequestHandler {
    PropertyPurchaseReturn buy(final Player player, final GameStatus gameStatus);
}
