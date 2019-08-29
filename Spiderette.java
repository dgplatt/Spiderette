import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import cards.*;

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
                current = this.deck.Start();
                this.deck.Start(current.Next());
                if(this.field[j] != null) {
                    current.Next(this.field[j]);
                } else {
                    current.Next(null);
                }
                this.field[j] = current;
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
                    for_print[i] = for_print[i].Next();
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
    boolean Move(Card card, int to, int from) {
        Card top = this.field[from];
        if (this.field[to] != null && this.field[to].Num() != card.Num() + 1) {
            return false;
        }
        top = this.field[from];
        this.field[from] = card.Next();
        card.Next(this.field[to]);
        this.field[to] = top;
        return true;
    }
    boolean Select (Card card, int from) {
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
    void new_game() {
        this.field = new Card[7]; 
        this.deck = new Deck();
        this.Setup();
    }
    void play() {
        System.out.println("Commands:");
        System.out.println("Quit --> stops the game");
        System.out.println("Select --> select a card");
        Scanner input = new Scanner(System.in);
        String comm = "";
        int lane; int depth; int move_to;
        Card card;
        while (true) {
            this.Print_Field();
            System.out.print("Enter Commands:  ");
            comm = input.nextLine();
            while(comm.equals("")){
                comm = input.nextLine();
            }
            if(comm.equals("Quit") || comm.equals("quit")) {
                break;
            } else if (comm.equals("New") || comm.equals("new")) {
                this.new_game();
            } else {
                while(true) {
                    try {
                        System.out.print("Lane?:  ");
                        lane = input.nextInt();
                        if (lane > 6 || lane < 0) {
                            System.out.println("Not a valid lane!");
                            continue;
                        }
                        break;
                    } catch(java.util.InputMismatchException e) { 
                        System.out.println("Not a valid lane!");
                        input.next();
                    }
                }
                card = this.field[lane];
                while(true) {
                    try {
                        System.out.print("Depth?:  ");
                        depth = input.nextInt();
                        for(int i = 0; i < depth; i++) {
                            card = card.Next();
                        }
                        break;
                    } catch(java.util.InputMismatchException | java.lang.NullPointerException e) { 
                        card = this.field[lane];
                        System.out.println("Not a valid depth!");
                    }
                }
                if (!this.Select(card, lane)) { 
                    System.out.println("You can't select that card");
                    continue; 
                }
                input.next();
                while (! comm.equals("y") && ! comm.equals("n")) {
                    System.out.print("Move y/n?:  ");
                    comm = input.nextLine();
                }
                if (comm.equals("n")) { 
                    continue; 
                }
                while(true) {
                    try {
                        System.out.print("Which lane?:  ");
                        move_to = input.nextInt();
                        if (move_to > 6 || move_to < 0) {
                            System.out.println("Not a valid lane!");
                            continue;
                        }
                        break;
                    } catch(java.util.InputMismatchException e) { 
                        System.out.println("Not a valid lane!");
                        input.next();
                    }
                }
                this.Move(card, move_to, lane);
            }
        }
    }
}
