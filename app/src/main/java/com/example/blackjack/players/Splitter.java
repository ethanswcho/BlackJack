package com.example.blackjack.players;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blackjack.deck.Card;

public class Splitter extends Character {

    int bet;

    public Splitter(LinearLayout ll, TextView tv, Card c, int bet){
        super(ll, tv);
        this.title = "Split";
        this.bet = bet;
        this.deal(c);
    }

}
