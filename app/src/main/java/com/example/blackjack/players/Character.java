package com.example.blackjack.players;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blackjack.deck.Card;
import com.example.blackjack.game.Game;

import java.util.ArrayList;

public class Character {

    int value;
    ArrayList<Card> cards;
    LinearLayout cardsLayout;
    String title;
    int layoutPosition;
    TextView titleText;

    public Character(LinearLayout ll, TextView tv){
        this.value=0;
        this.cards = new ArrayList<Card>();
        this.cardsLayout = ll;
        this.layoutPosition = 0;
        this.titleText = tv;
    }

    // Reset cards list and layout.
    public void resetCards(){
        this.cards.clear();
        this.cardsLayout.removeAllViews();
    }

    public void deal(Card c){
        this.cards.add(c);
        this.value += c.getValue();
        ImageView cp = c.getCardPhoto();
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.LEFT;
        lp.weight = 1;
        cp.setLayoutParams(lp);
        this.cardsLayout.addView(cp);

        titleText.setText(title + " " + this.value);
    }

    public int getValue(){
        return this.value;
    }

    public void reset(){
        this.cardsLayout.removeAllViews();
        this.value=0;
        this.cards.clear();
        this.layoutPosition = 0;
    }

    public int getNumCards(){
        return this.cards.size();
    }

}
