package com.amazon.sbidoo.alexa;

import com.amazon.sbidoo.chance.ChanceDao;
import com.amazon.sbidoo.community.CommunityChestDao;
import com.amazon.sbidoo.game.PropertyActions;
import com.amazon.sbidoo.game.property.PropertyPurchaseReturn;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Space;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MonopolyAlexaPlayerGameStatus extends PropertyActions implements AlexaTurnHandler {

    private final Logger logger;
    private final ChanceDao chanceDao;
    private final CommunityChestDao communityChestDao;


    @Inject
    public MonopolyAlexaPlayerGameStatus(@Named("MonopolyAlexaTurnHandlerLogger") Logger logger,
                                         final GameStatusDao gameStatusDao,
                                         final ChanceDao chanceDao,
                                         final CommunityChestDao communityChestDao) {
        this.chanceDao = chanceDao;
        this.communityChestDao = communityChestDao;
        this.gameStatusDao = gameStatusDao;
        this.logger = logger;
    }

    @Override
    public AlexaTurnResult handleTurn(String userId, GameStatus gameStatus) {
        final int dieOne = RandomUtils.nextInt(1, 7);
        final int dieTwo = RandomUtils.nextInt(1, 7);
        final Player playerOnTurn = getPlayerOnTurn(gameStatus);
        int before = playerOnTurn.getPositionFromStart();
        playerOnTurn.updatePositionFromStart(dieOne, dieTwo, playerOnTurn);
        final Space.SpaceType spaceType = gameStatus.getBoard()
                .getSpaceMap()
                .get(playerOnTurn.getPositionFromStart())
                .getSpaceType();
        StringBuilder chanceResultBuilder = new StringBuilder();
        if (spaceType.equals(Space.SpaceType.Chance)) {
            chanceResultBuilder.append(chanceDao.getActionFromCard().apply(playerOnTurn, gameStatus.getBanker()));
        } else if (spaceType.equals(Space.SpaceType.CommunityChest)) {
            chanceResultBuilder.append(communityChestDao.getActionFromCard().apply(playerOnTurn, gameStatus.getBanker()));
        }
        final String chanceResult = chanceResultBuilder.toString();
        Optional<PropertyPurchaseReturn> propertyPurchaseReturn;
        final String chargedStatement = chargePlayerIfSpaceIsOwned(playerOnTurn, gameStatus);
        if (playerOnTurn.getMoney() >= gameStatus.getBoard().getSpaceMap().get(playerOnTurn.getPositionFromStart()).getPrice() && chargedStatement.isEmpty()) {
            propertyPurchaseReturn = Optional.of(buyProperty(playerOnTurn, gameStatus));
        } else {
            propertyPurchaseReturn = Optional.empty();
        }
        int after = playerOnTurn.getPositionFromStart();
        StringBuilder passedGoStringBuilder = new StringBuilder();
        if (after - before > 12) {
            passedGoStringBuilder.append("You passed go. You collected 200 dollars.");
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
                        .message("could Not buy property. ")
                        .build())
                        .getMessage())
                .chargedStatement(chargedStatement)
                .chanceResult(chanceResult)
                .passedGoMessage(passedGoStringBuilder.toString())
                .build();
    }
}
