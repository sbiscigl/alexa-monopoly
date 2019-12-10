package com.amazon.sbidoo.alexa;

import com.amazon.sbidoo.game.TurnHandler;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.Logger;

public class MonopolyAlexaTurnHandler extends TurnHandler implements AlexaTurnHandler {

    private final Logger logger;

    @Inject
    public MonopolyAlexaTurnHandler(@Named("MonopolyAlexaTurnHandlerLogger")Logger logger,
                                    final GameStatusDao gameStatusDao) {
        this.gameStatusDao = gameStatusDao;
        this.logger = logger;
    }

    @Override
    public AlexaTurnResult handleTurn(String userId, GameStatus gameStatus) {
        final int dieOne = RandomUtils.nextInt(1, 7);
        final int dieTwo = RandomUtils.nextInt(1, 7);
        final Player playerOnTurn = getPlayerOnTurn(gameStatus);
        if (playerOnTurn == null) {
            logger.info("no player on turn found");
        }
        playerOnTurn.updatePositionFromStart(dieOne, dieTwo);
        endTurn(userId, gameStatus);
        return AlexaTurnResult.builder()
                .dieOne(dieOne)
                .dieTwo(dieTwo)
                .endPositionName(gameStatus.getBoard()
                        .getSpaceMap()
                        .get(playerOnTurn.getPositionFromStart())
                        .getSpaceName())
                .build();
    }
}
