package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.Monopoly;
import com.amazon.sbidoo.game.status.Board;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.OwnerInfo;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Property;
import com.amazon.sbidoo.game.status.Space;
import com.amazon.sbidoo.game.status.SpaceInfo;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        return handlerInput.getResponseBuilder()
                .withSpeech("this really updated")
                .withShouldEndSession(false)
                .build();
    }

    private void handleDiceRoll(String userId, int dieOne, int dieTwo) {
        Player playerOnTurn = getPlayerOnTurn(userId);
        if(playerOnTurn != null) {
            playerOnTurn.updatePositionFromStart(dieOne, dieTwo);
            chargePlayerIfSpaceIsOwned(userId, playerOnTurn);
        }
        else {
            System.out.println("There are no active players in the game... ");
        }
    }


    private void chargePlayerIfSpaceIsOwned(String userId, Player playerOnTurn) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        Property property = gameStatusForUserId.getProperty();
        Board board = getGameBoard(userId);
        int propertyIndex = playerOnTurn.getPositionFromStart();
        Space space = board.getSpaceMap().get(propertyIndex);

        Space.SpaceType spaceType = board.getSpaceMap().get(propertyIndex).getSpaceType();
        Space.SpaceCategory spaceCategory = board.getSpaceMap().get(propertyIndex).getSpaceCategory();
        String spaceName = board.getSpaceMap().get(propertyIndex).getSpaceName();

        //This is the key to the propertyMap
        SpaceInfo spaceInfo  = new SpaceInfo(spaceType, spaceCategory, spaceName);
        Map<SpaceInfo, OwnerInfo> propertyMap = property.getPropertyMap();

        if(propertyMap.containsKey(spaceInfo)){
            OwnerInfo propertyOwnerInfo = property.getPropertyMap().get(spaceInfo);
            Player.PieceType ownerPieceType = propertyOwnerInfo.getOwner();
            int spacePrice = space.getPrice();
            int houses = propertyOwnerInfo.getHouses();
            int hotels = propertyOwnerInfo.getHotels();

            //Alexa should say this
            System.out.println("This property is owned by " + ownerPieceType + ". ");
            int chargePrice = calculateChargePrice(spacePrice, houses, hotels, propertyMap, spaceType, spaceCategory, ownerPieceType);
            int playerMoney = playerOnTurn.getMoney();
            if(playerMoney >= chargePrice) {
                //Alexa should say this
                System.out.println("Charging you " + chargePrice + " dollars. ");
                playerMoney -= chargePrice;
                playerOnTurn.updateMoney(playerMoney);
                //Alexa should say this
                System.out.println("Your new balance is " + playerMoney + " dollars. ");
                return;
            }
            else {
                //Alexa should say this
                System.out.println("You don't have enough money to pay me, I win! ");
                return;
            }

        }
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

    private void buyProperty(String userId) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        Property property = gameStatusForUserId.getProperty();
        Player playerOnTurn = getPlayerOnTurn(userId);
        Board board = getGameBoard(userId);

        //This is the index of the piece of property you wanna buy
        int propertyIndex = playerOnTurn.getPositionFromStart();
        Space space = board.getSpaceMap().get(propertyIndex);
        int spacePrice = space.getPrice();
        int playerMoney = playerOnTurn.getMoney();

        if(spacePrice == 0){
            //Alexa should say this
            System.out.println("This space cannot be purchased! ");
            return;
        }

        Space.SpaceType spaceType = board.getSpaceMap().get(propertyIndex).getSpaceType();
        Space.SpaceCategory spaceCategory = board.getSpaceMap().get(propertyIndex).getSpaceCategory();
        String spaceName = board.getSpaceMap().get(propertyIndex).getSpaceName();

        //This is the key to the propertyMap
        SpaceInfo spaceInfo  = new SpaceInfo(spaceType, spaceCategory, spaceName);

        //Check if the spaceInfo, ownerInfo entry is already in the map, if not allow the player to proceed with purchase
        if(property.getPropertyMap().containsKey(spaceInfo)){
            Player.PieceType ownerPieceType = property.getPropertyMap().get(spaceInfo).getOwner();
            //Alexa should say this
            System.out.println("You cannot buy this property because it is already owned by the " + ownerPieceType);
        }
        else if(!playerHasEnoughMoneyForPurchase(playerMoney, spacePrice)) {
            //Alexa should say this
            System.out.println("You do not have enough money to purchase this property");
        }
        else {
            playerOnTurn.updateMoney(spacePrice);
            OwnerInfo propertyOwnerInfo = new OwnerInfo(playerOnTurn.getPieceType(), 0 ,0);
        }
        return;
    }

    private boolean playerHasEnoughMoneyForPurchase(int playerMoney, int price) {
        return ((playerMoney >= price) ? true : false);
    }

    private Board getGameBoard(String userId) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        Board board = gameStatusForUserId.getBoard();
        return board;
    }

    private void endTurn(String userId) {
        Player playerOnTurn = getPlayerOnTurn(userId);
        Player playerOffTurn = getPlayerOffTurn(userId);
        playerOnTurn.endTurn();
        playerOffTurn.startTurn();
    }

    private Player getPlayerOnTurn(String userId) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        Set<Player> players = gameStatusForUserId.getPlayers();
        Player playerOnTurn = null;
        for(Player player : players) {
            if (player.isOnTurn()) {
                playerOnTurn = player;
                break;
            }
        }
        return playerOnTurn;
    }

    private Player getPlayerOffTurn(String userId) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        Set<Player> players = gameStatusForUserId.getPlayers();
        Player playerOffTurn = null;
        for(Player player : players) {
            if (!player.isOnTurn()) {
                playerOffTurn = player;
                break;
            }
        }
        return playerOffTurn;
    }

}

