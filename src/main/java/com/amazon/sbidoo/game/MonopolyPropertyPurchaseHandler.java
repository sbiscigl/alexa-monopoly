package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.Player;

import java.util.Optional;

public class MonopolyPropertyPurchaseHandler implements PropertyPurchaseHandler {
    @Override
    public void buyProperty(Player player, GameStatus gameStatus) {

    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return false;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return Optional.empty();
    }
}
