package com.main;
import java.util.*;
import com.cards.*;
import com.auto.*;

public class Spiderette {
    private Deck deck;
    private Field field;
    private String gameRecord;
    private boolean winner;
    private int numMoves, numRepeat;
    private int[] values, scores;

    private static Scanner input;

    public Spiderette (int[] values, int numRepeat) {
        this.values = values;
        this.scores = new int[5];
        this.numRepeat = numRepeat;
        this.winner = false;
        this.numMoves = 0;
        this.gameRecord = "";
        this.field = new Field(7);
        this.deck = new Deck();

        for (int i = 0; i < 7; i ++) {
            this.field.add(null);
        }
        Card top;
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j ++) {
                top = this.deck.pop();
                top.setNext(this.field.get(j));
                this.field.put(j, top);
            }
        }
        for (int i = 0; i < 7; i ++) {
            this.field.get(i).flip();
        }
    }

    public void auto_play() {
        int score = 0;
        int deals = 0;

        AutoSolver solver = new AutoSolver(this.numRepeat, this.values);

        while (!this.field.isDone()) {
            MoveSet best_act = solver.getNextSet(this.field);
            this.gameRecord += this.field.toString();
            if (best_act == null) {
                this.scores[deals] = score;
                deals ++;
                if(this.Deal()){
                    continue;
                } 
                return;
            }
            for(Move move: best_act.getMoves()) {
                this.gameRecord += move.toString() + "/n";
                field.moveCard(move);
                score += move.getValue();
                this.numMoves ++;
                if(this.field.doFlip(move.getFrom()) || this.field.doFlip(move.getTo())) {
                    break;
                }
            }
        }
        this.scores[deals] = score;
        this.winner = true;
    }

    public void play() {
        
        AutoSolver solver = new AutoSolver(this.numRepeat, this.values);

        int comm, depth, from, to;
        Card card;

        System.out.println("[0] Quit --> End the game");
        System.out.println("[1-7] --> Select a lane");
        System.out.println("[8] Deal --> Deal the top of the deck");
        while (!this.field.isDone()) {
            System.out.println(field);
            System.out.println(deck);
            MoveSet best_act = solver.getNextSet(this.field);
            if (best_act == null) {
                System.out.println("No Best Move");
            } else {
                for(Move move: best_act.getMoves()) {
                   System.out.println(move);
                }
            }
            System.out.print("Enter Command [0-8]:  ");
            comm = Spiderette.getNum(0, 8);

            if(comm == 0) {
                return;
            } else if (comm == 8){
                this.Deal();
            } else {
                from = comm - 1;
                card = this.field.get(from);
                if (card == null) {
                    System.out.println("No card there");
                    continue;
                }
                Card bottom = this.field.maxDepth(from);
                depth = bottom.getNum() - card.getNum();
                if (depth > 0) {
                    System.out.print("Depth [0-"+ depth + "]:  ");
                    depth = Spiderette.getNum(0, depth);
                }
                if (this.field.doFlip(from)) { 
                    continue; 
                }
                System.out.print("Where to? [1-7]:  ");
                to = Spiderette.getNum(1, 7) - 1;
                Move move = new Move(depth, from, to);
                if (this.field.canMove(move)){
                    this.field.moveCard(move);
                } else {
                    System.out.println("Invalid move");
                }
            }
        }
        this.winner = true;
    }

    private static int getNum(int lower, int upper) {
        int comm;
        while (true) {
            try {
                comm = input.nextInt();
                if (comm >= lower && comm <= upper) {
                    break;
                }
            } catch (Exception e) {
                System.out.print("Enter number [" + lower + "-" + upper + "]: ");
            } finally {
                input.nextLine();
            }
            System.out.print("Enter number [" + lower + "-" + upper + "]: ");
        } 
        return comm;
    }

    public static boolean repeat() {
        String comm = "";
        System.out.print("Again (y/n)?:  ");
        while (! comm.equals("y") && ! comm.equals("n")) {
            comm = input.nextLine();
        }
        return comm.equals("y");
    }

    boolean Deal() {
        Card top;
        if (this.deck.getNumCards() == 0 ) {
            return false;
        }   
        for (int i = 0; i < 7 && this.deck.getNumCards() > 0 ; i++) {
            top = this.deck.pop();
            top.flip();
            this.field.put(i, top);
        }
        return true;
    }

    public String getGameRecord() {
        return gameRecord;
    }

    public boolean isWinner() {
        return winner;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public int[] getScores() {
        return scores;
    }
    public static void close(){
        Spiderette.input.close();
    }

    public static void setInput(Scanner input) {
        Spiderette.input = input;
    }

	@Override
	public String toString() {
		return this.field.toString() + this.deck.toString();
	}
    
    
}