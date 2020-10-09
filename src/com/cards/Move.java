package com.cards;
public class Move {
    private int depth, from, to, value;
    
    public Move (int depth, int from, int to, int value) {
        this.depth = depth;
        this.from = from;
        this.to = to;
        this.value = value;
    }
    public Move (int depth, int from, int to) {
        this.depth = depth;
        this.from = from;
        this.to = to;
        this.value = 0;
    }
    public int getDepth() {
        return this.depth;
    }
    public int getFrom() {
        return this.from;
    }
    public int getTo() {
        return this.to;
    }
    public int getValue() {
        return this.value;
    }
    @Override
    public String toString() {
        return "|  From: " + (this.from + 1) + "  ||  Depth: " + this.depth +   "  ||  To: " + (this.to + 1) + "  ||  Value : " + this.value + "  |";
    }
}