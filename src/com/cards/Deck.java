package com.cards;
import java.util.Random;

public class Deck {
    Card start;
    int num_cards;
    public Deck() {
        this.num_cards = 52;
        this.start = Setup(Make_Cards());
    }

    public Deck(Deck deck) {
        Card old_card = deck.start;
        Card new_card = new Card(deck.start);
        this.start = new_card;
        while(old_card.getNext() != null) {
            new_card.setNext(new Card(old_card.getNext()));
            old_card = old_card.getNext();
            new_card = new_card.getNext();
        }
    }

    private Card[] Make_Cards() {
        Card[] deck = new Card[52];
        for (int i = 0; i < 13; i ++) {
            deck[i*4] = new Card(i + 1, "Clbs");
            deck[i*4 + 1] = new Card(i + 1, "Dmds");
            deck[i*4 + 2] = new Card(i + 1, "Hrts");
            deck[i*4 + 3] = new Card(i + 1, "Spds");
        }
        return Shuffle(deck);
    }

    private Card[] Shuffle(Card[] deck) {
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
            suffled_deck[i] = deck[placements[i] - 1];
        }
        return suffled_deck;
    }
    private Card Setup(Card[] deck) {
        for (int i = 0; i < 51; i++) {
            deck[i].setNext(deck[i+1]);
        }
        return deck[0];
    }

    public void print() {
        System.out.println("*** There are " + num_cards + " cards in the Deck! ***");
    }

    public Card Start() {
        return this.start;
    }

    public void use() {
        this.num_cards --;
    }

    public void Start(Card card) {
        this.start = card;
    }
}