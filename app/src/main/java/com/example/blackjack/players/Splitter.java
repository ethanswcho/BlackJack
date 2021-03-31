package com.example.blackjack.players;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blackjack.deck.Card;
import com.example.blackjack.game.StateManager;

public class Splitter extends Character {

    int bet;
    boolean doubleStatus;
    int debt;
    TextView bustAlert;
    StateManager.State state;

    public Splitter(LinearLayout ll, TextView tv, TextView bustAlert){
        super(ll, tv);
        this.title = "Split";
        this.bustAlert = bustAlert;
    }

    // Display Splitter UI
    public void displayUI(){
        this.cardsLayout.setVisibility(View.VISIBLE);
        this.titleText.setVisibility(View.VISIBLE);
    }

    // Hide Splitter UI
    public void hideUI(){
        this.cardsLayout.setVisibility(View.INVISIBLE);
        this.titleText.setVisibility(View.INVISIBLE);
        this.bustAlert.setVisibility(View.INVISIBLE);
    }

    public void setDoubleStatus(boolean doubleStatus){
        this.doubleStatus = doubleStatus;
    }

    public boolean getDoubleStatus(){
        return this.doubleStatus;
    }

    public void setState(StateManager.State state){
        this.state = state;
    }

    public StateManager.State getState(){
        return this.state;
    }

    // If splitter value > 21: display that the splitter has busted for clarity.
    private void displayBust(){
        this.bustAlert.setVisibility(View.VISIBLE);
    }

    @Override
    public void reset(){
        super.reset();
        this.state = StateManager.State.NONE;
    }

    /*
    // Save splitter's status before moving onto the player.
    // These fields will be accessed again when resolving gamestate at the end.
    public void updateStatus(boolean doubleStatus, int bet, StateManager.State state){
        this.doubleStatus = doubleStatus;
        this.bet = bet;
        this.state = state;
        if(this.getValue() > 21){
            displayBust();
        }
    }
    */

}
