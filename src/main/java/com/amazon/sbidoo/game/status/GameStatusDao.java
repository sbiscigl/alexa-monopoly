package com.amazon.sbidoo.game.status;

public interface GameStatusDao {
    GameStatus getGameStatusForUserId(String userId);
    void updateGameStatusForUserId(GameStatus gameStatus, String userId);
    void deleteAllGameStatusForUserId(String userId);
}
