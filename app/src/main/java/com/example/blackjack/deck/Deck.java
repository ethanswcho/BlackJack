package com.example.blackjack.deck;

import com.example.blackjack.deck.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck {

    List<Card> cards = new ArrayList<>();
    //Points to current index of deck
    private int cardPointer = -1;

    //TODO: Support multiple decks
    public Deck(){
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Name name : Card.Name.values()) {
                Card c = new Card(suit, name);
                cards.add(c);
            }
        }
        shuffleDeck();
        //this.iterateDeck();
    }

    // Shuffle the deck
    private void shuffleDeck(){
        Collections.shuffle(cards);
    }

    // Get a card from the deck
    public Card deal(){
        cardPointer ++;
        return cards.get(cardPointer);
    }

    //Test method. Prints out the contents of current deck.
    public void iterateDeck(){
        for(Card c: cards){
            System.out.println(c.getSuit().name() + "|" + c.getName().name() + "|" + c.getValue());
        }
    }

    // Returns whether we are more than halfway through the deck or not. (Deck needs to be refilled if it is.)
    public boolean pastHalf(){
        return (cardPointer > cards.size()/2);
    }

}


