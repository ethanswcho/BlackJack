package com.example.blackjack.game;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blackjack.R;
import com.example.blackjack.deck.Deck;
import com.example.blackjack.deck.Card;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    Deck deck;

    Player player;
    Dealer dealer;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        this.game = new Game(this);

    }

    public Game(Context c){
        this.context=c;
        player = new Player(10000, c);
        dealer = new Dealer(c);
        this.getNewDeck();
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
            this.deck = new Deck();
        }
    }
}
