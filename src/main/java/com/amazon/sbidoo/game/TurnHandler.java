package com.amazon.sbidoo.game;

import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;

import java.util.Set;

abstract public class TurnHandler {
    protected GameStatusDao gameStatusDao;

    protected void endTurn(String userId, GameStatus gameStatus) {
        Player playerOnTurn = getPlayerOnTurn(userId, gameStatus);
        Player playerOffTurn = getPlayerOffTurn(userId);
        playerOnTurn.endTurn();
        playerOffTurn.startTurn();
    }

    protected Player getPlayerOnTurn(String userId, GameStatus gameStatus) {
        Set<Player> players = gameStatus.getPlayers();
        Player playerOnTurn = null;
        for(Player player : players) {
            if (player.isOnTurn()) {
                playerOnTurn = player;
                break;
            }
        }
        return playerOnTurn;
    }

    protected Player getPlayerOffTurn(String userId) {
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

    protected Player getPlayerOnTurn(final GameStatus gameStatusForUserId) {
        Set<Player> players = gameStatusForUserId.getPlayers();
        Player playerOnTurn = null;
        for (Player player : players) {
            if (player.isOnTurn()) {
                playerOnTurn = player;
                break;
            }
        }
        return playerOnTurn;
    }
}
