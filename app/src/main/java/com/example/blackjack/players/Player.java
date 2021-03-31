package com.example.blackjack.players;


import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blackjack.deck.Card;

import java.util.ArrayList;

public class Player extends Character{

    float money;

    public Player(LinearLayout ll, TextView tv, int m){
        super(ll, tv);
        this.money = m;
        this.title = "Player";
    }

    public float getMoney(){
        return this.money;
    }

    // TODO: Combine into one method
    public void loseMoney(float amount){
        this.money -= amount;
    }

    public void winMoney(float amount){
        this.money += amount;
    }

    public boolean canSplit(){
        return this.getNumCards() == 2 && (this.cards.get(0).getValue() == this.cards.get(1).getValue());
    }

    //Pops the second card, removes its value from player, removes its image from player cardsLayout
    // then returns the popped card so it can be used in Splitter.
    public Card popCard(){
        this.cardsLayout.removeViewAt(1);
        Card out =  this.cards.remove(1);
        this.value = this.value - out.getValue();
        return out;
    }

}
