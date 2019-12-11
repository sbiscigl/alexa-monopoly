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

public class MonopolyHotelPurchaseHandler extends PlayerGameStatus implements HotelPurchaseHandler {

    public static final String BUY_HOTEL_INTENT = "BuyHotelIntent";
    private final Logger logger;

    @Inject
    public MonopolyHotelPurchaseHandler(@Named("MonopolyHotelPurchaseHandlerLogger") final Logger logger,
                                        final GameStatusDao gameStatusDao) {
        this.logger = logger;
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public void purchaseHotel(final GameStatus gameStatus,
                                    final Player player,
                                    final int numberOfHotels) {
        return;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(BUY_HOTEL_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        logger.info("Buy hotel intent called");
        final String userId = handlerInput.getRequestEnvelope().getSession().getUser().getUserId();
        final GameStatus gameStatus = this.gameStatusDao.getGameStatusForUserId(userId);
        final Player player = getPlayerOnTurn(gameStatus);
        IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = request.getIntent().getSlots();
        final int hotels = Integer.parseInt(slots.get("NumberOfHotels").getValue());
        logger.info(String.format("attempting to buy %s hotels on %s",
                hotels,
                gameStatus.getBoard().getSpaceMap().get(player.getPositionFromStart()).getSpaceName()));
        purchaseHotel(gameStatus, player, hotels);
        this.gameStatusDao.updateGameStatusForUserId(gameStatus, userId);
        return handlerInput.getResponseBuilder()
                .withSpeech("Not Implemented")
                .build();
    }
}
