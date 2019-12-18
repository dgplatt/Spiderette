package auto;
import cards.*;
import java.util.*;
public class Actions {
    ArrayList<Move> actions;
    int value;
    Field field;
    Actions (Field field) {
        this.field = new Field(field);
        this.actions = new ArrayList<Move>();
        this.value = 0;
    }

    Actions (Actions other) {
        this.field = new Field(other.field);
        this.actions = (ArrayList<Move>) other.actions.clone();
        this.value = other.Value();
    }

    void add (Move move) {
        this.field.Move_Card(move);
        //if (this.field.Move_Card(move)) {
        //    this.value += 30;
        //}
        this.actions.add(move);
        this.value += move.value();
    }

    boolean equals(Actions other) {
        return (other.Field().equals(this.field));
    }

    int Value(){
        return this.value;
    }
    void Value(int val){
        this.value += val;
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