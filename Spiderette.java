import java.util.*;
import cards.*;
import auto.*;

public class Spiderette {
    Deck deck;
    Field field;
    int num_won;
    int games_played;
    double[] starter_scores;
    double[] winning_starter_scores;

    public static void main (String[] args) {
        Spiderette game = new Spiderette();
        boolean again;
        Scanner input = new Scanner(System.in);
        String comm = "";
        System.out.print("Testing (y/n)?:  ");
        while (! comm.equals("y") && ! comm.equals("n")) {
            comm = input.nextLine();
        }
        if (comm.equals("y")) {
            for (int i = 0; i < 10000; i++) {
                if(i%500 == 0) {
                    System.out.println(i);
                }
                game.auto_play(true);
            }
            // print stats
            for (int i = 0; i < 200; i ++) {
                System.out.println(i + ": Won " + game.winning_starter_scores[i]+ " out of " + game.starter_scores[i] + " games!");
            }
        } else {
            comm = "";
            do {
                System.out.print("Auto (y/n)?:  ");
                while (! comm.equals("y") && ! comm.equals("n")) {
                    comm = input.nextLine();
                }
                if (comm.equals("y")) {
                    again = game.auto_play(false);
                } else {
                    again = game.play();
                }
                comm = "";
            } while (again);
        }
        System.out.println("You Won " + game.num_won + " out of " + game.games_played + " Games Played!!!");
    }

    Spiderette () {
        this.num_won = 0;
        this.games_played = 0;
        this.starter_scores = new double[200];
        this.winning_starter_scores = new double[200];
    }


    boolean auto_play(boolean testing) {
        this.Setup();
        Scanner input = new Scanner(System.in);
        String comm = "";
        int num_moves = 0;
        int done = 0;
        String to_print = "";
        boolean starter = true;
        int starter_score = 0;
        while (done != 4) {
            Auto_Solver solver = new Auto_Solver(this.field);
            Actions best_act = solver.make_next_actions();
            to_print += this.field.to_String();
            if (best_act == null) {
                if (starter) {
                    starter = false;
                    this.starter_scores[starter_score] ++;
                }
                if(this.Deal()){
                    continue;
                } 
                this.games_played ++;
                if (! testing) {
                    System.out.print(to_print);
                    System.out.println("*****  GameOver in " + num_moves +  " Moves  ******");
                    return repeat();
                }
                return testing;
            }
            for(Move move: best_act.moves()) {
                to_print += move.to_String();
                if (field.Move_Card(move)) {
                    done++;
                }
                if (starter) {
                    starter_score += move.value();
                }
                num_moves ++;
                if(this.field.do_flip(move.from()) || this.field.do_flip(move.to())) {
                    break;
                }
            }
        }

        this.num_won++;
        this.games_played ++;
        this.winning_starter_scores[starter_score] ++;
        System.out.println("*****  WINNER!! in " + num_moves +  " Moves ******");
        if (! testing) {
            System.out.print(to_print);
            return repeat();
        }
        return testing;
    }

    boolean play() {
        this.Setup();

        Scanner input = new Scanner(System.in);
        String comm = "";
        int depth; int from; int to; Card card;
        int done = 0;

        System.out.println("Commands:");
        System.out.println("Quit --> stops the game");
        System.out.println("New --> start new game");
        System.out.println("Deal --> deal the top of the deck");
        System.out.println("[0-6] --> select a lane");
    
        while (done != 4) {
            field.print();
            deck.print();
            Auto_Solver solver = new Auto_Solver(this.field);
            solver.make_next_actions();
            Actions best_act = solver.make_next_actions();
            if (best_act == null) {
                System.out.println("failure");
            } else {
                for(Move move: best_act.moves()) {
                    move.print();
                }
            }
            System.out.print("Enter Commands:  ");
            do {
                comm = input.nextLine();
            } while(comm.equals(""));

            if(comm.equals("Quit") || comm.equals("quit")) {
                this.games_played ++;
                return false;
            } else if (comm.equals("New") || comm.equals("new")) {
                this.games_played ++;
                return true;
            } else if (comm.equals("Deal") || comm.equals("deal")){
                this.Deal();
            } else if (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '7') {
                from = Integer.parseInt(comm);
                card = this.field.lane(from);
                if (card == null) {
                    System.out.println("No card there");
                    continue;
                }
                depth = this.field.max_depth(from);
                if (depth > 0) {
                    System.out.print("Max Depth y/n?:  ");
                    do {
                        comm = input.nextLine();
                    } while (! comm.equals("y") && ! comm.equals("n"));
                    if (comm.equals("n")) { 
                        do { 
                            System.out.print("Depth?:  ");
                            comm = input.nextLine();
                        } while(! (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '0' + 13));
                        depth = Integer.parseInt(comm);
                    }
                }
                if (this.field.do_flip(from)) { 
                    continue; 
                }
                do {
                    System.out.print("Where to?:  ");
                    comm = input.nextLine();
                } while (! (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '7'));

                to = Integer.parseInt(comm);
                Move move = new Move(depth, from, to);
                if (this.field.Can_Move(move)){
                    if (this.field.Move_Card(move)) {
                        done++;
                    }
                } else {
                    System.out.print("Can't move there");
                }
            }
        }
        this.games_played ++;
        System.out.print("*****  WINNER!!  ******");
        this.num_won++;
        return repeat();
    }

    boolean repeat() {
        Scanner input = new Scanner(System.in);
        String comm = "";
        System.out.print("Again (y/n)?:  ");
        while (! comm.equals("y") && ! comm.equals("n")) {
            comm = input.nextLine();
        }
        return comm.equals("y");
    }

    private void Setup() {
        this.field = new Field();
        this.deck = new Deck();
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