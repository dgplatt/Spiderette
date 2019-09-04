package auto;
import cards.*;
import java.util.*;
public class Actions {
    ArrayList<Move> actions;
    int value;
    Field field;
    boolean done;
    Actions (Field field) {
        this.field = new Field(field);
        this.actions = new ArrayList<Move>();
        this.value = 0;
        this.done = false;
    }

    Actions (Actions other) {
        this.field = new Field(other.field);
        this.actions = (ArrayList<Move>) other.actions.clone();
        this.value = other.Value();
        this.done = false;
    }

    boolean add (Move move) {
        this.field.Move_Card(move);
        boolean complete = this.field.complete(move.to());
        if (complete) {
            this.value += 30;
        }
        if(this.field.do_flip(move.to()) || this.field.do_flip(move.from())) {
            this.done();
        }
        this.actions.add(move);
        this.value += move.value();
        return complete;
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

    public Field Field(){
        return this.field;
    }
    public ArrayList<Move> moves() {
        return this.actions;
    }

    public void print() {
        for(Move move: this.actions){
            move.print();
        }
        System.out.println(" ||  Value = " + this.value);
    }

}