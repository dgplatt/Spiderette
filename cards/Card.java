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
    public void Print_Card() {
        if (this.known == false) {
            System.out.print("|  Unknown  |");
        } else if (this.num == 1) {
            System.out.print("| A of " + this.suit + " |");
        } else if (this.num < 10) {
            System.out.print("| " + this.num + " of " + this.suit + " |");
        } else if (this.num == 10) {
            System.out.print("| X of " + this.suit + " |");
        } else if (this.num == 11) {
            System.out.print("| J of " + this.suit + " |");
        } else if (this.num == 12) {
            System.out.print("| Q of " + this.suit + " |");
        } else if (this.num == 13) {
            System.out.print("| K of " + this.suit + " |");
        }
    }
    public int Num() {
        return this.num;
    }
    public String Suit() {
        return this.suit;
    }
    public Card Next() {
        return this.next;
    }
    public void Next(Card next) {
        this.next = next;
    }
    public boolean Known() {
        return this.known;
    }
    public void Flip() {
        this.known = ! this.known;
    }
}