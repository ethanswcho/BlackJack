package com.example.blackjack.game;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blackjack.R;
import com.example.blackjack.deck.Deck;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;

public class Game extends AppCompatActivity {

    //public static Context context;

    Deck deck;
    Player player;
    Dealer dealer;
    LinearLayout dealerLayout, playerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.dealerLayout = (LinearLayout)findViewById(R.id.layout_dealer);
        this.playerLayout = (LinearLayout)findViewById(R.id.layout_player);
        this.dealerLayout.setVisibility(LinearLayout.VISIBLE);
        this.playerLayout.setVisibility(LinearLayout.VISIBLE);
        this.initialize();
        this.start();
    }


    // Initialize player, dealer and deck.
    private void initialize(){
        //this.dealerLayout.removeAllViews();
        //this.playerLayout.removeAllViews();
        this.player = new Player(10000, this.playerLayout);
        this.dealer = new Dealer(this.dealerLayout);
        this.deck = new Deck(this);
    }

    public void start(){
        this.initialDealing();
    }

    private void initialDealing(){
        player.deal(deck.getACard());
        dealer.deal(deck.getACard());
        player.deal(deck.getACard());
    }

    // Gets new deck. Invoked when we have gone through more than half the deck.
    private void getNewDeck() {
        if (deck.pastHalf()) {
            this.deck = new Deck(this);
        }
    }

}
