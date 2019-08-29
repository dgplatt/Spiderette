package cards;
import java.util.Random;
import cards.*;

public class Deck {
    Card[] deck;
    Card start;
    public Deck() {
        this.deck = new Card[52];
        this.Make_Cards();
        this.Shuffle();
        this.Setup();
    }
    public void Make_Cards() {
        for (int i = 0; i < 13; i ++) {
            this.deck[i*4] = new Card(i + 1, "Clbs");
            this.deck[i*4 + 1] = new Card(i + 1, "Dmds");
            this.deck[i*4 + 2] = new Card(i + 1, "Hrts");
            this.deck[i*4 + 3] = new Card(i + 1, "Spds");
        }
    }
    public void Shuffle() {
        int[] placements  = new int[52];
        Random rnd = new Random();
        int key;
        for (int i = 0; i < 52; i++) {
            key = rnd.nextInt(52) + 1;
            for(int j = 0; j < i; j++){
                if (placements[j] == key){
                    key = rnd.nextInt(52) + 1;
                    j = -1;
                }
            }
            placements[i] = key;
        }
        Card[] suffled_deck = new Card[52];
        for (int i = 0; i < 52; i ++) {
            suffled_deck[i] = this.deck[placements[i] - 1];
        }
        this.deck = suffled_deck;
    }
    public void Setup() {
        this.start = this.deck[0];
        for (int i = 0; i < 51; i++) {
            this.deck[i].Next(this.deck[i+1]);
        }
    }
    public void Print_Deck() {
        for (int i = 0 ; i < 52; i ++) {
            this.deck[i].Print_Card();
        }
    }
    public Card Start() {
        return this.start;
    }
    public void Start(Card card) {
        this.start = card;
    }
}