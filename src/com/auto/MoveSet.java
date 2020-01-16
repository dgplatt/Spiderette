package com.auto;
import com.cards.*;
import java.util.*;
public class MoveSet {
    private ArrayList<Move> moves;
    private int value;
    private Field field;

    MoveSet(Field field) {
        this.field = new Field(field);
        this.moves = new ArrayList<Move>();
        this.value = 0;
    }

    MoveSet(MoveSet other) {
        this.field = new Field(other.field);
        this.moves = new ArrayList<Move>();
        this.moves.addAll(other.getMoves());
        this.value = other.getValue();
    }

    boolean add(Move move) {
        this.moves.add(move);
        this.value += move.value();
        return this.field.moveCard(move);
    }

    int getValue(){
        return this.value;
    }
    
    void addValue(int val){
        this.value += val;
    }

    public Field getField(){
        return this.field;
    }

    public ArrayList<Move> getMoves() {
        return this.moves;
    }

    @Override
    public String toString() {
        String moveSetStr = "";
        for(Move move: this.moves){
            moveSetStr += move.toString();
        }
        return moveSetStr + " ||  Value = " + this.value + "\n";
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