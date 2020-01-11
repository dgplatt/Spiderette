package com.main;
import java.util.*;
import com.cards.*;
import com.auto.*;

public class Spiderette {
    Deck deck;
    Field field;
    int num_won;
    int games_played;
    int[] starter_scores;
    int[] winning_starter_scores;
    boolean testing;
    static Scanner input = new Scanner(System.in);

    Spiderette () {
        this.num_won = 0;
        this.games_played = 0;
        this.starter_scores = new int[300];
        this.winning_starter_scores = new int[300];
        this.testing = false;
    }

    void auto_play(int num_repeat, int[] values) {
        int done = 0;
        int num_moves = 0;
        int starter_score = 0;
        String to_print = "";
        boolean starter = true;

        this.Setup();
        Auto_Solver solver = new Auto_Solver(num_repeat, values);

        while (done != 4) {
            MoveSet best_act = solver.make_next_actions(this.field);
            to_print += this.field.toString();
            if (best_act == null) {
                if (starter) {
                    starter = false;
                    this.starter_scores[starter_score] ++;
                }
                if(this.Deal()){
                    continue;
                } 
                this.games_played ++;
                if (! this.testing) {
                    System.out.print(to_print);
                    System.out.println("*****  GameOver in " + num_moves +  " Moves  ******");
                }
                return;
            }
            for(Move move: best_act.getMoves()) {
                to_print += move.to_String();
                if (field.moveCard(move)) {
                    done++;
                }
                if (starter) {
                    starter_score += move.value();
                }
                num_moves ++;
                if(this.field.doFlip(move.from()) || this.field.doFlip(move.to())) {
                    break;
                }
            }
        }

        this.num_won++;
        this.games_played ++;
        this.winning_starter_scores[starter_score] ++;
        if (!this.testing) {
            System.out.print(to_print);
        }
        System.out.println("*****  WINNER!! in " + num_moves +  " Moves ******");
    }

    boolean play(int num_repeat, int[] values) {
        this.Setup();

        String comm = "";
        int depth; int from; int to; Card card;
        int done = 0;

        System.out.println("Commands:");
        System.out.println("Quit --> stops the game");
        System.out.println("New --> start new game");
        System.out.println("Deal --> deal the top of the deck");
        System.out.println("[0-6] --> select a lane");
        
        Auto_Solver solver = new Auto_Solver(num_repeat, values);
        while (done != 4) {
            field.print();
            deck.print();
            MoveSet best_act = solver.make_next_actions(this.field);
            if (best_act == null) {
                System.out.println("No Best Move");
            } else {
                for(Move move: best_act.getMoves()) {
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
                depth = this.field.maxDepth(from);
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
                if (this.field.doFlip(from)) { 
                    continue; 
                }
                do {
                    System.out.print("Where to?:  ");
                    comm = input.nextLine();
                } while (! (comm.length() == 1 && comm.charAt(0) >= '0' && comm.charAt(0) < '7'));

                to = Integer.parseInt(comm);
                Move move = new Move(depth, from, to);
                if (this.field.canMove(move)){
                    if (this.field.moveCard(move)) {
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
                this.deck.Start(current.getNext());
                current.setNext(this.field.lane(j));
                this.field.top(j,current);
            }
        }
        for (int i = 0; i < 7; i ++) {
            this.field.lane(i).flip();
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
            this.deck.Start(current.getNext());
            current.setNext(this.field.lane(i));
            this.field.top(i, current);
            current.flip();
        }
        return true;
    }
}