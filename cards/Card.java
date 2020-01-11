package cards;

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
        this.suit = card.Suit();
        this.known = card.Known();
        this.next = null;
    }

    public void print() {
        System.out.print(this.to_String());
    }

    public String to_String() {
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
        return (this.getNext() != null && this.getNext().Suit().equals(this.Suit()) && this.getNext().getNum() == this.getNum() + 1 && this.getNext().Known());
    }

    public boolean equal(Card other) {
        boolean same = true;
        if(this.num != other.getNum()) {
            same = false;
        }
        if (! this.suit.equals(other.Suit())){
            same = false;
        }
        if (this.known != other.Known()){
            same = false;
        }
        return same;
    }
    public int getNum() {
        return this.num;
    }
    public String Suit() {
        return this.suit;
    }
    public Card getNext() {
        return this.next;
    }
    public void setNext(Card next) {
        this.next = next;
    }
    public boolean Known() {
        return this.known;
    }
    public void Flip() {
        this.known = true;
    }
}