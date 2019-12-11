package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.game.property.PropertyPurchaseReturn;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MonopolyPropertyPurchaseHandler extends PropertyActions implements PropertyPurchaseHandler {

    public static final String BUY_PROPERTY_INTENT = "BuyPropertyIntent";
    private final Logger logger;

    @Inject
    public MonopolyPropertyPurchaseHandler(@Named("MonopolyPropertyPurchaseHandlerLogger") final Logger logger,
                                           final GameStatusDao gameStatusDao) {
        this.logger = logger;
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public PropertyPurchaseReturn buy(Player player, GameStatus gameStatus) {
        return buyProperty(player, gameStatus);
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(BUY_PROPERTY_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        logger.info("Buy property intent called");
        final String userId = handlerInput.getRequestEnvelope().getSession().getUser().getUserId();
        final GameStatus gameStatus = this.gameStatusDao.getGameStatusForUserId(userId);
        final Player player = getPlayerOnTurn(gameStatus);
        final PropertyPurchaseReturn propertyPurchaseReturn = buyProperty(player, gameStatus);
        this.gameStatusDao.updateGameStatusForUserId(gameStatus, userId);
        return handlerInput.getResponseBuilder()
                .withSpeech(propertyPurchaseReturn.getMessage())
                .build();
    }
}
