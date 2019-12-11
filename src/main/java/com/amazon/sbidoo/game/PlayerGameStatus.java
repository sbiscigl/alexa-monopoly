package com.amazon.sbidoo.game;

import com.amazon.sbidoo.game.status.Board;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.OwnerInfo;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Property;
import com.amazon.sbidoo.game.status.Space;
import com.amazon.sbidoo.game.status.SpaceInfo;

import java.util.Map;
import java.util.Set;

abstract public class PlayerGameStatus {
    protected GameStatusDao gameStatusDao;

    protected void endTurn(GameStatus gameStatus) {
        Player playerOnTurn = getPlayerOnTurn(gameStatus);
        Player playerOffTurn = getPlayerOffTurn(gameStatus);
        playerOnTurn.endTurn();
        playerOffTurn.startTurn();
    }

    protected Player getPlayerOffTurn(final GameStatus gameStatus) {
        Set<Player> players = gameStatus.getPlayers();
        Player playerOffTurn = null;
        for(Player player : players) {
            if (!player.isOnTurn()) {
                playerOffTurn = player;
                break;
            }
        }
        return playerOffTurn;
    }

    protected Player getPlayerOnTurn(final GameStatus gameStatus) {
        Set<Player> players = gameStatus.getPlayers();
        Player playerOnTurn = null;
        for (Player player : players) {
            if (player.isOnTurn()) {
                playerOnTurn = player;
                break;
            }
        }
        return playerOnTurn;
    }

    protected String chargePlayerIfSpaceIsOwned(Player playerOnTurn, GameStatus gameStatus) {
        Property property = gameStatus.getProperty();
        Board board = gameStatus.getBoard();
        int propertyIndex = playerOnTurn.getPositionFromStart();
        Space space = board.getSpaceMap().get(propertyIndex);

        Space.SpaceType spaceType = board.getSpaceMap().get(propertyIndex).getSpaceType();
        Space.SpaceCategory spaceCategory = board.getSpaceMap().get(propertyIndex).getSpaceCategory();
        String spaceName = board.getSpaceMap().get(propertyIndex).getSpaceName();

        int spacePrice = space.getPrice();
        int playerMoney = playerOnTurn.getMoney();
        StringBuilder sb = new StringBuilder();
        if(spaceType == Space.SpaceType.IncomeTax || spaceType == Space.SpaceType.LuxuryTax) {
            sb.append("This space is taxed ").append(spacePrice).append(" dollars.");
            playerOnTurn.updateMoney(playerMoney - spacePrice);
            sb.append("New balance is ").append(playerMoney).append(" dollars.");
            return sb.toString();
        }

        //This is the key to the propertyMap
        SpaceInfo spaceInfo  = new SpaceInfo(spaceType, spaceCategory, spaceName);
        Map<SpaceInfo, OwnerInfo> propertyMap = property.getPropertyMap();

        if(propertyMap.containsKey(spaceInfo)){
            OwnerInfo propertyOwnerInfo = property.getPropertyMap().get(spaceInfo);
            Player.PieceType ownerPieceType = propertyOwnerInfo.getOwner();
            int houses = propertyOwnerInfo.getHouses();
            int hotels = propertyOwnerInfo.getHotels();

            //Alexa should say this
            sb.append("This property is owned by ").append(ownerPieceType).append(".");
            int chargePrice = calculateChargePrice(spacePrice, houses, hotels, propertyMap, spaceType, spaceCategory, ownerPieceType);
            if(playerMoney >= chargePrice) {
                //Alexa should say this
                sb.append("Charging ").append(chargePrice).append(" dollars.");
                playerMoney -= chargePrice;
                playerOnTurn.updateMoney(playerMoney);
                //Alexa should say this
                sb.append("New balance is ").append(playerMoney).append(" dollars.");
                return sb.toString();
            }
            else {
                //Alexa should say this
                sb.append("Not enough money to pay. " + ownerPieceType + "wins!");
                return sb.toString();
            }
        }
        return sb.toString();
    }

    private int calculateChargePrice(int spacePrice,
                                     int houses,
                                     int hotels,
                                     Map<SpaceInfo, OwnerInfo> propertyMap,
                                     Space.SpaceType spaceType,
                                     Space.SpaceCategory spaceCategory,
                                     Player.PieceType ownerPieceType) {
        //Check if owner has color set
        boolean hasColorSet = checkForColorSet(propertyMap, spaceType, spaceCategory, ownerPieceType);

        int chargePrice;
        if(!hasColorSet) {
            chargePrice = (int) Math.floor(spacePrice * 0.07);
            return chargePrice;
        }
        else {
            //This assumes no houses or hotels have been built
            //Add logic later for if house and hotels have been built
            chargePrice = (int) Math.floor(spacePrice * 0.15);
            return chargePrice;
        }

    }

    private boolean checkForColorSet(Map<SpaceInfo, OwnerInfo> propertyMap, Space.SpaceType spaceType, Space.SpaceCategory spaceCategory, Player.PieceType ownerPieceType) {
        int counter = 0;
        boolean hasColorSet = false;
        for(Map.Entry<SpaceInfo, OwnerInfo> entry : propertyMap.entrySet()){
            if(entry.getKey().getSpaceCategory() == spaceCategory
                    && entry.getKey().getSpaceType() == spaceType
                    && entry.getValue().getOwner() == ownerPieceType) {
                counter++;
            }
        }

        //For most properties, three spaces constitutes a color set
        if(counter == 3) {
            hasColorSet = true;
        }

        //For the brown and blue properties, two spaces constitutes a color set
        if(counter == 2 && (spaceCategory == Space.SpaceCategory.Brown || spaceCategory == Space.SpaceCategory.Blue)) {
            hasColorSet = true;
        }
        return hasColorSet;
    }
}
