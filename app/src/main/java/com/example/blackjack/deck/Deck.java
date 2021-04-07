package com.example.blackjack.deck;

import android.content.Context;

import com.example.blackjack.deck.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck {

    List<Card> cards = new ArrayList<>();
    //Points to current index of deck
    private int cardPointer = -1;
    Context context;

    //TODO: Support multiple decks
    public Deck(Context context){
        this.context=context;
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Name name : Card.Name.values()) {
                Card c = new Card(suit, name, context);
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
    public Card getACard(){
        cardPointer ++;
        return cards.get(cardPointer);
    }

    // Get a pair of cards who are guaranteed to be splittable (has same value), and removes them from the deck.
    public ArrayList<Card> getSplitCards(){
        ArrayList<Card> splitCards = new ArrayList<Card>();
        cardPointer ++;
        int firstVal = cards.get(cardPointer).getValue();
        splitCards.add(cards.get(cardPointer));

        for(int i=1; i<cards.size(); i++){
            if(cards.get(i).getValue() == firstVal){
                splitCards.add(cards.get(i));
                cards.remove(i);
                break;
            }
        }
        cards.remove(0);
        return splitCards;
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


