package com.cards;

public class Card {
    private int num;
    private String suit;
    private boolean known;
    private Card next;
    
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
        if (card.getNext() != null) {
            this.next = new Card(card.getNext());
        } else {
            this.next = null;
        }
    }

    @Override
    public String toString() {
        String cardStr = "";
        if (this.known == false) {
            cardStr += "|  Unknown  |";
        } else if (this.num == 1) {
            cardStr += "| A of " + this.suit + " |";
        } else if (this.num < 10) {
            cardStr += "| " + this.num + " of " + this.suit + " |";
        } else if (this.num == 10) {
            cardStr += "| X of " + this.suit + " |";
        } else if (this.num == 11) {
            cardStr += "| J of " + this.suit + " |";
        } else if (this.num == 12) {
            cardStr += "| Q of " + this.suit + " |";
        } else if (this.num == 13) {
            cardStr += "| K of " + this.suit + " |";
        }
        return cardStr;
    }

    public boolean isOrdered() {
        return (this.getNext() != null 
                && this.getNext().getSuit().equals(this.getSuit()) 
                && this.getNext().getNum() == this.getNum() + 1 
                && this.getNext().isKnown());
    }

    @Override
    public boolean equals(Object obj) {
        Card other = (Card) obj;
        return this.num == other.getNum() && this.suit.equals(other.getSuit());
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