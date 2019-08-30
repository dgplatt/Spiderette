package auto;
import cards.*;
import java.util.*;
public class Actions {
    ArrayList<Integer> depth;
    ArrayList<Integer> from;
    ArrayList<Integer> to;
    int value;
    Field field;
    boolean done;
    Actions (Field field) {
        this.field = new Field(field);
        this.depth = new ArrayList<Integer>();
        this.from = new ArrayList<Integer>();
        this.to  = new ArrayList<Integer>();
        this.value = 0;
        this.done = false;
    }

    Actions (Actions actions) {
        this.field = new Field(actions.field);
        this.depth = (ArrayList<Integer>) actions.depth.clone();
        this.from = (ArrayList<Integer>) actions.from.clone();
        this.to = (ArrayList<Integer>) actions.to.clone();
        this.value = actions.value;
        this.done = false;
    }

    void add (int[] move) {
        Card card = this.field.lane(move[1]);
        for (int i = 0; i < move[0]; i++) {
            card = card.Next();
        }
        this.field.Move(card, move[2], move[1]);
        if(this.field.lane(move[2]) != null && ! this.field.lane(move[2]).Known()) {
            this.done();
        }
        this.depth.add(move[0]);
        this.from.add(move[1]);
        this.to.add(move[2]);
        this.value += move[3];
    }
    boolean equals(Actions other) {
        return (other.Field().equals(this.field));
    }
    boolean is_done(){
        return this.done;
    }
    void done(){
        this.done = true;
    }
    int Value(){
        return this.value;
    }
    void Value(int x){
        this.value += x;
    }
    public Field Field(){
        return this.field;
    }
    public int[][] make_moves() {
        int leng = this.depth.toArray().length;
        int[][] all_moves = new int[leng][];
        for(int i = 0; i < leng; i++) {
            all_moves[i] = new int[]{this.depth.get(i), this.from.get(i), this.to.get(i)};
        }
        return all_moves;
    }

    public void print() {
        int leng = this.depth.toArray().length;
        for(int i = 0; i < leng; i++){
            System.out.print("  ||  depth : " + this.depth.get(i));
            System.out.print("  ||  from : " + this.from.get(i));
            System.out.println("  ||  to : " + this.to.get(i));
        }
        System.out.println(" ||  Value = " + this.value);
    }

}
