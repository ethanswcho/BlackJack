package com.example.blackjack.game;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blackjack.R;
import com.example.blackjack.deck.Deck;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    //public static Context context;

    Deck deck;
    Player player;
    Dealer dealer;
    LinearLayout dealerLayout, playerLayout, postGameLayout;
    Button buttonHit, buttonPass, buttonDouble, buttonSplit, buttonPlayAgain;
    TextView textPlayer, textDealer, textMoney, textBet, textError, textStatus, textWinLossAmount, textTotal;
    EditText textBetAmount;
    View currentLayout;
    ArrayList<View> mainGameViews;

    // TODO: Customizable odds are below
    int bet = 10;
    int hittingLimit = 17;
    float blackJackMultiplier = 1.5f;
    int startingMoney = 10000;
    enum Check{BlackJack, Hit, Double}

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.currentLayout = findViewById(android.R.id.content);
        this.dealerLayout = findViewById(R.id.layout_dealer);
        this.playerLayout = findViewById(R.id.layout_player);
        this.postGameLayout = findViewById(R.id.layout_postgame);

        this.textPlayer = findViewById(R.id.text_player);
        this.textDealer = findViewById(R.id.text_dealer);
        this.textMoney = findViewById(R.id.text_money);
        this.textBet = findViewById(R.id.text_bet);
        this.textBetAmount = findViewById(R.id.text_betamount);
        this.textError = findViewById(R.id.text_error);
        this.textStatus = findViewById(R.id.text_status);
        this.textWinLossAmount = findViewById(R.id.text_winlossamount);
        this.textTotal = findViewById(R.id.text_total);

        this.buttonHit = findViewById(R.id.button_hit);
        this.buttonPass = findViewById(R.id.button_pass);
        this.buttonDouble = findViewById(R.id.button_double);
        this.buttonSplit = findViewById(R.id.button_split);

        this.buttonHit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doHit();
            }
        });
        this.buttonPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doPass();
            }
        });
        this.buttonDouble.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doDouble();
            }
        });
        this.buttonSplit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doSplit();
            }
        });
        this.buttonPlayAgain = findViewById(R.id.button_playagain);
        this.buttonPlayAgain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                playAgain();
            }
        });

        this.player = new Player(this.playerLayout, this.textPlayer, this.startingMoney);
        this.dealer = new Dealer(this.dealerLayout, this.textDealer);
        this.dealer.setHittingLimit(this.hittingLimit);
        this.postGameLayout.setVisibility(View.INVISIBLE);
        this.mainGameViews = TransitionManager.getGameViews(this.currentLayout, this.postGameLayout);

        this.h = new Handler();
        this.reset();
    }

    //Reset player/dealer cards and the deck, then does the initial dealing. (Called onCreate and each hand)
    private void reset(){
        this.dealer.reset();
        this.player.reset();
        this.deck = new Deck(this);
        this.textMoney.setText("$" + Float.toString(this.player.getMoney()));
        this.textBet.setText("$" + this.bet);
        this.textError.setVisibility(View.INVISIBLE);
        this.initialDealing();
    }


    // When PlayAgain button is pressed, validate the bet. If bet is good, reset and restart the game.
    private void playAgain(){
        BetManager.betState bs = BetManager.checkBet(this.textBetAmount, this.player.getMoney());
        if(bs == BetManager.betState.OK){
            this.bet = BetManager.getIntBet(this.textBetAmount);
            this.reset();
            TransitionManager.preGameTransition(mainGameViews, postGameLayout);
        }
        else{
            BetManager.setErrorMessage(bs, this.textError);
            this.textError.setVisibility(View.VISIBLE);
        }
    }

    // Does the initial dealing. 2 cards to player and 1 to dealer. Checks if player got BJ after.
    private void initialDealing(){
        this.player.deal(this.deck.getACard());
        this.dealer.deal(this.deck.getACard());
        this.player.deal(this.deck.getACard());
        this.delayedCheck();
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
        }
        this.delayedCheck();
    }

    //TODO:Implement double and splits.
    //Player doubles. Doubles the amount of bet and only gets one card. (Linked to DOUBLE button)
    public void doDouble(){
        this.player.deal(this.deck.getACard());
        this.delayedCheck();
        while(this.dealer.getValue() < this.dealer.getHittingLimit()){
            this.dealer.deal(this.deck.getACard());
            this.delayedCheck();
        }
    }
    //Player splits. (Linked to SPLIT button)
    public void doSplit(){}

    // Checks current game state and resolves it if need be.
    // If the game is resolved (player wins/loses), then transitions to post-game UI.
    public void delayedCheck(){
        StateManager.State state = StateManager.checkState(player, dealer);
        if(state != StateManager.State.NONE){
            StateManager.resolveState(state, player, bet, blackJackMultiplier);
            TransitionManager.preparePostGameLayout(textStatus, textWinLossAmount, textTotal,
                    bet, player.getMoney(), blackJackMultiplier, state);
            TransitionManager.postGameTransition(mainGameViews, postGameLayout);
        }
    }

    /*
    public void delayedCheck(){
        //Handler h = new Handler();
        this.h.postDelayed(new Runnable() {
            @Override
            public void run(){
                StateManager.State state = StateManager.checkState(player, dealer);
                if(state != StateManager.State.NONE){
                    StateManager.resolveState(state, player, bet, blackJackMultiplier);
                    TransitionManager.preparePostGameLayout(textStatus, textWinLossAmount, textTotal,
                            bet, player.getMoney(), state);
                    TransitionManager.postGameTransition(mainGameViews, postGameLayout);
                }
            }
        }, 2000);
    }

     */
}
