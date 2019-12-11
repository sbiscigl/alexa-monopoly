package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MonopolyHousePurchaseHandler extends PropertyActions implements HousePurchaseHandler {

    public static final String BUY_HOUSES_INTENT = "BuyHousesIntent";
    public static final String NUMBER_OF_HOUSES = "NumberOfHouses";
    private final Logger logger;

    @Inject
    public MonopolyHousePurchaseHandler(@Named("MonopolyHousePurchaseHandlerLogger") final Logger logger,
                                        final GameStatusDao gameStatusDao) {
        this.logger = logger;
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public void purchaseHouse(final GameStatus gameStatus,
                              final Player player,
                              final int numberOfHouses) {
        buyHouses(numberOfHouses, player, gameStatus);
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(BUY_HOUSES_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        logger.info("buy house intent called");
        final String userId = handlerInput.getRequestEnvelope().getSession().getUser().getUserId();
        final GameStatus gameStatus = this.gameStatusDao.getGameStatusForUserId(userId);
        final Player player = getPlayerOnTurn(gameStatus);
        IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = request.getIntent().getSlots();
        final int houses = Integer.parseInt(slots.get(NUMBER_OF_HOUSES).getValue());
        logger.info(String.format("attempting to buy %s houses on %s",
                houses,
                gameStatus.getBoard().getSpaceMap().get(player.getPositionFromStart()).getSpaceName()));
        purchaseHouse(gameStatus, player, houses);
        this.gameStatusDao.updateGameStatusForUserId(gameStatus, userId);
        return handlerInput.getResponseBuilder()
                .withSpeech("Not Implemented")
                .build();
    }
}
