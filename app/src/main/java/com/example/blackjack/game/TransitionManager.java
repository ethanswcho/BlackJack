package com.example.blackjack.game;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TransitionManager {

    // Prepares the post game layout with information from the current hand.
    public static void preparePostGameLayout(TextView textStatus, TextView textWinLossAmount,
                                             TextView textTotal, int bet, float total,
                                             float blackJackMultipler, StateManager.State state){
        String status = "If you are seeing this status it is a bug";
        String color = "";
        String sign = "";
        float amount = bet;

        switch(state){
            case BLACKJACK:
                status="BLACKJACK!";
                color="66ff00";
                sign="+";
                amount *= blackJackMultipler;
                break;
            case WIN:
                status="Player WIN!";
                color="66ff00";
                sign="+";
                break;
            case LOSS:
                status="Dealer WIN!";
                color="#ff0000";
                sign="-";
                break;
            case TIE:
                status="TIE!";
                color="66ff00";
                sign="+";
                amount=0;
                break;
            case NONE:
                status="STATUS IS NONE! this is a bug";
                break;
        }
        textStatus.setText(status);
        String htmlText = "<font color=" + color + ">" + sign + " $" + String.valueOf(amount) + "</font";
        textWinLossAmount.setText(Html.fromHtml(htmlText));
        textTotal.setText("TOTAL: " + String.valueOf(total));
    }

    // Fade out main UI and enable post Game UI
    // Disable all background buttons
    public static void postGameTransition(ArrayList<View> mainGameViews, View postGameLayout){
        changeViewsAlpha(mainGameViews, 0.1f);
        disableButtons(mainGameViews);
        postGameLayout.setVisibility(View.VISIBLE);
    }

    // Fade back in the main UI, disable post game UI
    public static void preGameTransition(ArrayList<View> mainGameViews, View postGameLayout){
        changeViewsAlpha(mainGameViews, 1.0f);
        enableButtons(mainGameViews);
        postGameLayout.setVisibility(View.INVISIBLE);
    }

    // Fade out given list of views
    private static void changeViewsAlpha(ArrayList<View> views, float alpha){
        for(int i=0; i<views.size(); i++) {
            views.get(i).setAlpha(alpha);
        }
    }

    private static void enableButtons(ArrayList<View> views){
        for(int i=0; i<views.size(); i++) {
            if(views.get(i) instanceof Button){
                views.get(i).setEnabled(true);
            }
        }
    }

    private static void disableButtons(ArrayList<View> views){
        for(int i=0; i<views.size(); i++) {
            if(views.get(i) instanceof Button){
                views.get(i).setEnabled(false);
            }
        }
    }

    // Returns the views used in main Game (All views - views in postGameLayout)
    public static ArrayList<View> getGameViews(View gameLayout, View postGameLayout){
        ViewGroup tempVG = (ViewGroup)gameLayout;
        View tempV = tempVG.getChildAt(0);
        ArrayList<View>gameLayoutChildren = getChildViews(tempV);
        for(int i=0; i<gameLayoutChildren.size(); i++){
            View v = gameLayoutChildren.get(i);
            if(v.getId() == postGameLayout.getId()){
                gameLayoutChildren.remove(i);
                break;
            }
        }
        return gameLayoutChildren;
    }

    // Get Child views of the input view
    private static ArrayList<View> getChildViews(View v){
        ViewGroup vg = (ViewGroup)v;
        int count = vg.getChildCount();
        ArrayList<View> childViews = new ArrayList<View>();
        for(int i=0; i<count; i++){
            childViews.add(vg.getChildAt(i));
        }
        return childViews;
    }

    // Remove Views in A2 from A1 by using Set difference operation.
    private static ArrayList<View> minusViewArrays(ArrayList<View> a1, ArrayList<View> a2){
        Set<View> a1set = new HashSet<View>(a1);
        Set<View> a2set = new HashSet<View>(a2);
        a1set.removeAll(a2set);
        ArrayList<View> output = new ArrayList<View>();
        output.addAll(a1set);
        return output;
    }

}
