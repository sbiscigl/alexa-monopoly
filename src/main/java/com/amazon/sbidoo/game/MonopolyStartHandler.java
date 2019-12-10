package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Board;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Property;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class MonopolyStartHandler implements OnStartHandler {

    private final GameStatusDao gameStatusDao;

    @Inject
    public MonopolyStartHandler(final GameStatusDao gameStatusDao) {
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public GameStatus startGame(String userId) {
        final GameStatus initialGameStatus = GameStatus.builder()
                .banker(Banker.builder()
                        .bankroll(0)
                        .freeParkingMoney(0)
                        .build())
                .board(Board.builder()
                        .spaceMap(ImmutableMap.of())
                        .build())
                .players(ImmutableSet.of())
                .property(Property.builder()
                        .propertyMap(ImmutableMap.of())
                        .build())
                .version(0)
                .build();
        this.gameStatusDao.updateGameStatusForUserId(initialGameStatus, userId);
        return initialGameStatus;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        startGame(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
        return handlerInput.getResponseBuilder()
                .withSpeech("Welcome to monopoly. Created a Game. Please take to first turn")
                .build();
    }
}
