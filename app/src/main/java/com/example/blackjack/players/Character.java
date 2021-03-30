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
    boolean containsAce = false;
    LinearLayout.LayoutParams lp;

    public Character(LinearLayout ll, TextView tv){
        this.value = 0;
        this.cards = new ArrayList<Card>();
        this.cardsLayout = ll;
        this.layoutPosition = 0;
        this.titleText = tv;
        this.initializeLayoutParams();
    }

    // Initialize layout parameters for CardPhotos (which are ImageViews).
    private void initializeLayoutParams(){
        this.lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.LEFT;
        lp.weight = 1;
    }

    // Deals a card to the character. Subsequently updates:
    // cards
    // cardsLayout
    // value
    public void deal(Card c){
        if(c.getName() == Card.Name.Ace){
            this.containsAce = true;
        }
        this.cards.add(c);
        this.value += c.getValue();
        ImageView cp = c.getCardPhoto();
        cp.setLayoutParams(this.lp);
        cardsLayout.addView(cp);
        this.changeTitleText();
    }

    // Sets title text to correctly display current value
    private void changeTitleText(){
        String text = title + ": " + this.value;
        if(this.aceChangesValue()){
            text = text + "/" + (this.value+10);
        }
        this.titleText.setText(text);
    }

    // Returns true if current cards for this character contains an Ace AND the value changes because of it.
    public boolean aceChangesValue(){
        return this.containsAce && this.value <= 11;
    }

    public void reset(){
        this.cardsLayout.removeAllViews();
        this.value=0;
        this.cards.clear();
        this.layoutPosition = 0;
        this.containsAce = false;
    }

    public int getValue(){
        if(this.aceChangesValue()){
            return this.value + 10;
        }
        else{
            return this.value;
        }
    }

    public int getNumCards(){
        return this.cards.size();
    }

}
