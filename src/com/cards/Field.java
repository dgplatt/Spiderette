package com.cards;

public class Field {
    private Card[] field;

    public Field() {
        this.field = new Card[7];
    }
    
    public Field(Field field) {
        this.field = new Card[7];
        for (int i = 0; i < 7; i ++) {
            if (field.get(i) == null) {
                continue;
            }
            this.field[i] = new Card(field.get(i));
        }
    }

    public Card get(int index) {
        return this.field[index];
    }

    public void set(int index, Card card) {
        if (card != null){
            card.setNext(this.get(index));
        }
        this.field[index] = card;
    }

    public boolean canMove(Move move){
        int maxDepth = this.maxDepth(move.from());
        if (move.depth() > maxDepth) {
            return false;
        }
        Card fromTop = this.field[move.from()];
        Card toTop = this.field[move.to()];
        if (fromTop == null) {
            return false;
        }
        if (toTop != null && !(toTop.getNum() == fromTop.getNum() + move.depth() + 1 && toTop.isKnown())) {
            return false;
        }
        return true;
    }

    public boolean moveCard(Move move) {
        Card fromTop = this.field[move.from()];
        Card toTop = this.field[move.to()];
        Card card = fromTop;
        for (int i = 0; i < move.depth(); i ++ ) {
            card = card.getNext();
        }
        this.field[move.from()] = card.getNext();
        card.setNext(toTop);
        this.field[move.to()] = fromTop;
        if(card.isOrdered()) {
            return this.complete(move.to());
        }
        return false;
    }

    public boolean doFlip (int i) {
        Card card = this.get(i);
        if (card != null && !card.isKnown()) {
            card.flip();
            return true;
        }
        return false;
    }

    public int maxDepth(int i) {
        Card card = this.get(i);
        int depth = 0;
        while(card != null && card.isOrdered()) {
            card = card.getNext();
            depth ++;
        }
        return depth;
    }

    public boolean complete(int i) {
        if (this.maxDepth(i) == 12) {
            Card card = this.get(i);
            for (int j = 0; j < 12; j ++) {
                card = card.getNext();
            }
            this.field[i] = card.getNext();
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        Field other = (Field) obj;
        Card card_1, card_2;
        for (int i = 0; i < 7; i ++) {
            card_1 = this.get(i);
            card_2 = other.get(i);
            while (card_1 != null && card_2 != null && card_1.equals(card_2))  {
                card_1 = card_1.getNext();
                card_2 = card_2.getNext();
            }
            if(card_1 != null && card_2 != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Card[] toPrint = new Card[7];
        String fieldStr = "";
        for (int i = 0; i < 7; i ++) {
            fieldStr += "|     " + (i + 1) + "     |";
            toPrint[i] = this.field[i];
        }
        fieldStr += "\n";;
        for (int i = 0; i < 7; i ++) {
            fieldStr += "-------------";
        }
        fieldStr += "\n";
        boolean empty = false;
        while (!empty) {
            empty = true;
            for (int i = 0; i < 7; i ++) {
                if (toPrint[i] != null) {
                    fieldStr += toPrint[i].toString();
                    toPrint[i] = toPrint[i].getNext();
                    empty = false;
                }
                else {
                    fieldStr += "|           |";
                }
            }
            fieldStr += "\n";
        }
        return fieldStr;
    }

    @Override
    public int hashCode() {
        String s = "";
        for(Card c: this.field) {
            if(c == null) {
                s += 0;
            } else {
                s += c.getNum()%10;
            }
        }
        return Integer.parseInt(s);
    }
}