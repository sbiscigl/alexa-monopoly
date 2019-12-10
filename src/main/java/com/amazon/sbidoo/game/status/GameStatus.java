package com.amazon.sbidoo.game.status;

import java.util.Set;

public class GameStatus {
    private final int version;
    private final Set<Player> player;
    private final Banker banker;
    private final Property property;
    private final Board board;

    public GameStatus(final int version,
                      final Set<Player> player,
                      final Banker banker,
                      final Property property,
                      final Board board) {
        this.version = version;
        this.player = player;
        this.banker = banker;
        this.property = property;
        this.board = board;
    }

    public int getVersion() {
        return version;
    }
}
