import java.util.*;
import cards.*;
import auto.Actions;
import auto.*;

public class Spiderette {
    Deck deck;
    Field field;
    int done;

    public static void main (String[] args) {
        Spiderette game = new Spiderette();
        game.play();
    }

    Spiderette() {
        this.deck = new Deck();
        this.field = new Field();
        this.Setup();
        this.done = 0;
    }

    void play() {
        System.out.println("Commands:");
        System.out.println("Quit --> stops the game");
        System.out.println("New --> start new game");
        System.out.println("Deal --> deal the top of the deck");
        System.out.println("[0-6] --> select a lane");
        Scanner input = new Scanner(System.in);
        String comm = "";
        int lane; int depth; int move_to;
        Card card;
        boolean auto = false;
        System.out.print("Auto (y/n)?:  ");
        while (! comm.equals("y") && ! comm.equals("n")) {
            comm = input.nextLine();
        }
        if (comm.equals("y")) {
            auto = true;
        }
        while (true) {
            Auto_Solver solver = new Auto_Solver(this.field);
            this.field.Print_Field();
            this.deck.Print_Deck();
            solver.make_next_actions();
            //for (Actions act : solver.acts()) {
            //    act.print();
            //}
            Actions best_act = solver.Max();
            if (best_act == null) {
                if(this.Deal()){
                    continue;
                }
                System.out.println("*****  GameOver  ******");
                done = 4;
                break;
            }
            best_act.print();
            int[][] the_moves = best_act.make_moves();
            for(int[] move: the_moves){
                Card temp = this.field.lane(move[1]);
                for (int j = 0; j < move[0]; j++) {
                    temp = temp.Next();
                }
                this.field.Move(temp, move[2], move[1]);
                if( this.field.lane(move[1])!= null && ! this.field.lane(move[1]).Known()) {
                    this.field.lane(move[1]).Flip();
                    break;
                }
                Card bottem = this.field.Bottem_Card(move[2]);
                if (bottem.Num() == 13 && this.field.lane(move[2]).Num() == 1) {
                    done++;
                    System.out.println("*****  Yay!!  ******");
                    this.field.Remove(move[2], bottem);
                    bottem.Next(null);
                }
            }
            if (done == 4) {
                System.out.println("*****  WINNER!!  ******");
                break;
            }
        }
        while (true && done != 4) {
            this.field.Print_Field();
            System.out.print("Enter Commands:  ");
            comm = input.nextLine();
            while(comm.equals("")){
                comm = input.nextLine();
            }
            if(comm.equals("Quit") || comm.equals("quit")) {
                break;
            } else if (comm.equals("New") || comm.equals("new")) {
                this.new_game();
            } else if (comm.equals("Deal") || comm.equals("deal")){
                this.Deal();
            } else if (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '7') {
                lane = Integer.parseInt(comm);
                card = this.field.lane(lane);
                if (card == null) {
                    System.out.println("No card there");
                    continue;
                }
                Card bottem = this.field.Bottem_Card(lane);
                if (bottem != card) {
                    System.out.print("Max Depth y/n?:  ");
                    while (! comm.equals("y") && ! comm.equals("n")) {
                        comm = input.nextLine();
                    }
                    if (comm.equals("n")) { 
                        while(! (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '7')) {
                            System.out.print("Depth?:  ");
                            comm = input.nextLine();
                        }
                        depth = Integer.parseInt(comm);
                        for(int i = 0; i < depth; i++) {
                            card = card.Next();
                        }
                    } else {
                        card = bottem;
                    }
                }
                if (! this.field.Select(card, lane)) { 
                    continue; 
                }
                do {
                    System.out.print("Where to?:  ");
                    comm = input.nextLine();
                } while (! (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '7'));
                move_to = Integer.parseInt(comm);
                if (this.field.Move(card, move_to, lane)) {
                    bottem = this.field.Bottem_Card(move_to);
                    if (bottem.Num() == 13 && this.field.lane(move_to).Num() == 1) {
                        done++;
                        this.field.Top(move_to, bottem.Next());
                        bottem.Next(null);
                    }
                }
                if (done == 4) {
                    System.out.print("*****  WINNER!!  ******");
                    break;
                }
            }
        }
    }

    private void Setup() {
        Card current;
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j ++) {
                this.deck.use();
                current = this.deck.Start();
                this.deck.Start(current.Next());
                if(this.field.lane(j) != null) {
                    current.Next(this.field.lane(j));
                } else {
                    current.Next(null);
                }
                this.field.Top(j,current);
            }
        }
        for (int i = 0; i < 7; i ++) {
            this.field.lane(i).Flip();
        }
    }

    void new_game() {
        this.field = new Field(); 
        this.deck = new Deck();
        this.Setup();
    }

    boolean Deal() {
        Card current;
        if (this.deck.Start() == null ) {
            return false;
        }   
        for (int i = 0; i < 7 && this.deck.Start() != null ; i++) {
            this.deck.use();
            current = this.deck.Start();
            this.deck.Start(current.Next());
            current.Next(this.field.lane(i));
            this.field.Top(i, current);
            current.Flip();
        }
        return true;
    }
}
