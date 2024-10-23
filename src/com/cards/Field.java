package com.cards;

import java.util.ArrayList;

public class Field extends ArrayList<Card> {

    /**
     *
     */
    private static final long serialVersionUID = 5897313269777042217L;
    private int hashcode;
    private byte numComplete;
    private boolean isLineCompleted;

    public Field(int initialCapacity) {
        super(initialCapacity);
    }

    public Field(Field field, Move move) {
        super(field);
        Card toTop = this.get(move.getTo());
        if (toTop != null) {
            Card toBottom = toTop.getBottom();
            if(toTop != toBottom) {
                toBottom = new Card(toBottom, true);
                toTop = new Card(toTop, toBottom);
            } else {
                toTop = new Card(toTop, true);
            }
        }
        this.set(move.getTo(), toTop);

        Card fromTop = this.get(move.getFrom());
        if(move.getDepth() > 0) {
            Card fromBottom = fromTop.getBottom();
            int maxDepth = fromBottom.getNum() - fromTop.getNum();
            if(move.getDepth() != maxDepth) {
                fromBottom = fromTop;
                for (int i = 0; i < move.getDepth(); i++) {
                    fromBottom = fromBottom.getNext();
                }
            }
            fromBottom = new Card(fromBottom, true);
            fromTop = new Card(fromTop, fromBottom);
        } else {
            fromTop = new Card(fromTop, true);
        }
        this.set(move.getFrom(), fromTop);
        this.isLineCompleted = this.moveCard(move);
        this.hashcode = field.hashCode();
        this.numComplete = field.getNumComplete();
    }

    public Card put(int index, Card card) {
        if (card != null) {
            Card nextCard = this.get(index);
            card.setNext(nextCard);
            if (nextCard != null && card.isSuited()) {
                card.setBottom(nextCard.getBottom());
            }
        }
        return super.set(index, card);
    }

    public boolean canMove(Move move) {
        Card fromTop = this.get(move.getFrom());
        if (fromTop == null) {
            return false;
        }
        Card fromBottom = fromTop.getBottom();
        int maxDepth = fromBottom.getNum() - fromTop.getNum();
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
        Card fromBottom = fromTop.getBottom();
        if(move.getDepth() < fromBottom.getNum() - fromTop.getNum()) {
            fromBottom = fromTop;
            for (int i = 0; i < move.getDepth(); i++) {
                fromBottom = fromBottom.getNext();
            }
        }
        Card toTop = this.get(move.getTo());

        //Set the card on top as the card after last card removed
        this.set(move.getFrom(), fromBottom.getNext());

        //Set new bottom if placed on suited card
        fromBottom.setNext(toTop);
        if (toTop != null && fromTop.getSuit() == toTop.getSuit()) {
            fromTop.setBottom(toTop.getBottom());
        } else {
            fromTop.setBottom(fromBottom);
        }

        //Set new top card
        this.set(move.getTo(), fromTop);

        Boolean isComplete = false;
        if (this.complete(move.getTo())) {
            isComplete = true;
        }
        this.setHashCode();
        return isComplete;
    }

    public boolean doFlip(int i) {
        Card card = this.get(i);
        if (card != null && !card.isKnown()) {
            card.flip();
            return true;
        }
        return false;
    }

    public boolean complete(int i) {
        Card bottom = this.get(i).getBottom();
        if (bottom.getNum() - this.get(i).getNum() == 12) {
            this.set(i, bottom.getNext());
            this.numComplete ++;
            return true;
        }
        return false;
    }

    public boolean isLineComplete() {
        return this.isLineCompleted;
    }

    @Override
    public boolean equals(Object obj) {
        Field other = (Field) obj;
        Card card_1, card_2;
        for (int i = 0; i < 7; i++) {
            card_1 = this.get(i);
            card_2 = other.get(i);
            while (card_1 != card_2 && card_1 != null && card_2 != null && card_1.equals(card_2)) {
                card_1 = card_1.getBottom() != card_1 ? card_1.getBottom() : card_1.getNext();
                card_2 = card_2.getBottom() != card_2 ? card_2.getBottom() : card_2.getNext();
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
        StringBuilder fieldStr = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            fieldStr.append("|     ");
            fieldStr.append(i + 1);
            fieldStr.append("     |");
            toPrint[i] = this.get(i);
        }
        fieldStr.append("\n");
        ;
        for (int i = 0; i < 7; i++) {
            fieldStr.append("-------------");
        }
        boolean empty = false;
        while (!empty) {
            fieldStr.append("\n");
            empty = true;
            for (int i = 0; i < 7; i++) {
                if (toPrint[i] != null) {
                    fieldStr.append(toPrint[i].toString());
                    if(toPrint[i].isKnown() && toPrint[i].getBottom() != toPrint[i] && !toPrint[i].getBottom().equals(toPrint[i].getNext())) {
                        toPrint[i] = Card.getPackedCard(toPrint[i]);
                    } else {
                        toPrint[i] = toPrint[i].getNext();
                    }
                    empty = false;
                } else {
                    fieldStr.append("|           |");
                }
            }
        }
        return fieldStr.toString();
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