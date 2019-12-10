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
    private List<Boolean> wereLastRollsDoubles;



    public void updatePositionFromStart(int dieOne, int dieTwo){

        trackPreviousRolls(dieOne, dieTwo);
        checkForDoubles();

        int numberRolled = dieOne + dieTwo;
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

    private void trackPreviousRolls(int dieOne, int dieTwo) {
        System.out.println("tracking previous rolls...");
        boolean rolledDoubles = false;
        if (dieOne == dieTwo) { rolledDoubles = true; }
        wereLastRollsDoubles.add(0, rolledDoubles);
    }


    private void checkForDoubles() {
        if(wereLastRollsDoubles.get(0) == true && wereLastRollsDoubles.get(1) == true && wereLastRollsDoubles.get(2) == true){
            System.out.println("You go to jail!");
        }
        else if(wereLastRollsDoubles.get(0) == true) {
            System.out.println("You get to roll again!");
        }
    }
}
