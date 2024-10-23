package com.auto;
import com.cards.*;
import java.util.*;
public class MoveSet {
    private Move move;
    private int value;
    private Field field;
    private MoveSet oldMoveSet;

    public MoveSet(Field field) {
        this.move = null;
        this.value = 0;
        this.field = field;
        this.oldMoveSet = null;
    }

    public MoveSet(MoveSet other, Move move) {
        this.oldMoveSet = other;
        this.field = new Field(other.field, move);
        this.move = move;
        this.value = other.getValue() + move.getValue();
    }

    public Move getMove() {
        return this.move;
    }

    public ArrayList<Move> getMoves() {
        if(oldMoveSet == null) {
            return new ArrayList<Move>();
        }
        ArrayList<Move> moves = oldMoveSet.getMoves();
        moves.add(this.move);
        return moves;
    }

    public int getValue(){
        return this.value;
    }
    
    public void addValue(int val){
        this.value += val;
    }

    public Field getField(){
        return this.field;
    }

    public boolean getComplete() {
        return this.field.isLineComplete();
    }

    @Override
    public String toString() {
        if(this.oldMoveSet == null) {
            return this.move + " ||  Value = " + this.value + "\n";
        }
        return this.oldMoveSet.toString() + this.move + " ||  Value = " + this.value + "\n";
    }

    @Override
    public boolean equals(Object other) {
        MoveSet obj = (MoveSet) other;
        return obj.getField().equals(this.field);
    }
    
    @Override
    public int hashCode() {
        return this.field.hashCode();
    }

}