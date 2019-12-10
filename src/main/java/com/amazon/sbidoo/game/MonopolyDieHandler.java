package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.sbidoo.exception.NoPlayerAvailibleException;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MonopolyDieHandler extends TurnHandler implements DieRollHandler {

    public static final String DIE_ROLL_INTENT = "DieRollIntent";
    public static final String DIE_ONE = "DieOne";
    public static final String DIE_TWO = "DieTwo";
    public static final String ROLL_STATUS_FORMAT = "You are currently on %s";

    private final Logger logger;
    private final GameStatusDao gameStatusDao;

    public MonopolyDieHandler(@Named("MonopolyDieHandlerLogger") final Logger logger,
                              final GameStatusDao gameStatusDao) {
        this.logger = logger;
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public void handleDiceRoll(final Player player,
                               final int dieOne,
                               final int dieTwo) {
        if (player != null) {
            player.updatePositionFromStart(dieOne, dieTwo);
        }
        throw new NoPlayerAvailibleException("No active players exist");
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(DIE_ROLL_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        logger.info("Die roll handler called");
        IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = request.getIntent().getSlots();
        final int dieOne = Integer.parseInt(slots.get(DIE_ONE).getValue());
        final int dieTwo = Integer.parseInt(slots.get(DIE_TWO).getValue());
        logger.info(String.format("slot values were found to be: %d, %d", dieOne, dieTwo));
        final String userId = handlerInput.getRequestEnvelope().getSession().getUser().getUserId();
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        final Player playerOnTurn = getPlayerOnTurn(gameStatusForUserId);
        handleDiceRoll(playerOnTurn, dieOne, dieTwo);
        return handlerInput.getResponseBuilder()
                .withSpeech(String.format(ROLL_STATUS_FORMAT,
                        gameStatusForUserId.getBoard()
                                .getSpaceMap()
                                .get(playerOnTurn.getPositionFromStart())
                                .getSpaceName()))
                .build();
    }
}
