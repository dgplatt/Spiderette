package com.auto;
import com.cards.*;
import java.util.*;

public class AutoSolver {
    private int numRepeat, maxInc, maxDrc, numBuckets, center;
    private Values values;

    /* 
    * numRepeat :  Number of moves ahead the algorithm looks
    * values    :  The values used to determine moves to make
    * numBuckets:  The maximum number if buckets needed to accomidate all possible MoveSet values
    * center    :  The index that represent the value 0
    */
    public AutoSolver(int numRepeat, Values values) {
        this.numRepeat = numRepeat;
        this.values = values;
        this.maxInc = values.getMaxIncrease();
        this.maxDrc = values.getMaxDecrease();
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
            prevSets.add(new HashSet<MoveSet>());
            newSets.add(new HashSet<MoveSet>(3000));
        }
        // Add current field with empty MoveSet to prevSets so it will be used
        prevSets.get(this.center).add(new MoveSet(field));
        int maxVal = 0;
        int minVal = 0;
        boolean again = true;
        // Look foward numRepeat (int) moves
        for (int i = 0; i < this.numRepeat && again; i ++) {
            // Calculate (based on min and max numbers, turn, and calculated numbers) what values to look at
            start = Math.max((maxVal + 1) - this.maxInc * (this.numRepeat - i), minVal) + this.center;
            end =  maxVal + this.center;
            again = false;
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
                        //System.out.println(setNew.getField());
                        //System.out.println(setNew.getValue());
                        if (setNew.getComplete()) { // Returns true if move completes a row of twelve
                            // Add value based on what is below the row
                            if(setNew.getField().isDone()) {
                                return setNew;
                            }
                            if (setNew.getField().get(move.getTo()) == null) {
                                setNew.addValue(this.values.getSpaceValue());
                            } else if (!setNew.getField().get(move.getTo()).isKnown()) {
                                setNew.addValue(this.values.getUnknownValue());
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
                        again = true;
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
                prevSets.set(y, new HashSet<MoveSet>(3000));
            }
            tempA = prevSets;
            prevSets = newSets;
            newSets = tempA;
        }
        return bestMoves;
    }

    private int getRemoveValue(Card fromBottom) {
        Card nextCard = fromBottom.getNext();
        if(nextCard == null) {
            return this.values.getSpaceValue();
        } else if (! nextCard.isKnown()) {
            return this.values.getUnknownValue();
        } else if (nextCard.getNum() != fromBottom.getNum() + 1) {
            return this.values.getDisorderedValue();
        } else if (nextCard.getSuit() != fromBottom.getSuit()) {
            return this.values.getOrderedValue();
        }
        return this.values.getSuitedValue();
    }

    private int getAddValue(Card fromBottom, Card toTop) {
        if(toTop == null) {
            return -this.values.getSpaceValue();
        //} else if (! other.isKnown()) { Can't place on unkown card
        //    return -this.values.getUnknownValue();
        //} else if (card.getNum() + 1 != other.getNum()) { Can't place on card out of order
        //    return -this.values.getDisorderedValue();
        } else if (fromBottom.getSuit() != toTop.getSuit()) {
            return -this.values.getOrderedValue();
        }
        return -this.values.getSuitedValue();
    }

    /*
    * Input: Field field (A Field)
    * Output: A list of all possible moves for that field with calculated values
    */
    public ArrayList<Move> getAvailableMoves(MoveSet mSet) {
        Field field = mSet.getField();
        ArrayList<Move> moves = new ArrayList<Move>();
        Card toTop, fromTop, fromBottom;
        int depth, value, maxDepth;
        Boolean toNull;

        // Look at the fromTop card in each lane of the field
        for(int from = 0; from < 7; from++) {
            toNull = false;
            fromTop = field.get(from);
            if (fromTop == null || !fromTop.isKnown()) {
                continue;
            }
            fromBottom = fromTop.getBottom();
            maxDepth = fromBottom.getNum() - fromTop.getNum();
            // Find the max depth of possible move and the fromBottom card
            // Look through all other fromTop cards
            for (int to = 0; to < 7; to ++) {
                if (to == from) {
                    continue;
                }
                // Based on set values calculate value of move if possible
                value = 0;
                toTop = field.get(to);
                // Don't allow moving from one blank space to another because that's wasteful
                try {
                    if (toTop == null && fromBottom.getNext() != null && !toNull) {
                        toNull = true;
                        value += getRemoveValue(fromBottom);
                        value += getAddValue(fromBottom, toTop);
                        moves.add(new Move(maxDepth, from, to, value));
                    } else if (toTop != null && toTop.getNum() > fromTop.getNum() && toTop.getNum() <= fromBottom.getNum() + 1 && toTop.isKnown()) {
                        depth = toTop.getNum() - (fromTop.getNum() + 1);
                        //if(depth != maxDepth) continue;
                        Card newFromBottom = fromBottom;
                        if(depth < maxDepth) {
                            newFromBottom = fromTop;
                            for (int j = 0; j < depth; j++) {
                                newFromBottom = newFromBottom.getNext();
                            }
                        }
                        value += getRemoveValue(newFromBottom);
                        value += getAddValue(newFromBottom, toTop);
                        moves.add(new Move(depth, from, to, value));
                    }
                } catch (Exception e) {
                    System.out.println(field);
                    System.out.println("to: " + to);
                    System.out.println("from: " + from);
                    throw e;
                }
            }
        }
        return moves;
    }
}