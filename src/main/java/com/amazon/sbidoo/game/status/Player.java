package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class Player {
    public enum PieceType {
        Dog,
        Hat,
        Thimble,
        Boot,
        Wheelbarrow,
        Cat,
        Car,
        Battleship
    }

    private PieceType pieceType;
    private int positionFromStart;
    private int money;
    private boolean isOnTurn;
    private boolean hasRolled;
    private List<Integer> lastRolls;
}
