package com.cards;

public class Card {
    private byte num;
    private byte suit;
    private boolean known;
    private Card next;
    private Card bottom;
    
    public Card(int num, int suit) {
        this.num = (byte) num;
        this.suit = (byte) suit;
        this.known = false;
        this.next = null;
    }

    public Card(Card card) {
        this.num = card.getNum();
        this.suit = card.getSuit();
        this.known = card.isKnown();
        this.next = card.getNext();
        this.bottom = card.getBottom();
    }

    public Card(Card card, Card bottom) {
        this.num = card.getNum();
        this.suit = card.getSuit();
        this.known = card.isKnown();
        Card next = card.getNext();
        this.bottom = bottom;
        if(next != null) {
            if (next.equals(bottom)) {
                this.next = bottom;
            }  else {
                this.next = new Card(next, this.bottom);
            }
        }
    }

    @Override
    public String toString() {
        String cardStr = "";
        final String[] suits =  new String[] {"Clbs", "Dmds", "Hrts", "Spds"};

        String suit = suits[this.suit];
        if (this.known == false) {
            cardStr += "|  Unknown  |";
        } else if (this.num == 1) {
            cardStr += "| A of " + suit + " |";
        } else if (this.num < 10) {
            cardStr += "| " + this.num + " of " + suit + " |";
        } else if (this.num == 10) {
            cardStr += "| X of " + suit + " |";
        } else if (this.num == 11) {
            cardStr += "| J of " + suit + " |";
        } else if (this.num == 12) {
            cardStr += "| Q of " + suit + " |";
        } else if (this.num == 13) {
            cardStr += "| K of " + suit + " |";
        }
        return cardStr;
    }

    public boolean isOrdered() {
        return (this.getNext() != null 
                && this.getNext().getSuit() == this.getSuit() 
                && this.getNext().getNum() == this.getNum() + 1 
                && this.getNext().isKnown());
    }

    public byte getNum() {
        return this.num;
    }

    public byte getSuit() {
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

    public Card getBottom() {
        return bottom;
    }

    public void setBottom(Card bottom) {
        this.bottom = this != bottom ? bottom : null;
    }

    @Override
    public boolean equals(Object obj) {
        Card other = (Card) obj;
        return this.num == other.num && this.suit == other.suit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bottom == null) ? 0 : bottom.hashCode());
        result = prime * result + num;
        result = prime * result + suit;
        return result;
    }
}