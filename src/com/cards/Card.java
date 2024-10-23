package com.cards;

public class Card {
    private byte num;
    private byte suit;
    private boolean known;
    private Card next;
    private Card bottom;
    private int hashcode;
    
    public Card(int num, int suit) {
        this.num = (byte) num;
        this.suit = (byte) suit;
        this.known = false;
        this.next = null;
        this.bottom = this;
    }

    public Card(Card card, boolean isBottom) {
        this.num = card.getNum();
        this.suit = card.getSuit();
        this.known = card.isKnown();
        this.next = card.getNext();
        if(isBottom) {
            this.bottom = this;
        } else  {
            this.bottom = card.getBottom();
        }
        
    }

    public Card(Card card, Card bottomCard) {
        this.num = card.getNum();
        this.suit = card.getSuit();
        this.known = card.isKnown();
        this.bottom = bottomCard;

        Card nextCard = card.getNext();
        if(nextCard != null) {
            if (nextCard.equals(bottomCard)) {
                this.next = bottomCard;
            }  else {
                this.next = new Card(nextCard, bottomCard);
            }
        }
        this.setHashCode();
    }

    public static Card getPackedCard(Card card) {
        Card packedCard = new Card(0, card.getSuit());
        packedCard.known = true;
        packedCard.next = card.getBottom();
        return packedCard;
    }

    @Override
    public String toString() {
        String cardStr = "";
        final String[] suits =  new String[] {"Clbs", "Dmds", "Hrts", "Spds"};

        String suit = suits[this.suit];
        if (this.known == false) {
            cardStr = "|  Unknown  |";
        } else if(this.num == 0) {
            cardStr = "|    ...    |";
        } else if (this.num == 1) {
            cardStr = "| A of " + suit + " |";
        } else if (this.num < 10) {
            cardStr = "| " + this.num + " of " + suit + " |";
        } else if (this.num == 10) {
            cardStr = "| X of " + suit + " |";
        } else if (this.num == 11) {
            cardStr = "| J of " + suit + " |";
        } else if (this.num == 12) {
            cardStr = "| Q of " + suit + " |";
        } else if (this.num == 13) {
            cardStr = "| K of " + suit + " |";
        }
        return cardStr;
    }

    public boolean isSuited() {
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
        this.bottom = bottom;
        if(this != bottom && this.next != null) {
            this.next.setBottom(bottom);
        }
    }

    @Override
    public boolean equals(Object obj) {
        Card other = (Card) obj;
        return other != null && this.num == other.num && this.suit == other.suit;
    }

    public void setHashCode() {
        int result;
        if(num > 9) {
            result = (num - 10) * 10 + 5 + suit;
        } else {
            result = num * 10 + suit;
        }
        result = bottom == null ? result * 100 + bottom.hashCode(): result;
        this.hashcode = result;
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }
}