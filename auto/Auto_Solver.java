package auto;
import cards.*;
import auto.*;
import java.util.*;

public class Auto_Solver {
    Actions start_action;
    int num_repeat, turn_max, turn_min, num_buckets, center;
    int[] values;

    public Auto_Solver(int num_repeat, int[] values) {
        this.num_repeat = num_repeat;
        this.values = values;
        this.turn_max = Math.max(Math.max(this.values[0], this.values[1]), this.values[2]) + this.values[3] + this.values[4];
        this.turn_min = Math.max(this.values[0], this.values[4]) + this.values[3];
        this.num_buckets = (this.turn_max + this.turn_min) * (this.num_repeat + 1) + 1;
        this.center = this.turn_min * this.num_repeat;
    }

    public Actions make_next_actions (Field field) {
        int max_value = 0;
        int min_value = 0;
        int start, end;
        Actions max_actions = null;
        ArrayList<ArrayList<Actions>> temp;
        ArrayList<Actions> act_list;

        ArrayList<ArrayList<Actions>> all_acts = new ArrayList<ArrayList<Actions>>(this.num_buckets);
        ArrayList<ArrayList<Actions>> prev_acts = new ArrayList<ArrayList<Actions>>(this.num_buckets);
        ArrayList<ArrayList<Actions>> temp_acts = new ArrayList<ArrayList<Actions>>(this.num_buckets);

        for (int i = 0; i < this.num_buckets; i ++) {
            all_acts.add(new ArrayList<Actions>());
            prev_acts.add(new ArrayList<Actions>());
            temp_acts.add(new ArrayList<Actions>());
        }
        prev_acts.get(this.center).add(new Actions(field));


        for (int i = 0; i < this.num_repeat; i ++) {
            start = Math.max((max_value + 1) - this.turn_max * (this.num_repeat - i), min_value) + this.center;
            end =  max_value + this.center;
            for (int y = end; y >= start; y--) {
                act_list =  prev_acts.get(y);
                for (Actions act: act_list){
                    move_loop: for (Move move: Find_Available_Moves(act.field)){
                        if(act.Value() + move.value() < (max_value + 1) - this.turn_max * (this.num_repeat - i - 1)) {
                            continue;
                        }
                        Actions temp_act = new Actions(act);
                        if (temp_act.add(move)) {
                            if (temp_act.field.lane(move.to()) == null) {
                                temp_act.Value(this.values[0]);
                            } else if (!temp_act.field.lane(move.to()).Known()) {
                                temp_act.Value(this.values[1]);
                            }
                        }
                        for (Actions Act: all_acts.get(temp_act.Value() + this.center)) {
                            if(Act.equals(temp_act)) {
                                continue move_loop;
                            }
                        }
                        temp_acts.get(temp_act.Value() + this.center).add(temp_act);
                        if(temp_act.Value() > max_value){
                            max_actions = temp_act;
                            max_value = temp_act.Value();
                        } else if(temp_act.Value() < min_value) {
                            min_value = temp_act.Value();
                        }
                    }
                }
            }
            for (int y = start - this.turn_min; y <= end + this.turn_max; y++) {
                all_acts.get(y).addAll(prev_acts.get(y));
                prev_acts.get(y).clear();
            }
            temp = prev_acts;
            prev_acts = temp_acts;
            temp_acts = temp;
        }
        return max_actions;
    }

    public ArrayList<Move> Find_Available_Moves(Field field) {
        ArrayList<Move> moves = new ArrayList();
        Card other, bottem;
        int depth, value, top_num, max_depth;
        for(int from = 0; from < 7; from++) {
            bottem = field.lane(from);
            if (bottem == null || !bottem.Known()) {
                continue;
            }
            top_num = bottem.Num();
            max_depth = 0;
            while (bottem.ordered()) {
                bottem = bottem.Next();
                max_depth ++;
            }
            for (int i = 0; i < 7; i ++) {
                if (i == from) {
                    continue;
                }
                value = 0;
                other = field.lane(i);
                if (other == null && bottem.Next() != null) {
                    value -= this.values[0];
                    //for (int x = 0; x < max_depth; x ++) {
                    //    moves.add(new Move(x, from, i, -5));
                    //}
                    if (! bottem.Next().Known()) {
                        value += this.values[1];
                    } else if (bottem.Next().Num() != bottem.Num() + 1) {
                        value += this.values[2];
                    } else if (! bottem.Next().Suit().equals(bottem.Suit())) {
                        value -= this.values[3];
                    }
                    moves.add(new Move(max_depth, from, i, value));
                } else if (other != null && other.Num() > top_num && other.Num() <= bottem.Num() + 1 && other.Known()) {
                    depth = other.Num() - (top_num + 1);
                    if (depth == max_depth) {
                        if(bottem.Next() == null) {
                            value += this.values[0];
                        } else if (! bottem.Next().Known()) {
                            value += this.values[1];
                        } else if (bottem.Next().Num() != bottem.Num() + 1) {
                            value += this.values[2];
                        } else if (! bottem.Next().Suit().equals(bottem.Suit())) {
                            value -= this.values[3];
                        } 
                        if(other.Suit().equals(bottem.Suit())) {
                            value += this.values[3] + this.values[4];
                        } else {
                            value += this.values[3];
                        }
                    } else {
                        value -= this.values[3] + this.values[4];
                    }
                    moves.add(new Move(depth, from, i, value));
                }
            }
        }
        return moves;
    }
}