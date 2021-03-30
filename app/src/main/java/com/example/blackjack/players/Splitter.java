package com.example.blackjack.players;

import android.widget.LinearLayout;
import android.widget.TextView;

public class Splitter extends Character {

    public Splitter(LinearLayout ll, TextView tv, int m){
        super(ll, tv);
        this.title = "Split";
    }


}
