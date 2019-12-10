package com.amazon.sbidoo.game.status;

import lombok.*;

import java.util.List;

@Builder
@Data
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



    public void updatePositionFromStart(int numberRolled){

        int newPositionBeforeNormalization = this.positionFromStart + numberRolled;
        int newPositionFromStart = -1;
        int numOfSpacesOnBoard = 40;

        //this case handles when you go back to the beginning of the board
        if(newPositionBeforeNormalization >= numOfSpacesOnBoard) {
            newPositionFromStart = newPositionBeforeNormalization - numOfSpacesOnBoard;
        }
        else {
            newPositionFromStart = newPositionBeforeNormalization;
        }

        this.positionFromStart += newPositionFromStart;
    }
}
