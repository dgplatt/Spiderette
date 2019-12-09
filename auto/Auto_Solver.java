package auto;
import cards.*;
import auto.*;
import java.util.*;

public class Auto_Solver {
    Actions start_action;
    int num_buckets;
    int num_repeat;
    int known_value;
    int free_space_value;
    int ordered_value;
    int same_suit_value;
    int out_of_order_value;

    public Auto_Solver(Field field) {
        this.num_buckets = 30;
        this.start_action = new Actions(field);
        this.num_repeat = 6;
        this.known_value = 4;
        this.free_space_value = 2;
        this.ordered_value = 1;
        this.out_of_order_value = 1;
        this.same_suit_value = 2;
    }

    public Actions make_next_actions () {
        int max_value = 0;
        Actions max_actions = null;
        boolean equal;
        ArrayList<ArrayList<Actions>> temp;
        int x;
        Card temp_card;
        int rank = 0;

        ArrayList<ArrayList<Actions>> all_acts = new ArrayList<ArrayList<Actions>>(this.num_buckets);
        for (int i = 0; i < this.num_buckets; i ++) {
            all_acts.add(new ArrayList<Actions>());
        }
        ArrayList<ArrayList<Actions>> prev_acts = new ArrayList<ArrayList<Actions>>(this.num_buckets);
        for (int i = 0; i < this.num_buckets; i ++) {
            prev_acts.add(new ArrayList<Actions>());
        }
        prev_acts.get(10).add(this.start_action);
        ArrayList<ArrayList<Actions>> temp_acts = new ArrayList<ArrayList<Actions>>(this.num_buckets);
        for (int i = 0; i < this.num_buckets; i ++) {
            temp_acts.add(new ArrayList<Actions>());
        }

        for (int i = 0; i < this.num_repeat; i ++) {
            for (ArrayList<Actions> act_list: prev_acts) {
                for (Actions act: act_list){
                    if(act.Value() < max_value - 6 *(this.num_repeat - 1 - i)) {
                        continue;
                    }
                    for (Move move: Find_Available_Moves(act.field)){
                        Actions temp_act = new Actions(act);
                        temp_act.add(move);
                        if(temp_act.Value() > this.num_buckets - 11) {
                            x = 0;
                        } else if(temp_act.Value() >= -10) {
                            x = -temp_act.Value() + this.num_buckets - 11;
                        } else {
                            break;
                        }
                        equal = false;
                        for (Actions Act: all_acts.get(x)) {
                            if(Act.equals(temp_act)) {
                                equal = true;
                                break;
                            }
                        }
                        if(!equal) {
                            temp_acts.get(x).add(temp_act);
                            //temp_card = temp_act.Field().lane(move.from());
                            //if (temp_card != null && !temp_card.Known()) {
                            //    temp_act.Value(this.num_repeat - i);
                            //}
                            if(temp_act.Value() > max_value){
                                max_actions = temp_act;
                                max_value = temp_act.Value();
                            }
                        }
                    }
                }
            }
            for (int y = 0; y < this.num_buckets; y++ ) {
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
        Card other;
        int depth;
        int value;
        Card Bottem;
        int Top_num;
        int max_depth;
        for(int from = 0; from < 7; from++) {
            Bottem = field.lane(from);
            if (Bottem == null || !Bottem.Known()) {
                continue;
            }
            Top_num = Bottem.Num();
            max_depth = 0;
            while (Bottem.ordered()) {
                Bottem = Bottem.Next();
                max_depth ++;
            }
            for (int i = 0; i < 7; i ++) {
                if (i == from) {
                    continue;
                }
                value = 0;
                other = field.lane(i);
                if (other == null && Bottem.Next() != null) {
                    value -= this.free_space_value;
                    //for (int x = 0; x < max_depth; x ++) {
                    //    moves.add(new Move(x, from, i, -5));
                    //}
                    if (! Bottem.Next().Known()) {
                        value += this.known_value;
                    } else if (Bottem.Next().Num() != Bottem.Num() + 1) {
                        value += 1;
                    } else if (! Bottem.Next().Suit().equals(Bottem.Suit())) {
                        value -= this.ordered_value;
                    }
                    moves.add(new Move(max_depth, from, i, value));
                } else if (other != null && other.Num() > Top_num && other.Num() <= Bottem.Num() + 1 && other.Known()) {
                    depth = other.Num() - (Top_num + 1);
                    if (depth == max_depth) {
                        if(Bottem.Next() == null) {
                            value += this.free_space_value;
                        } else if (! Bottem.Next().Known()) {
                            value += this.known_value;
                        } else if (Bottem.Next().Num() != Bottem.Num() + 1) {
                            value += this.out_of_order_value;
                        } else if (! Bottem.Next().Suit().equals(Bottem.Suit())) {
                            value -= this.ordered_value;
                        } 
                        if(other.Suit().equals(Bottem.Suit())) {
                            value += this.ordered_value + this.same_suit_value;
                        } else {
                            value += this.ordered_value;
                        }
                    } else {
                        value -= this.ordered_value + this.same_suit_value;
                    }
                    moves.add(new Move(depth, from, i, value));
                }
            }
        }
        return moves;
    }
}