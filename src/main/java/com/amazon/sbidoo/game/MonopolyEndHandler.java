package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.google.inject.Inject;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class MonopolyEndHandler implements OnEndHandler {

    private final GameStatusDao gameStatusDao;

    @Inject
    public MonopolyEndHandler(final GameStatusDao gameStatusDao) {
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public void endGame(String userId) {
        this.gameStatusDao.deleteAllGameStatusForUserId(userId);
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(requestType(SessionEndedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        endGame(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
        return handlerInput.getResponseBuilder()
                .withSpeech("Thank you for playing monopoly")
                .build();
    }
}
