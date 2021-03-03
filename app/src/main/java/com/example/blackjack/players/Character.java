package com.example.blackjack.players;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.blackjack.deck.Card;

import java.util.ArrayList;

public class Character {

    int value;
    ArrayList<Card> cards = new ArrayList<Card>();
    LinearLayout cardsLayout;
    Context context;

    public Character(Context c){
        this.context=c;
    }

    public void deal(Card c){
        this.cards.add(c);
        this.value += c.getValue();
        this.cards.add(c.getCardPhoto());
    }



}
