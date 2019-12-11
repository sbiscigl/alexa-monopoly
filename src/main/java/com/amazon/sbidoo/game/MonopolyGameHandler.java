package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.Monopoly;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
import com.amazon.sbidoo.game.status.Property;
import com.amazon.sbidoo.game.status.Board;
import com.amazon.sbidoo.game.status.Space;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            //check if someone owns the spot you just landed on
        }
        else {
            System.out.println("There are no active players in the game...");
        }
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
            System.out.println("This space cannot be purchased!");
        }

        Space.SpaceType spaceType = board.getSpaceMap().get(propertyIndex).getSpaceType();
        Space.SpaceCategory spaceCategory = board.getSpaceMap().get(propertyIndex).getSpaceCategory();
        String spaceName = board.getSpaceMap().get(propertyIndex).getSpaceName();

        //This is the key to the propertyMap
        Property.SpaceInfo spaceInfo  = property.new SpaceInfo(spaceType, spaceCategory, spaceName);

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
            Property.OwnerInfo propertyOwnerInfo = property.getPropertyMap().get(spaceInfo);
            propertyOwnerInfo.setOwner(playerOnTurn.getPieceType());
            propertyOwnerInfo.setHouses(0);
            propertyOwnerInfo.setHotels(0);
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

