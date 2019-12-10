package com.amazon.sbidoo.game.status;

import java.util.List;

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
    private List<Integer> lastRolls;

    public Player(final PieceType pieceType,
                  final int positionFromStart,
                  final int money,
                  final boolean isOnTurn,
                  final List<Integer> lastRolls) {
        this.pieceType = pieceType;
        this.positionFromStart = positionFromStart;
        this.money = money;
        this.isOnTurn = isOnTurn;
        this.lastRolls = lastRolls;
    }
}
