package com.cards;

import java.util.ArrayList;

public class Field extends ArrayList<Card> {

    /**
     *
     */
    private static final long serialVersionUID = 5897313269777042217L;
    private int hashcode;
    private byte numComplete;

    public Field(int initialCapacity) {
        super(initialCapacity);
    }

    public Field(Field field, Move move) {
        super(field);
        for (int i : new int[] { move.getTo(), move.getFrom() }) {
            if (this.get(i) != null) {
                Card bottom = this.get(i).getBottom();
                if (bottom == null) {
                    this.set(i, new Card(this.get(i)));
                } else {
                    bottom = new Card(bottom);
                    this.set(i, new Card(this.get(i), bottom));
                }
            }
        }
        this.hashcode = field.hashCode();
        this.numComplete = field.getNumComplete();
    }

    public Card put(int index, Card card) {
        if (card != null) {
            card.setNext(this.get(index));
            if (this.get(index) != null) {
                Card bottom = this.maxDepth(index);
                if (card.isOrdered()) {
                    card.setBottom(bottom);
                }
            }
        }
        return super.set(index, card);
    }

    public boolean canMove(Move move) {
        Card fromTop = this.get(move.getFrom());
        if (fromTop == null) {
            return false;
        }
        Card bottom = this.maxDepth(move.getFrom());
        int maxDepth = bottom.getNum() - fromTop.getNum();
        if (move.getDepth() > maxDepth) {
            return false;
        }
        Card toTop = this.get(move.getTo());
        if (toTop != null && !(toTop.getNum() == fromTop.getNum() + move.getDepth() + 1 && toTop.isKnown())) {
            return false;
        }
        return true;
    }

    public boolean moveCard(Move move) {
        Card fromTop = this.get(move.getFrom());
        Card fromBottom = this.maxDepth(move.getFrom());
        Card toTop = this.get(move.getTo());
        if (toTop != null) {
            Card toBottom = this.maxDepth(move.getTo());
            if (fromTop.getSuit() == toTop.getSuit()) {
                fromTop.setBottom(toBottom);
            } else if (move.getDepth() != fromBottom.getNum() - fromTop.getNum()) {
                Card card = fromTop;
                for (int i = 0; i < move.getDepth(); i++) {
                    card = card.getNext();
                }
                fromTop.setBottom(card);
                card.getNext().setBottom(fromBottom);
                fromBottom = card;
                fromBottom.setBottom(null);
            }
        }
        this.set(move.getFrom(), fromBottom.getNext());
        fromBottom.setNext(toTop);
        this.set(move.getTo(), fromTop);
        Boolean toReturn = false;
        if (this.complete(move.getTo())) {
            toReturn = true;
        }
        this.setHashCode();
        return toReturn;
    }

    public boolean doFlip(int i) {
        Card card = this.get(i);
        if (card != null && !card.isKnown()) {
            card.flip();
            return true;
        }
        return false;
    }

    public Card maxDepth(int i) {
        Card card = this.get(i);
        return card.getBottom() != null ? card.getBottom() : card;
    }

    public boolean complete(int i) {
        Card bottom = this.maxDepth(i);
        if (bottom.getNum() - this.get(i).getNum() == 12) {
            this.set(i, bottom.getNext());
            this.numComplete ++;
            return true;
        }
        return false;
    }

    

    @Override
    public boolean equals(Object obj) {
        Field other = (Field) obj;
        Card card_1, card_2;
        for (int i = 0; i < 7; i++) {
            card_1 = this.get(i);
            card_2 = other.get(i);
            while (card_1 != card_2 && card_1 != null && card_2 != null && card_1.equals(card_2)) {
                card_1 = card_1.getBottom() != null ? card_1.getBottom() : card_1.getNext();
                card_2 = card_2.getBottom() != null ? card_2.getBottom() : card_2.getNext();
            }
            if (card_1 != card_2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Card[] toPrint = new Card[7];
        String fieldStr = "";
        for (int i = 0; i < 7; i++) {
            fieldStr += "|     " + (i + 1) + "     |";
            toPrint[i] = this.get(i);
        }
        fieldStr += "\n";
        ;
        for (int i = 0; i < 7; i++) {
            fieldStr += "-------------";
        }
        boolean empty = false;
        while (!empty) {
            fieldStr += "\n";
            empty = true;
            for (int i = 0; i < 7; i++) {
                if (toPrint[i] != null) {
                    fieldStr += toPrint[i].toString();
                    toPrint[i] = toPrint[i].getNext();
                    empty = false;
                } else {
                    fieldStr += "|           |";
                }
            }
        }
        return fieldStr;
    }

    public void setHashCode() {
        this.hashcode = super.hashCode();
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }

    public byte getNumComplete() {
        return numComplete;
    }

    public boolean isDone() {
        return numComplete == 4;
    }
}