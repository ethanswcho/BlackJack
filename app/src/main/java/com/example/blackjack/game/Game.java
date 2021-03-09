package com.example.blackjack.game;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blackjack.R;
import com.example.blackjack.deck.Deck;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;

import java.util.concurrent.TimeUnit;

public class Game extends AppCompatActivity {

    //public static Context context;

    Deck deck;
    Player player;
    Dealer dealer;
    LinearLayout dealerLayout, playerLayout;
    Button buttonHit, buttonPass, buttonDouble, buttonSplit;
    TextView textPlayer, textDealer, textMoney, textBet;

    // TODO: Customizable odds are below
    int bet = 10;
    int hittingLimit = 17;
    float blackJackMultiplier = 1.5f;
    int startingMoney = 10000;
    enum Check{BlackJack, Hit, Double}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.dealerLayout = (LinearLayout)findViewById(R.id.layout_dealer);
        this.playerLayout = (LinearLayout)findViewById(R.id.layout_player);

        this.textPlayer = findViewById(R.id.text_player);
        this.textDealer = findViewById(R.id.text_dealer);
        this.textMoney = findViewById(R.id.text_money);
        this.textBet = findViewById(R.id.text_bet);

        this.buttonHit = findViewById(R.id.button_hit);
        this.buttonPass = findViewById(R.id.button_pass);
        this.buttonDouble = findViewById(R.id.button_double);
        this.buttonSplit = findViewById(R.id.button_split);

        buttonHit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doHit();
            }
        });
        buttonPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doPass();
            }
        });
        buttonDouble.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doDouble();
            }
        });
        buttonSplit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doSplit();
            }
        });

        this.player = new Player(this.playerLayout, this.textPlayer, this.startingMoney);
        this.dealer = new Dealer(this.dealerLayout, this.textDealer);
        this.dealer.setHittingLimit(this.hittingLimit);
        this.reset();
    }

    //Reset player/dealer cards and the deck, then does the initial dealing. (Called onCreate and each hand)
    private void reset(){
        this.bet=10;
        this.dealer.reset();
        this.player.reset();
        this.deck = new Deck(this);
        this.textMoney.setText("$" + Float.toString(this.player.getMoney()));
        this.textBet.setText("$" + this.bet);
        this.initialDealing();
    }

    // Does the intial dealing. 2 cards to player and 1 to dealer. Checks if player got BJ after.
    private void initialDealing(){
        this.player.deal(this.deck.getACard());
        this.dealer.deal(this.deck.getACard());
        this.player.deal(this.deck.getACard());
        this.checkBlackJack();
    }

    // Player gets one card. (Linked to HIT button)
    public void doHit(){
        this.player.deal(this.deck.getACard());
        this.delayedCheck();
    }

    // Player passes. (Linked to PASS button)
    public void doPass(){
        while(this.dealer.getValue() < this.dealer.getHittingLimit()){
            this.dealer.deal(this.deck.getACard());
            this.delayedCheck();
        }
    }

    //TODO:Implement double and splits.
    //Player doubles. Doubles the amount of bet and only gets one card. (Linked to DOUBLE button)
    public void doDouble(){}
    //Player splits. (Linked to SPLIT button)
    public void doSplit(){}

    // Checks current game state.
    void checkState(){
        // Check for busts
        // player bust (loss)
        if (this.player.getValue() > 21) {
            System.out.println("1");
            this.playerLoss();
        }
        // dealer bust (win)
        else if (this.dealer.getValue() > 21) {
            System.out.println("2");
            this.playerWin();
        }

        // No player/dealer busts. Compare values.
        if(this.dealer.getValue() >= this.dealer.getHittingLimit()) {
            // player > dealer (win)
            if (this.player.getValue() > this.dealer.getValue()) {
                System.out.println("3");
                this.playerWin();
            }
            // player < dealer (loss)
            else if (this.player.getValue() < this.dealer.getValue()) {
                System.out.println("4");
                this.playerLoss();
            }
            // player == dealer (tie)
            else if (this.player.getValue() == this.dealer.getValue()){
                this.playerTie();
            }
        }
    }

    // Checks if the initial hand was a black jack.
    void checkBlackJack(){
        if (this.player.getValue() == 21){
            this.playerBlackJack();
        }
    }

    void playerBlackJack(){
        this.player.winMoney((float)(this.bet*this.blackJackMultiplier));
        //this.wait(3000);
        this.reset();
    }

    void playerWin(){
        this.player.winMoney(this.bet);
        //this.wait(3000);
        this.reset();
    }

    void playerLoss(){
        this.player.loseMoney(this.bet);
        //this.wait(3000);
        this.reset();
    }

    void playerTie(){
        this.reset();
    }

    public void delayedCheck(){
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run(){
                checkState();
            }
        }, 1000);
    }


    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex) {
            System.out.println("LMAO!");
            Thread.currentThread().interrupt();
        }
    }

    // Gets new deck. Invoked when we have gone through more than half the deck.
    private void getNewDeck() {
        if (deck.pastHalf()) {
            this.deck = new Deck(this);
        }
    }

}
