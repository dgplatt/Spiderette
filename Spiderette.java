import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;

public class Spiderette {
    public static void main (String[] args) {
        Game game = new Game();
        game.play();

    }
}

class Game {
    Deck deck;
    Card[] field; 
    Game() {
        this.field = new Card[7]; 
        this.deck = new Deck();
        this.Setup();
        }
    private void Setup() {
        Card current;
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j ++) {
                current = this.deck.start;
                this.deck.start = current.next;
                if(this.field[j] != null) {
                    current.next = this.field[j];
                } else {
                    current.next = null;
                }
                this.field[j] = current;
                current.lane = j;
            }
        }
        for (int i = 0; i < 7; i ++) {
            field[i].Flip();
        }
    }
    void Print_Field() {
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
        while (!empty(for_print)) {
            for (int i = 0; i < 7; i ++) {
                if (for_print[i] != null) {
                    for_print[i].Print_Card();
                    for_print[i] = for_print[i].next;
                }
                else {
                    System.out.print("|           |");
                }
            }
            System.out.println("");
        }
    }
    private boolean empty(Card[] for_print) {
        for (int i = 0; i < 7; i++) {
            if (for_print[i] != null) {
                return false;
            }
        }
        return true;
    }
    boolean Move(Card card, int to) {
        int from  = card.lane;
        Card top = this.field[from];
        if (this.field[to] != null && this.field[to].num != card.num + 1) {
            return false;
        }
        top = this.field[from];
        this.field[from] = card.next;
        card.next = this.field[to];
        this.field[to] = top;
        return true;
    }
    boolean Select (Card card) {
        int from = card.lane;
        Card top = this.field[from];
        if (top == card && !card.known) {
            card.Flip();
            return false;
        }
        while (top != card) {
            if(top.next == null || top.next.suit != top.suit || top.next.num != top.num + 1 || !top.next.known) {
                return false;
            }
        }
        return true;
    }
    void play() {
        System.out.println("Commands:");
        System.out.println("Quit --> stops the game");
        System.out.println("Select --> select a card");
        Scanner input = new Scanner(System.in);
        String comm = "";
        int num; Card card;
        while (true) {
            this.Print_Field();
            System.out.print("Enter Commands:  ");
            comm = input.nextLine();
            while(comm.equals("")){
                comm = input.nextLine();
            }
            if(comm.equals("Quit") || comm.equals("quit")) {
                break;
            } else {
                System.out.print("Lane?:  ");
                num = input.nextInt();
                card = this.field[num];
                System.out.print("Depth?:  ");
                num = input.nextInt();
                for(int i = 0; i < num; i++) {
                    card = card.next;
                }
                if (!this.Select(card)) { 
                    continue; 
                }
                System.out.print("Move y/n?:  ");
                while (! comm.equals("y") && ! comm.equals("n")) {
                    comm = input.nextLine();
                }
                if (comm.equals("n")) { 
                    continue; 
                }
                System.out.print("Which lane?:  ");
                num = input.nextInt();
                this.Move(card, num);
            }
        }
    }
}

class Card {
    int num;
    String suit;
    boolean known;
    Card next;
    int lane;
    Card(int num, String suit) {
        this.num = num;
        this.suit = suit;
        this.known = false;
        this.next = null;
        int lane = 7;
    }
    void Print_Card() {
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
    void Flip() {
        this.known = ! this.known;
    }
}

class Deck {
    Card[] deck;
    Card start;
    Deck() {
        this.deck = new Card[52];
        this.Make_Cards();
        this.Shuffle();
        this.Setup();
    }
    void Make_Cards() {
        for (int i = 0; i < 13; i ++) {
            this.deck[i*4] = new Card(i + 1, "Clbs");
            this.deck[i*4 + 1] = new Card(i + 1, "Dmds");
            this.deck[i*4 + 2] = new Card(i + 1, "Hrts");
            this.deck[i*4 + 3] = new Card(i + 1, "Spds");
        }
    }
    void Shuffle() {
        int[] placements  = new int[52];
        Random rnd = new Random();
        int key;
        for (int i = 0; i < 52; i++) {
            key = rnd.nextInt(52) + 1;
            for(int j = 0; j < i; j++){
                if (placements[j] == key){
                    key = rnd.nextInt(52) + 1;
                    j = -1;
                }
            }
            placements[i] = key;
        }
        Card[] suffled_deck = new Card[52];
        for (int i = 0; i < 52; i ++) {
            suffled_deck[i] = this.deck[placements[i] - 1];
        }
        this.deck = suffled_deck;
    }
    void Setup() {
        this.start = this.deck[0];
        for (int i = 0; i < 51; i++) {
            this.deck[i].next = this.deck[i+1];
        }
    }
    void Print_Deck() {
        for (int i = 0 ; i < 52; i ++) {
            this.deck[i].Print_Card();
        }
    }
}