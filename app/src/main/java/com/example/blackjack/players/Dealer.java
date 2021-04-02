package com.example.blackjack.players;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blackjack.deck.Card;
import com.example.blackjack.deck.Deck;

import java.util.ArrayList;

public class Dealer extends Character{
    //TODO:
    //Change below to public final static
    int hittingLimit = 17;

    public Dealer(LinearLayout ll, TextView tv, ImageView a){
        super(ll, tv, a);
        this.title = "Dealer";
    }

    public void setHittingLimit(int hl){
        this.hittingLimit = hl;
    }

    public int getHittingLimit(){
        return this.hittingLimit;
    }

    public void doDealerAnimation(final Deck d){
        HandlerThread thread = new HandlerThread("HT");
        thread.start();
        final Handler handler = new Handler(thread.getLooper());
        while(this.value < this.hittingLimit){
            handler.postDelayed(new Runnable() {
                @Override
                public void run(){
                    System.out.println("!!!!!!!!!!!!!!!!!!");
                    deal(d.getACard());
                }
            }, 100);
        }
    }

}