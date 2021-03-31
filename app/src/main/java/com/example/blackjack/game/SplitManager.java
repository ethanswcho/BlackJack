package com.example.blackjack.game;

import android.view.View;
import android.view.ViewGroup;

import com.example.blackjack.players.Splitter;

public class SplitManager {

    boolean splitStatus;
    Splitter splitter;
    int splitDebt;
    boolean doubleStatus;


    public static void displaySplitUI(ViewGroup splitGroup){
        for(int i=0; i<splitGroup.getChildCount(); i++){
            splitGroup.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    public static void hideSplitUI(ViewGroup splitGroup){
        for(int i=0; i<splitGroup.getChildCount(); i++){
            splitGroup.getChildAt(i).setVisibility(View.INVISIBLE);
        }
    }



}
