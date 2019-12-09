package cards;
import cards.*;

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
            this.Top(i, new_card);
            while(old_card.Next() != null) {
                new_card.Next(new Card(old_card.Next()));
                old_card = old_card.Next();
                new_card = new_card.Next();
            }

        }
    }

    public Card lane(int i) {
        if (i < 0 || i > 6) {
            return null;
        }
        return this.field[i];
    }

    public boolean equals(Field other) {
        Card card_1;
        Card card_2;
        for (int i = 0; i < 7; i ++) {
            card_1 = this.lane(i);
            card_2 = other.lane(i);
            while (card_1 != null && card_2 != null && card_1.equal(card_2))  {
                card_1 = card_1.Next();
                card_2 = card_2.Next();
            }
            if(card_1 != null && card_2 != null) {
                return false;
            }
        }
        return true;
    }

    public void Top(int i, Card card) {
        if (card != null){
            card.Next(this.lane(i));
        }
        this.field[i] = card;
    }

    public boolean complete(int i) {
        if (this.max_depth(i) == 12) {
            Card card = this.lane(i);
            for (int j = 0; j < 12; j ++) {
                card = card.Next();
            }
            this.field[i] = card.Next();
            return true;
        }
        return false;
    }

    public void print() {
        System.out.print(this.to_String());
    }

    public String to_String() {
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
                    string_field += for_print[i].to_String();
                    for_print[i] = for_print[i].Next();
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

    public boolean Can_Move(Move move){
        int max_depth = this.max_depth(move.from());
        if (move.depth() > max_depth) {
            return false;
        }
        Card from_top = this.field[move.from()];
        Card to_top = this.field[move.to()];
        if (from_top == null) {
            return false;
        }
        if (to_top != null && !(to_top.Num() == from_top.Num() + move.depth() + 1 && to_top.Known())) {
            return false;
        }
        return true;
    }

    public boolean Move_Card(Move move) {
        if (! this.Can_Move(move)) {
            return false;
        }
        Card from_top = this.field[move.from()];
        Card to_top = this.field[move.to()];
        Card card = from_top;
        for (int i = 0; i < move.depth(); i ++ ) {
            card = card.Next();
        }
        this.field[move.from()] = card.Next();
        card.Next(to_top);
        this.field[move.to()] = from_top;
        return this.complete(move.to());
    }
    
    public boolean do_flip (int i) {
        Card card = this.lane(i);
        if (card != null && !card.Known()) {
            card.Flip();
            return true;
        }
        return false;
    }

    public int max_depth(int i) {
        Card card = this.lane(i);
        int depth = 0;
        while(card != null && card.ordered()) {
            card = card.Next();
            depth ++;
        }
        return depth;
    }

    
}