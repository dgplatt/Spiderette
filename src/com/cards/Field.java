package com.cards;

public class Field {
    Card[] field;
    public Field() {
        this.field = new Card[7];
    }
    
    public Field(Field field) {
        this.field = new Card[7];
        Card new_card;
        Card old_card;
        for (int i = 0; i < 7; i ++) {
            if (field.lane(i) == null) {
                continue;
            }
            old_card = field.lane(i);
            new_card = new Card(old_card);
            this.top(i, new_card);
            while(old_card.getNext() != null) {
                new_card.setNext(new Card(old_card.getNext()));
                old_card = old_card.getNext();
                new_card = new_card.getNext();
            }
        }
    }

    public Card[] getField() {
        return field;
    }

    public Card lane(int i) {
        if (i < 0 || i > 6) {
            return null;
        }
        return this.field[i];
    }

    public void top(int i, Card card) {
        if (card != null){
            card.setNext(this.lane(i));
        }
        this.field[i] = card;
    }
    public boolean canMove(Move move){
        int max_depth = this.maxDepth(move.from());
        if (move.depth() > max_depth) {
            return false;
        }
        Card from_top = this.field[move.from()];
        Card to_top = this.field[move.to()];
        if (from_top == null) {
            return false;
        }
        if (to_top != null && !(to_top.getNum() == from_top.getNum() + move.depth() + 1 && to_top.isKnown())) {
            return false;
        }
        return true;
    }

    public boolean moveCard(Move move) {
        if (! this.canMove(move)) {
            return false;
        }
        Card from_top = this.field[move.from()];
        Card to_top = this.field[move.to()];
        Card card = from_top;
        for (int i = 0; i < move.depth(); i ++ ) {
            card = card.getNext();
        }
        this.field[move.from()] = card.getNext();
        card.setNext(to_top);
        this.field[move.to()] = from_top;
        return this.complete(move.to());
    }
    public boolean doFlip (int i) {
        Card card = this.lane(i);
        if (card != null && !card.isKnown()) {
            card.flip();
            return true;
        }
        return false;
    }

    public int maxDepth(int i) {
        Card card = this.lane(i);
        int depth = 0;
        while(card != null && card.ordered()) {
            card = card.getNext();
            depth ++;
        }
        return depth;
    }
    public boolean complete(int i) {
        if (this.maxDepth(i) == 12) {
            Card card = this.lane(i);
            for (int j = 0; j < 12; j ++) {
                card = card.getNext();
            }
            this.field[i] = card.getNext();
            return true;
        }
        return false;
    }

    public void print() {
        System.out.print(this.toString());
    }

    @Override
    public boolean equals(Object obj) {
        Field other = (Field) obj;
        Card card_1;
        Card card_2;
        for (int i = 0; i < 7; i ++) {
            card_1 = this.lane(i);
            card_2 = other.lane(i);
            while (card_1 != null && card_2 != null && card_1.equal(card_2))  {
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
        Card[] for_print = new Card[7];
        String string_field = "";
        for (int i = 0; i < 7; i ++) {
            string_field += "|     " + i + "     |";
            for_print[i] = this.field[i];
        }
        string_field += "\n";;
        for (int i = 0; i < 7; i ++) {
            string_field += "-------------";
        }
        string_field += "\n";
        boolean empty = false;
        while (!empty) {
            empty = true;
            for (int i = 0; i < 7; i ++) {
                if (for_print[i] != null) {
                    string_field += for_print[i].toString();
                    for_print[i] = for_print[i].getNext();
                    empty = false;
                }
                else {
                    string_field += "|           |";
                }
            }
            string_field += "\n";
        }
        return string_field;
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