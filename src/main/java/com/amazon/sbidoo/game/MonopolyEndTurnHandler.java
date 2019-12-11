package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.alexa.AlexaTurnHandler;
import com.amazon.sbidoo.alexa.AlexaTurnResult;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MonopolyEndTurnHandler extends PlayerGameStatus implements EndTurnHandler {

    public static final String END_TURN_INTENT = "EndTurnIntent";
    public static final String ALEXA_RESPONSE_FORMAT = "I rolled a %s and a %s, and am currently at %s and %s and %s";

    private final AlexaTurnHandler alexaTurnHandler;
    private final Logger logger;

    @Inject
    public MonopolyEndTurnHandler(@Named("MonopolyEndTurnHandlerLogger") Logger logger,
                                  final GameStatusDao gameStatusDao,
                                  final AlexaTurnHandler alexaTurnHandler) {
        this.gameStatusDao = gameStatusDao;
        this.alexaTurnHandler = alexaTurnHandler;
        this.logger = logger;
    }

    @Override
    public void updatePlayerStatus(GameStatus gameStatus) {
        endTurn(gameStatus);
    }

    @Override
    public AlexaTurnResult processAlexaTurn(final String userId,
                                            final GameStatus gameStatus) {
        return alexaTurnHandler.handleTurn(userId, gameStatus);
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(END_TURN_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final String userId = handlerInput.getRequestEnvelope().getSession().getUser().getUserId();
        final GameStatus gameStatus = this.gameStatusDao.getGameStatusForUserId(userId);
        updatePlayerStatus(gameStatus);
        AlexaTurnResult alexaTurnResult = processAlexaTurn(userId, gameStatus);
        this.gameStatusDao.updateGameStatusForUserId(gameStatus, userId);
        return handlerInput.getResponseBuilder()
                .withSpeech(buildSpeechString(alexaTurnResult))
                .withShouldEndSession(false)
                .build();
    }

    private String buildSpeechString(AlexaTurnResult alexaTurnResult) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("I rolled %d and %d. ", alexaTurnResult.getDieOne(), alexaTurnResult.getDieTwo()));
        stringBuilder.append(String.format("I am currently on %s. ",
                alexaTurnResult.getEndPositionName()));
        if (!alexaTurnResult.getPurchaseMessage().isEmpty()) {
            stringBuilder.append(alexaTurnResult.getPurchaseMessage()).append(". ");
        }
        if (!alexaTurnResult.getChargedStatement().isEmpty()) {
            stringBuilder.append(alexaTurnResult.getChargedStatement()).append(". ");
        }
        if (!alexaTurnResult.getChanceResult().isEmpty()) {
            stringBuilder.append(alexaTurnResult.getChanceResult()).append(". ");
        }
        return stringBuilder.toString();
    }
}
