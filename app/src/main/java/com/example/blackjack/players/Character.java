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
    int layoutPosition;

    public Character(LinearLayout ll){
        this.value=0;
        this.cards = new ArrayList<Card>();
        this.cardsLayout = ll;
        this.layoutPosition = 0;
    }

    /*
    private LinearLayout getNewLayout(){
        LinearLayout lo = new LinearLayout(this.context);
        lo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        lo.setVisibility(View.VISIBLE);
        return lo;
    }

     */

    public void deal(Card c){
        this.cards.add(c);
        this.value += c.getValue();
        //this.cardsLayout.addView(c.getCardPhoto(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.cardsLayout.addView(c.getCardPhoto(), this.layoutPosition);
        layoutPosition ++;
    }

    public void reset(){
        this.cardsLayout.removeAllViews();
        this.value=0;
        this.cards.clear();
        this.layoutPosition = 0;
    }

}
