package com.auto;
import com.cards.*;
import java.util.*;
public class MoveSet {
    ArrayList<Move> moves;
    int value;
    Field field;
    MoveSet (Field field) {
        this.field = new Field(field);
        this.moves = new ArrayList<Move>();
        this.value = 0;
    }

    MoveSet (MoveSet other) {
        this.field = new Field(other.field);
        this.moves = new ArrayList<Move>();
        for(Move move: other.moves){
            this.moves.add(move);
        }
        this.value = other.getValue();
    }

    boolean add (Move move) {
        this.moves.add(move);
        this.value += move.value();
        return this.field.moveCard(move);
    }
    @Override
    public boolean equals(Object other) {
        MoveSet obj = (MoveSet) other;
        return (obj.getField().equals(this.field));
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

    public void print() {
        for(Move move: this.moves){
            move.print();
        }
        System.out.println(" ||  Value = " + this.value);
    }
    @Override
    public int hashCode() {
        return this.field.hashCode();
    }

}