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
        boolean same = true;
        for (int i = 0; i < 7; i ++) {
            card_1 = this.field[i];
            card_2 = other.lane(i);
            while (card_1 != null && card_2 != null && card_1.equal(card_2))  {
                card_1 = card_1.Next();
                card_2 = card_2.Next();
            }
            if(card_1 != null && card_2 != null) {
                same  = false;
                break;
            }
        }
        return same;
    }

    public void Top(int lane, Card card) {
        if (card != null){
            card.Next(this.field[lane]);
        }
        this.field[lane] = card;
    }
    public void Remove(int lane, Card card) {
        this.field[lane] = card.next;
        if(card.Next() != null)  {
            card.Next().Flip();
        }
    }
    public void Remove(int lane) {
        if (this.complete(lane)) {
            this.Remove(lane, this.Bottem_Card(lane));
        }

    }

    public void Print_Field() {
        Card[] for_print = new Card[7];
        for (int i = 0; i < 7; i ++) {
            System.out.print("|     " + i + "     |");
            for_print[i] = this.field[i];
        }
        System.out.println("");
        for (int i = 0; i < 7; i ++) {
            System.out.print("-------------");
        }
        System.out.println("");
        boolean empty = false;
        while (!empty) {
            empty = true;
            for (int i = 0; i < 7; i ++) {
                if (for_print[i] != null) {
                    for_print[i].Print_Card();
                    for_print[i] = for_print[i].Next();
                    empty = false;
                }
                else {
                    System.out.print("|           |");
                }
            }
            System.out.println("");
        }
    }

    public boolean Move(Card card, int to, int from) {
        Card top = this.field[from];
        Card old_top = this.field[to];
        if (old_top != null && !(old_top.Num() == card.Num() + 1 && old_top.Known())) {
            return false;
        }
        this.field[from] = card.Next();
        card.Next(old_top);
        this.field[to] = top;
        return true;
    }
    
    public boolean Select (Card card, int from) {
        Card top = this.field[from];
        if (top == card && !card.Known()) {
            card.Flip();
            return false;
        }
        while (top != card) {
            if(top.Next() == null || top.Next().Suit() != top.Suit() || top.Next().Num() != top.Num() + 1 || !top.Next().Known()) {
                return false;
            }
            top = top.Next();
        }
        return true;
    }

    public Card Bottem_Card(int i) {
        Card card = this.field[i];
        while(card != null && card.Next() != null && card.Next().Suit() == card.Suit() && card.Next().Num() == card.Num() + 1 && card.Next().Known()) {
            card = card.Next();
        }
        return card;
    }
    public boolean complete(int i) {
        if (this.lane(i) == null) {
            return false;
        }
        return (this.lane(i).Num() == 1 && this.Bottem_Card(i).Num() == 13);
    }
    
}