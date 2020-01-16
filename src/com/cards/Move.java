package com.cards;
public class Move {
    int depth, from, to, value;
    
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
    public int depth() {
        return this.depth;
    }
    public int from() {
        return this.from;
    }
    public int to() {
        return this.to;
    }
    public int value() {
        return this.value;
    }
    public String to_String() {
        return ("  ||  From: " + (this.from + 1) + "  ||  To: " + (this.to + 1) + "  ||  Depth: " + this.depth +   "  ||  Value : " + this.value +"\n");
    }
    public void print(){
        System.out.print(this.to_String());
    }
}