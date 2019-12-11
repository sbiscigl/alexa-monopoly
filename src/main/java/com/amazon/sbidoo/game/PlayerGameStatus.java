package com.amazon.sbidoo.game;

import com.amazon.sbidoo.game.status.GameStatus;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.Player;

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
}
