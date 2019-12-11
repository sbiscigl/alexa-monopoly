package com.amazon.sbidoo.game;

import com.amazon.sbidoo.game.property.PropertyPurchaseReturn;
import com.amazon.sbidoo.game.status.Board;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Property;
import com.amazon.sbidoo.game.status.Space;

abstract public class PropertyActions extends PlayerGameStatus {

    void buyHouses(final int houses,
                   final Player player,
                   final GameStatus gameStatus) {
        return;
    }

    public PropertyPurchaseReturn buyProperty(final Player player,
                                              final GameStatus gameStatus) {
        Property property = gameStatus.getProperty();
        Board board = gameStatus.getBoard();

        //This is the index of the piece of property you wanna buy
        int propertyIndex = player.getPositionFromStart();
        Space space = board.getSpaceMap().get(propertyIndex);
        int spacePrice = space.getPrice();
        int playerMoney = player.getMoney();

        if (spacePrice == 0) {
            //Alexa should say this
            return PropertyPurchaseReturn.builder()
                    .message("This space cannot be purchased!")
                    .build();
        }

        Space.SpaceType spaceType = board.getSpaceMap().get(propertyIndex).getSpaceType();
        Space.SpaceCategory spaceCategory = board.getSpaceMap().get(propertyIndex).getSpaceCategory();
        String spaceName = board.getSpaceMap().get(propertyIndex).getSpaceName();

        //This is the key to the propertyMap
        Property.SpaceInfo spaceInfo = property.new SpaceInfo(spaceType, spaceCategory, spaceName);

        //Check if the spaceInfo, ownerInfo entry is already in the map, if not allow the player to proceed with purchase
        if (property.getPropertyMap().containsKey(spaceInfo)) {
            Player.PieceType ownerPieceType = property.getPropertyMap().get(spaceInfo).getOwner();
            return PropertyPurchaseReturn.builder()
                    .message("You cannot buy this property because it is already owned by the " + ownerPieceType)
                    .build();
        } else if (!playerHasEnoughMoneyForPurchase(playerMoney, spacePrice)) {
            return PropertyPurchaseReturn.builder()
                    .message("You do not have enough money to purchase this property")
                    .build();
        } else {
            player.updateMoney(spacePrice);
            Property.OwnerInfo propertyOwnerInfo = property. new OwnerInfo(player.getPieceType(), 0 ,0);
            property.getPropertyMap().put(spaceInfo, propertyOwnerInfo);
            return PropertyPurchaseReturn.builder()
                    .message("You purchased " + spaceName)
                    .build();
        }
    }

    private boolean playerHasEnoughMoneyForPurchase(int playerMoney, int price) {
        return ((playerMoney >= price) ? true : false);
    }
}
