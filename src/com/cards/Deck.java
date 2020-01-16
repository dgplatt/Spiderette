package com.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
    private Card top;
    private int numCards;

    public Deck() {
        this.numCards = 52;
        Card[] deck  = Deck.makeCards();
        List<Card> CardList = Arrays.asList(deck);
        Collections.shuffle(CardList);
        CardList.toArray(deck);
        for (int i = 0; i < 51; i++) {
            deck[i].setNext(deck[i+1]);
        }
        this.top = deck[0];
    }

    private static Card[] makeCards() {
        Card[] deck = new Card[52];
        for (int i = 0; i < 13; i ++) {
            deck[i*4] = new Card(i + 1, "Clbs");
            deck[i*4 + 1] = new Card(i + 1, "Dmds");
            deck[i*4 + 2] = new Card(i + 1, "Hrts");
            deck[i*4 + 3] = new Card(i + 1, "Spds");
        }
        return deck;
    }
    
    @Override
    public String toString() {
        return "*** There are " + numCards + " cards in the Deck! ***";
    }

    public Card pop() {
        this.numCards --;
        Card toReturn = this.top;
        this.top = this.top.getNext();
        return toReturn;
    }

    public int getNumCards() {
        return numCards;
    }
}