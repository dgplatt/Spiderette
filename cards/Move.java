package cards;
public class Move {
    int depth;
    int from;
    int to;
    int value;
    public Move (int depth, int from, int to, int value) {
        this.depth = depth;
        this.from = from;
        this.to = to;
        this.value = value;
    }
    public Move (int depth, int from, int to) {
        this.depth = depth;
        this.from = from;
        this.to = to;
        this.value = 0;
    }
    public int depth() {
        return this.depth;
    }
    public int from() {
        return this.from;
    }
    public int to() {
        return this.to;
    }
    public int value() {
        return this.value;
    }
    public String to_String() {
        return ("  ||  depth : " + this.depth + "  ||  from : " + this.from + "  ||  to : " + this.to + "||  value : " + this.value +"\n");
    }
    public void print(){
        System.out.print(this.to_String());
    }
}