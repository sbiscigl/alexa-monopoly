package com.amazon.sbidoo.alexa;

import com.amazon.sbidoo.game.PropertyActions;
import com.amazon.sbidoo.game.property.PropertyPurchaseReturn;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MonopolyAlexaPlayerGameStatus extends PropertyActions implements AlexaTurnHandler {

    private final Logger logger;

    @Inject
    public MonopolyAlexaPlayerGameStatus(@Named("MonopolyAlexaTurnHandlerLogger") Logger logger,
                                         final GameStatusDao gameStatusDao) {
        this.gameStatusDao = gameStatusDao;
        this.logger = logger;
    }

    @Override
    public AlexaTurnResult handleTurn(String userId, GameStatus gameStatus) {
        final int dieOne = RandomUtils.nextInt(1, 7);
        final int dieTwo = RandomUtils.nextInt(1, 7);
        final Player playerOnTurn = getPlayerOnTurn(gameStatus);
        playerOnTurn.updatePositionFromStart(dieOne, dieTwo);
        Optional<PropertyPurchaseReturn> propertyPurchaseReturn;
        if (playerOnTurn.getMoney() >= gameStatus.getBoard().getSpaceMap().get(playerOnTurn.getPositionFromStart()).getPrice()) {
            propertyPurchaseReturn = Optional.of(buyProperty(playerOnTurn, gameStatus));
        } else {
            propertyPurchaseReturn = Optional.empty();
        }
        endTurn(gameStatus);
        return AlexaTurnResult.builder()
                .dieOne(dieOne)
                .dieTwo(dieTwo)
                .endPositionName(gameStatus.getBoard()
                        .getSpaceMap()
                        .get(playerOnTurn.getPositionFromStart())
                        .getSpaceName())
                .purchaseMessage(propertyPurchaseReturn.orElse(PropertyPurchaseReturn.builder()
                        .message("could Not buy property")
                        .build())
                        .getMessage())
                .build();
    }
}
