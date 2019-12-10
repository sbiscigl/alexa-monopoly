package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.Monopoly;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MonopolyGameHandler implements GameHandler {

    private static final Logger logger = LogManager.getLogger(Monopoly.class);

    private final GameStatusDao gameStatusDao;

    @Inject
    public MonopolyGameHandler(GameStatusDao gameStatusDao) {
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        whatever(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
        return handlerInput.getResponseBuilder()
                .withSpeech("this really updated")
                .withShouldEndSession(false)
                .build();
    }

    private void whatever(String userId) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
//        this.gameStatusDao.updateGameStatusForUserId(gameStatus, userId);
    }
}
