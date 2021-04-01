package com.example.blackjack.game;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Manages transition from main game screen to post game screen
public class TransitionManager {

    public static void preparePostGameLayout(LinearLayout statusLayout1, LinearLayout statusLayout2,
                                             TextView textTotalMoney, EditText textBetAmount,
                                             int bet, float total, float blackJackMultiplier,
                                             ArrayList<StateManager.State> states){

        for(int i=0; i<states.size(); i++){
            StateManager.State state = states.get(i);
            String status = "If you are seeing this status it is a bug";
            String sign = "";
            float amount = bet;

            switch(state){
                case BLACKJACK:
                    status="BLACKJACK!";
                    sign="+";
                    amount *= blackJackMultiplier;
                    break;
                case WIN:
                    status="WIN!";
                    sign="+";
                    break;
                case DOUBLE_WIN:
                    status="DOUBLE WIN!";
                    sign="+";
                    amount *= 2;
                    break;
                case LOSS:
                    status="LOSS!";
                    sign="-";
                    break;
                case DOUBLE_LOSS:
                    status="DOUBLE LOSS!";
                    sign="-";
                    amount *= 2;
                    break;
                case TIE:
                    status="TIE!";
                    sign="+";
                    amount=0;
                    break;
                case NONE:
                    status="STATUS IS NONE! this is a bug";
                    break;
            }
            String prefix;
            TextView textStatus, textAmount;

            if(states.size() == 1){
                prefix = "PLAYER ";
                textStatus = (TextView)statusLayout2.getChildAt(0);
                textAmount = (TextView)statusLayout2.getChildAt(1);
            }

            else{
                prefix = "Hand" + String.valueOf(i+1) + " ";
                if(i == 0){
                    textStatus = (TextView)statusLayout1.getChildAt(0);
                    textAmount = (TextView)statusLayout1.getChildAt(1);
                }
                else{
                    textStatus = (TextView)statusLayout2.getChildAt(0);
                    textAmount = (TextView)statusLayout2.getChildAt(1);
                }
            }
            textStatus.setText(prefix + status);
            textAmount.setText(sign + " $" + String.valueOf(amount));
            if(sign == "+"){
                textAmount.setTextColor(0xFF00FF00);
            }
            else if (sign == "-") {
                textAmount.setTextColor(0xFFA50000);
            }
        }
        textTotalMoney.setText("$" + String.valueOf(total));
        textBetAmount.setText(String.valueOf(bet));
    }



    // Fade out main UI and enable post Game UI
    // Disable all background buttons
    public static void postGameTransition(ArrayList<View> mainGameViews, View postGameLayout,
                                          boolean splitStatus, LinearLayout splitLayout){
        changeViewsAlpha(mainGameViews, 0.1f);
        disableButtons(mainGameViews);
        postGameLayout.setVisibility(View.VISIBLE);
        // If player did not split, only one row of hand results should be shown
        if(splitStatus == false){
            splitLayout.setVisibility(View.INVISIBLE);
        }
    }

    // Fade back in the main UI, disable post game UI
    public static void preGameTransition(ArrayList<View> mainGameViews, View postGameLayout){
        changeViewsAlpha(mainGameViews, 1.0f);
        enableButtons(mainGameViews);
        postGameLayout.setVisibility(View.INVISIBLE);
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
