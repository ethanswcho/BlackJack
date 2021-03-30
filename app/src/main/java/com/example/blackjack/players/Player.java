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

    public void loseMoney(float amount){
        this.money -= amount;
    }

    public void winMoney(float amount){
        this.money += amount;
    }

    public boolean canSplit(){
        return this.getNumCards() == 2 && (this.cards.get(0).getValue() == this.cards.get(1).getValue());
    }

    // Pops the second card and returns it => Splitter object will be initialized with the popped card
    public Card popCard(){
        return this.cards.remove(1);
    }

}
