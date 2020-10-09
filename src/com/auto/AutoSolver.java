package com.auto;
import com.cards.*;
import java.util.*;

public class AutoSolver {
    private int numRepeat, maxInc, maxDrc, numBuckets, center;
    private int[] values;

    /* 
    * numRepeat:  Number of moves ahead the algorithm looks
    * values[0] :  Value of a free space
    * values[1] :  Value of a unknown card
    * values[2] :  Value of removing a card from another not in numerical order
    * values[3] :  Value of having cards in numerical order but not of the same suit
    * values[4] :  Value of having cards in numerical order and of the same suit
    -------------- Calculated from values and numRepeat ---------------------------
    * maxInc    :  The maximum value a MoveSet can increase in value per turn
    * maxDrc    :  The maximum value a MoveSet can decrease in value per turn
    * numBuckets:  The maximum number if buckets needed to accomidate all possible MoveSet values
    * center    :  The index that represent the value 0
    */
    public AutoSolver(int numRepeat, int[] values) {
        this.numRepeat = numRepeat;
        this.values = values;
        this.maxInc = Math.max(Math.max(this.values[0], this.values[1]), this.values[2]) + this.values[3] + this.values[4];
        this.maxDrc = Math.max(this.values[0], this.values[4]) + this.values[3];
        this.numBuckets = (this.maxInc + this.maxDrc) * (this.numRepeat + 1) + 1;
        this.center = this.maxDrc * this.numRepeat;
    }

    /*
    * Input: Field field (Current working Field)
    * Output: Best MoveSet found within numRepeat moves
    */
    public MoveSet getNextSet(Field field) {
        int start, end, value;
        MoveSet bestMoves = null;
        ArrayList<HashSet<MoveSet>> tempA;
        MoveSet setNew;

        // All previous MoveSets so nothing is repeated for faster run time
        ArrayList<HashSet<MoveSet>> allSets = new ArrayList<HashSet<MoveSet>>(this.numBuckets);
        // The sets that have just been proccessed
        ArrayList<HashSet<MoveSet>> prevSets = new ArrayList<HashSet<MoveSet>>(this.numBuckets);
        // All newly created sets
        ArrayList<HashSet<MoveSet>> newSets = new ArrayList<HashSet<MoveSet>>(this.numBuckets);

        // Initialize all the HashSets
        for (int i = 0; i < this.numBuckets; i ++) {
            allSets.add(new HashSet<MoveSet>(1000000));
            prevSets.add(new HashSet<MoveSet>(1000000));
            newSets.add(new HashSet<MoveSet>(100000));
        }
        // Add current field with empty MoveSet to prevSets so it will be used
        prevSets.get(this.center).add(new MoveSet(field));
        int maxVal = 0;
        int minVal = 0;

        // Look foward numRepeat (int) moves
        for (int i = 0; i < this.numRepeat; i ++) {
            // Calculate (based on min and max numbers, turn, and calculated numbers) what values to look at
            start = Math.max((maxVal + 1) - this.maxInc * (this.numRepeat - i), minVal) + this.center;
            end =  maxVal + this.center;
            for (int y = end; y >= start; y--) {
                // Get each move set
                for (MoveSet mSet: prevSets.get(y)){
                    // Find all possible moves for its field
                    for (Move move: getAvailableMoves(mSet)) {
                        // If within the next this.numRepeat - i - 1 turns it can't reach max value: skip 
                        if(mSet.getValue() + move.getValue() < (maxVal + 1) - this.maxInc * (this.numRepeat - i - 1)) {
                            continue;
                        }
                        // Clone MoveSet and add move
                        setNew = new MoveSet(mSet, move);
                        if (setNew.add(move)) { // Returns true if move completes a row of twelve
                            // Add value based on what is below the row
                            if(setNew.getField().isDone()) {
                                return setNew;
                            }
                            if (setNew.getField().get(move.getTo()) == null) {
                                setNew.addValue(this.values[0]);
                            } else if (!setNew.getField().get(move.getTo()).isKnown()) {
                                setNew.addValue(this.values[1]);
                            }
                        }
                        // Value of new action
                        value = setNew.getValue();
                        // If there is already a MoveSet with the same field: skip
                        if(allSets.get(value + this.center).contains(setNew)){
                            continue;
                        }
                        // add new MoveSet to appropriate bucket
                        newSets.get(value + this.center).add(setNew);

                        // Set new min/max values if needed
                        if(value > maxVal){
                            bestMoves = setNew;
                            maxVal = value;
                        } else if(value < minVal) {
                            minVal = value;
                        }
                    }
                }
            }
            // add all values in prevSets to all sets and newSets becomes prevSets
            for (int y = start - this.maxDrc; y <= end + this.maxInc; y++) {
                allSets.get(y).addAll(prevSets.get(y));
                prevSets.set(y, new HashSet<MoveSet>(100000));
            }
            tempA = prevSets;
            prevSets = newSets;
            newSets = tempA;
        }
        return bestMoves;
    }

    /*
    * Input: Field field (A Field)
    * Output: A list of all possible moves for that field with calculated values
    */
    public ArrayList<Move> getAvailableMoves(MoveSet mSet) {
        Field field = mSet.getField();
        ArrayList<Move> moves = new ArrayList<Move>();
        Card other, top, bottem;
        int depth, value, maxDepth;
        Boolean toNull;

        // Look at the top card in each lane of the field
        for(int from = 0; from < 7; from++) {
            toNull = false;
            top = field.get(from);
            if (top == null || !top.isKnown()) {
                continue;
            }
            bottem = field.maxDepth(from);
            maxDepth = bottem.getNum() - top.getNum();
            // Find the max depth of possible move and the bottem card
            // Look through all other top cards
            for (int i = 0; i < 7; i ++) {
                if (i == from) {
                    continue;
                }
                // Based on set values calculate value of move if possible
                value = 0;
                other = field.get(i);
                // Don't allow moving from one blank space to another because that's waseful
                if (other == null && bottem.getNext() != null && !toNull) {
                    toNull = true;
                    value -= this.values[0];
                    if (! bottem.getNext().isKnown()) {
                        value += this.values[1];
                    } else if (bottem.getNext().getNum() != bottem.getNum() + 1) {
                        value += this.values[2];
                    } else if (bottem.getNext().getSuit() != bottem.getSuit()) {
                        value -= this.values[3];
                    }
                    moves.add(new Move(maxDepth, from, i, value));
                } else if (other != null && other.getNum() > top.getNum() && other.getNum() <= bottem.getNum() + 1 && other.isKnown()) {
                    depth = other.getNum() - (top.getNum() + 1);
                    if (depth == maxDepth) {
                        if(bottem.getNext() == null) {
                            value += this.values[0];
                        } else if (! bottem.getNext().isKnown()) {
                            value += this.values[1];
                        } else if (bottem.getNext().getNum() != bottem.getNum() + 1) {
                            value += this.values[2];
                        } else if (bottem.getNext().getSuit() != bottem.getSuit()) {
                            value -= this.values[3];
                        } 
                        if(other.getSuit() == bottem.getSuit()) {
                            value += this.values[3] + this.values[4];
                        } else {
                            value += this.values[3];
                        }
                    } else {
                        value -= this.values[3] + this.values[4];
                    }
                    moves.add(new Move(depth, from, i, value));
                }
            }
        }
        return moves;
    }
}