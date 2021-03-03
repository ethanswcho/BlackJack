package com.example.blackjack.game;

import com.example.blackjack.deck.Deck;
import com.example.blackjack.deck.Card;

import java.util.ArrayList;

public class Game {

    Deck deck;

    int playerVal = 0;
    int dealerVal = 0;
    ArrayList<Card> playerCards = new ArrayList<Card>();
    ArrayList<Card> dealerCards = new ArrayList<Card>();

    public Game(){
        this.getNewDeck();
    }

    public void start(){
        this.initialDealing();
    }

    private void initialDealing(){
        playerCards.add(deck.deal());
        dealerCards.add(deck.deal());
        playerCards.add(deck.deal());
    }

    // Deals a card to player
    private void dealPlayer(){
        Card c = deck.deal();
        playerVal += c.getValue();
        playerCards.add(c);
    }

    // Deals a card to dealer
    private void dealDealer(){
        Card c = deck.deal();
        dealerVal += c.getValue();
        dealerCards.add(c);
    }

    // Gets new deck. Invoked when we have gone through more than half the deck.
    private void getNewDeck() {
        if (deck.pastHalf()) {
            deck = new Deck();
        }
    }
}
