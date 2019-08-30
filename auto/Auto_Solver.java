package auto;
import cards.*;
import auto.*;
import java.util.*;

public class Auto_Solver {
    ArrayList<ArrayList<Actions>>  acts;

    public Auto_Solver(Field field) {
        this.acts = new ArrayList<ArrayList<Actions>>(30);
        for (int i = 0; i < 30; i ++) {
            this.acts.add(new ArrayList<Actions>());
        }
        Actions no_moves = new Actions(field);
        this.acts.get(10).add(no_moves);
    }
    public void make_next_actions () {
        int[] total = new int[30];
        total[10] = 1;
        int max_value = 0;
        int num_repeat = 9;
        boolean equal;
        int[] temp_total = new int[30];
        int x; int p;
         ArrayList<ArrayList<Actions>> temp_acts = new ArrayList<ArrayList<Actions>>(30);
        for (int i = 0; i < 30; i ++) {
            temp_acts.add(new ArrayList<Actions>());
        }
        for (int i = 0; i < num_repeat; i ++) {
            p = 0;
            for (ArrayList<Actions> act_list: this.acts) {
                if(p - 10 < max_value - 3*(num_repeat - i) && p != 29) {
                    continue;
                }
                p ++;
                for (Actions act: act_list){
                    if (act.is_done()) {
                        continue;
                    } else if(act.Value() > max_value){
                        max_value = act.Value();
                    }
                    //act.print();
                    for (int[] move: Find_Available_Moves(act.field)){
                        Actions temp_act = new Actions(act);
                        temp_act.add(move);
                        equal = false;
                        if(temp_act.Field().complete(move[2])) {
                            temp_act.Field().Remove(move[2]);
                            temp_act.Value(30);
                             if(temp_act.Value() > max_value){
                                max_value = temp_act.Value();
                            }
                        }
                        if(temp_act.Value() > 19) {
                            x = 29;
                        } else if(temp_act.Value() < -10) {
                            x = 0;
                        } else {
                            x = temp_act.Value() + 10;
                        }
                        for (int j = 0; j < total[x]; j++) {
                            if(this.acts.get(x).get(j).equals(temp_act)) {
                                equal = true;
                                break;
                            }
                        }
                        if(!equal) {
                            temp_acts.get(x).add(temp_act);
                            temp_total[x] ++;
                        }
                    }
                    act.done();
                }
            }
            for (int y = 0; y < 30; y++ ) {
                total[y] += temp_total[y];
                temp_total[y] = 0;
                this.acts.get(y).addAll(temp_acts.get(y));
                temp_acts.get(y).clear();
            }
        }
        System.out.println("");
    }
    public ArrayList<ArrayList<Actions>> acts() {
        return this.acts;
    }
    public ArrayList<int[]> Find_Available_Moves(Field field) {
        ArrayList<int[]> moves = new ArrayList();
        for(int lane = 0; lane < 7; lane++) {
            Card card = field.lane(lane);
            int depth = 0;
            int num_moves;
            int value;
            while(card != null) {
                for (int i = 0; i < 7; i ++) {
                    if (i == lane) {
                        continue;
                    }
                    value = 0;
                    if ((field.lane(i) == null && card.Next() != null)
                        || (field.lane(i) != null && field.lane(i).Num() == card.Num() + 1  && field.lane(i).Known() && card.Known())) {
                        if(card.Next() == null) {
                            value += 2;
                        } else if (! card.Next().Known()) {
                            value += 6;
                        } else if (card.Next().Num() == card.Num() + 1  && card.Next().Suit().equals(card.Suit())) {
                            value -= 3;
                        } else if (card.Next().Num() != card.Num() + 1) {
                            value += 2;
                        } 
                        if (field.lane(i) != null) {
                            if(field.lane(i).Suit().equals(card.Suit())) {
                                value += 3;
                            } else {
                                value -= 1;
                            }
                        } else {
                            value -= 2;
                        }
                        moves.add(new int[]{ depth, lane, i, value});
                    }
                }
                if(card.Next() == null || card.Next().Suit() != card.Suit() || card.Next().Num() != card.Num() + 1 || ! card.Next().Known()) {
                    break;
                }
                card = card.Next();
                depth ++;
            }
        }
        return moves;
    }
    public Actions Max() {
        int max_value = 0;
        Actions max_actions = null;
        for (int i  = 29; i > 10; i--) {
            if(this.acts.get(i).isEmpty()) {
                continue;
            }
            for (Actions act: this.acts.get(i)) {
                if(act.value > max_value) {
                    max_value = act.value;
                    max_actions = act;
                }
            }
            break;
        }
        return max_actions;
    }
}