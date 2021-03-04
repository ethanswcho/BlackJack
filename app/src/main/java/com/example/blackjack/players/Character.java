package com.example.blackjack.players;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.blackjack.deck.Card;
import com.example.blackjack.game.Game;

import java.util.ArrayList;

public class Character {

    int value;
    ArrayList<Card> cards;
    LinearLayout cardsLayout;
    Context context;

    public Character(Context context){
        this.value=0;
        this.cards = new ArrayList<Card>();
        this.context = context;
        this.cardsLayout = this.getNewLayout();
    }

    private LinearLayout getNewLayout(){
        LinearLayout lo = new LinearLayout(this.context);
        lo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        lo.setVisibility(View.VISIBLE);
        return lo;
    }

    public void deal(Card c){
        this.cards.add(c);
        this.value += c.getValue();
        this.cardsLayout.addView(c.getCardPhoto());
    }

    public void reset(){
        this.value=0;
        this.cards.clear();
        this.cardsLayout = getNewLayout();
    }

}
