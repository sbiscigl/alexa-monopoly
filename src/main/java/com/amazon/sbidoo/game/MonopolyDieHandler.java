package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.sbidoo.chance.ChanceDao;
import com.amazon.sbidoo.community.CommunityChestDao;
import com.amazon.sbidoo.exception.NoPlayerAvailibleException;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Space;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MonopolyDieHandler extends PlayerGameStatus implements DieRollHandler {

    public static final String DIE_ROLL_INTENT = "DiceRollIntent";
    public static final String DIE_ONE = "DieOne";
    public static final String DIE_TWO = "DieTwo";

    private final Logger logger;
    private final ChanceDao chanceDao;
    private final CommunityChestDao communityChestDao;

    @Inject
    public MonopolyDieHandler(@Named("MonopolyDieHandlerLogger") final Logger logger,
                              final GameStatusDao gameStatusDao,
                              final ChanceDao chanceDao,
                              final CommunityChestDao communityChestDao) {
        this.logger = logger;
        this.chanceDao = chanceDao;
        this.communityChestDao = communityChestDao;
        this.gameStatusDao = gameStatusDao;
    }

    @Override
    public String handleDiceRoll(final GameStatus gameStatus,
                                 final Player player,
                                 final int dieOne,
                                 final int dieTwo) {
        if (player != null) {
            player.updatePositionFromStart(dieOne, dieTwo, player);
            final Space.SpaceType spaceType = gameStatus.getBoard()
                    .getSpaceMap()
                    .get(player.getPositionFromStart())
                    .getSpaceType();
            if (spaceType.equals(Space.SpaceType.Chance)) {
                return "You landed on a chance space. " + chanceDao.getActionFromCard().apply(player, gameStatus.getBanker());
            } else if (spaceType.equals(Space.SpaceType.CommunityChest)) {
                return "You landed on a community chest space. " + communityChestDao.getActionFromCard().apply(player, gameStatus.getBanker());
            }
            return "";
        } else {
            throw new NoPlayerAvailibleException("No active players exist");
        }
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName(DIE_ROLL_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        try {
            logger.info("Die roll handler called");
            IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
            final Map<String, Slot> slots = request.getIntent().getSlots();
            final int dieOne = Integer.parseInt(slots.get(DIE_ONE).getValue());
            final int dieTwo = Integer.parseInt(slots.get(DIE_TWO).getValue());
            logger.info(String.format("slot values were found to be: %d, %d", dieOne, dieTwo));
            final String userId = handlerInput.getRequestEnvelope().getSession().getUser().getUserId();
            final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
            final Player playerOnTurn = getPlayerOnTurn(gameStatusForUserId);
            int before = playerOnTurn.getPositionFromStart();
            final String handleDiceRoll = handleDiceRoll(gameStatusForUserId, playerOnTurn, dieOne, dieTwo);
            final String chargedStatement = chargePlayerIfSpaceIsOwned(playerOnTurn, gameStatusForUserId);
            int after = playerOnTurn.getPositionFromStart();
            StringBuilder passedGoStringBuilder = new StringBuilder();
            if (after - before > 12) {
                passedGoStringBuilder.append("You passed go. You collected 200 dollars.");
            }
            this.gameStatusDao.updateGameStatusForUserId(gameStatusForUserId, userId);
            return handlerInput.getResponseBuilder()
                    .withSpeech(buildSpeechString(gameStatusForUserId, playerOnTurn, chargedStatement, handleDiceRoll, passedGoStringBuilder.toString()))
                    .withShouldEndSession(false)
                    .build();
        } catch (Exception e) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("Could you please repeat that? ")
                    .withShouldEndSession(false)
                    .build();
        }
    }

    private String buildSpeechString(GameStatus gameStatusForUserId,
                                     Player playerOnTurn,
                                     String chargedStatement,
                                     String handleDiceRoll,
                                     String passerGoString) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (!handleDiceRoll.isEmpty()) {
            stringBuilder.append(handleDiceRoll).append(". ");
        }
        if (!chargedStatement.isEmpty()) {
            stringBuilder.append(chargedStatement).append(". ");
        }
        if (!passerGoString.isEmpty()) {
            stringBuilder.append(passerGoString).append(". ");
        }
        stringBuilder.append(String.format("You are currently on %s. ",
                gameStatusForUserId.getBoard()
                        .getSpaceMap()
                        .get(playerOnTurn.getPositionFromStart())
                        .getSpaceName()));
        return stringBuilder.toString();
    }
}
