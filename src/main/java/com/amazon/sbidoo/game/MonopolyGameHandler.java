package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.sbidoo.Monopoly;
import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;
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
        whatever(handlerInput.getRequestEnvelope().getSession().getUser().getUserId());
        return handlerInput.getResponseBuilder()
                .withSpeech("this really updated")
                .withShouldEndSession(false)
                .build();
    }

    private void whatever(String userId) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
//        this.gameStatusDao.updateGameStatusForUserId(gameStatus, userId);
    }

    private void handleDiceRoll(String userId, int dieOne, int dieTwo) {
        final GameStatus gameStatusForUserId = this.gameStatusDao.getGameStatusForUserId(userId);
        Set<Player> players = gameStatusForUserId.getPlayers();
        Player currentPlayer = null;
        //loop through the set of players and grab the first player whose isOnTurn is set to true;
        for(Player player : players) {
            if (player.isOnTurn()) {
                currentPlayer = player;
                break;
            }
        }
        if(currentPlayer != null) {
            currentPlayer.updatePositionFromStart(dieOne, dieTwo);
        }
        else {
            System.out.println("There are no active players in the game...");
        }
    }

}

