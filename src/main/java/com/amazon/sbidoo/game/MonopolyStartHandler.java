package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Property;
import com.amazon.sbidoo.game.status.util.BoardCreationHelper;
import com.google.common.collect.ImmutableList;
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
                        .bankroll(20_580)
                        .freeParkingMoney(0)
                        .build())
                .board(BoardCreationHelper.createBoard())
                .players(ImmutableSet.of(
                        Player.builder()
                                .hasRolled(false)
                                .rollsAgain(false)
                                .isInJail(false)
                                .wereLastRollsDoubles(ImmutableList.of())
                                .isOnTurn(true)
                                .money(1_500)
                                .pieceType(Player.PieceType.Penguin)
                                .positionFromStart(0)
                                .build(),
                        Player.builder()
                                .hasRolled(false)
                                .rollsAgain(false)
                                .isInJail(false)
                                .wereLastRollsDoubles(ImmutableList.of())
                                .isOnTurn(false)
                                .money(1_500)
                                .pieceType(Player.PieceType.Dog)
                                .positionFromStart(0)
                                .build()
                ))
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
                .withSpeech("Welcome to monopoly. I've created a game. Please take your first turn")
                .withShouldEndSession(false)
                .build();
    }
}
