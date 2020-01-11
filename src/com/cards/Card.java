package com.cards;

public class Card {
    int num;
    String suit;
    boolean known;
    Card next;
    
    public Card(int num, String suit) {
        this.num = num;
        this.suit = suit;
        this.known = false;
        this.next = null;
    }
    
    public Card(Card card) {
        this.num = card.getNum();
        this.suit = card.getSuit();
        this.known = card.isKnown();
        this.next = null;
    }

    public void print() {
        System.out.print(this.toString());
    }

    @Override
    public String toString() {
        String string_card = "";
        if (this.known == false) {
            string_card += "|  Unknown  |";
        } else if (this.num == 1) {
            string_card += "| A of " + this.suit + " |";
        } else if (this.num < 10) {
            string_card += "| " + this.num + " of " + this.suit + " |";
        } else if (this.num == 10) {
            string_card += "| X of " + this.suit + " |";
        } else if (this.num == 11) {
            string_card += "| J of " + this.suit + " |";
        } else if (this.num == 12) {
            string_card += "| Q of " + this.suit + " |";
        } else if (this.num == 13) {
            string_card += "| K of " + this.suit + " |";
        }
        return string_card;
    }

    public boolean ordered() {
        return (this.getNext() != null && this.getNext().getSuit().equals(this.getSuit()) && this.getNext().getNum() == this.getNum() + 1 && this.getNext().isKnown());
    }

    public boolean equal(Card other) {
        boolean same = true;
        if(this.num != other.getNum()) {
            same = false;
        }
        if (! this.suit.equals(other.getSuit())){
            same = false;
        }
        if (this.known != other.isKnown()){
            same = false;
        }
        return same;
    }
    public int getNum() {
        return this.num;
    }
    public String getSuit() {
        return this.suit;
    }
    public Card getNext() {
        return this.next;
    }
    public void setNext(Card next) {
        this.next = next;
    }
    public boolean isKnown() {
        return this.known;
    }
    public void flip() {
        this.known = true;
    }
}