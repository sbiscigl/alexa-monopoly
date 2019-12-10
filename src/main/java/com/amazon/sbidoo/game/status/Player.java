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
    private boolean isInJail;
    private boolean hasRolled;
    private boolean rollsAgain;
    private List<Boolean> wereLastRollsDoubles;



    public void updatePositionFromStart(int dieOne, int dieTwo){

        trackPreviousRolls(dieOne, dieTwo);

        //I will uncomment this method when we decide we want to implement doubles rules
        //checkForDoubles();

        int numberRolled = dieOne + dieTwo;
        int newPositionBeforeNormalization = this.positionFromStart + numberRolled;
        int newPositionFromStart;
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

    public void startTurn() {
        this.isOnTurn = true;
    }

    public void endTurn() {
        this.hasRolled = false;
        this.rollsAgain = false;
        this.isOnTurn = false;
    }

    private void trackPreviousRolls(int dieOne, int dieTwo) {
        System.out.println("tracking previous rolls...");
        boolean rolledDoubles = false;
        if (dieOne == dieTwo) { rolledDoubles = true; }
        wereLastRollsDoubles.add(0, rolledDoubles);
    }


    private void checkForDoubles() {
        //if player is in jail and rolls doubles, they get out and their turn ends
        if(this.isInJail && wereLastRollsDoubles.get(0) == true){
            this.isInJail = false;
            this.isOnTurn = false;
            return;
        }
        //if player rolls 3 doubles in a row, they go to jail and their turn ends
        if(wereLastRollsDoubles.get(0) == true && wereLastRollsDoubles.get(1) == true && wereLastRollsDoubles.get(2) == true){
            System.out.println("You rolled 3 doubles in a row. Go to jail!");
            this.isInJail = true;
            this.isOnTurn = false;
        }
        //if player rolls doubles, they go again
        else if(wereLastRollsDoubles.get(0) == true) {
            System.out.println("You get to roll again!");
            this.rollsAgain = true;
        }
    }
}
