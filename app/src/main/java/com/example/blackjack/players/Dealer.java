package com.example.blackjack.players;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blackjack.deck.Card;

import java.util.ArrayList;

public class Dealer extends Character{
    //TODO:
    //Change below to public final static
    int hittingLimit = 17;

    public Dealer(LinearLayout ll, TextView tv){
        super(ll, tv);
        this.title = "Dealer";
    }

    public void setHittingLimit(int hl){
        this.hittingLimit = hl;
    }

    public int getHittingLimit(){
        return this.hittingLimit;
    }
}