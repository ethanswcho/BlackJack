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
import com.example.blackjack.deck.Card;
import com.example.blackjack.deck.Deck;
import com.example.blackjack.players.Character;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;
import com.example.blackjack.players.Splitter;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    //public static Context context;
    Deck deck;
    Player player;
    Splitter splitter;
    Dealer dealer;
    Character currentCharacter;
    LinearLayout dealerLayout, playerLayout, splitterLayout, statusLayout1, statusLayout2, postGameLayout;
    Button buttonHit, buttonPass, buttonDouble, buttonSplit, buttonPlayAgain;
    TextView textPlayer, textSplitter, textDealer, textMoney, textBet, textError,
             textSplitterBust, textTotalMoney, textTotal;
    EditText textBetAmount;
    View currentLayout;
    ArrayList<View> mainGameViews;
    Boolean splitStatus, doubleStatus;

    // TODO: Customizable odds are below
    int bet = 10;
    int hittingLimit = 17;
    float blackJackMultiplier = 1.5f;
    int startingMoney = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.currentLayout = findViewById(android.R.id.content);
        this.dealerLayout = findViewById(R.id.layout_dealer);
        this.playerLayout = findViewById(R.id.layout_player);
        this.splitterLayout = findViewById(R.id.layout_splitter);
        this.statusLayout1 = findViewById(R.id.layout_status1);
        this.statusLayout2 = findViewById(R.id.layout_status2);
        this.postGameLayout = findViewById(R.id.layout_postgame);

        this.textPlayer = findViewById(R.id.text_player);
        this.textSplitter = findViewById(R.id.text_splitter);
        this.textDealer = findViewById(R.id.text_dealer);
        this.textMoney = findViewById(R.id.text_money);
        this.textBet = findViewById(R.id.text_bet);
        this.textBetAmount = findViewById(R.id.text_betamount);
        this.textError = findViewById(R.id.text_error);
        this.textSplitterBust = findViewById(R.id.text_splitterBust);
        this.textTotal = findViewById(R.id.text_total);
        this.textTotalMoney = findViewById(R.id.text_totalMoney);

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
        this.splitter = new Splitter(this.splitterLayout, this.textSplitter, this.textSplitterBust);
        this.dealer = new Dealer(this.dealerLayout, this.textDealer);
        this.dealer.setHittingLimit(this.hittingLimit);
        this.mainGameViews = TransitionManager.getGameViews(this.currentLayout, this.postGameLayout);
        this.reset();
    }

    //Reset player/dealer cards and the deck, then does the initial dealing. (Called onCreate and each hand)
    private void reset(){
        this.doubleStatus = false;
        this.postGameLayout.setVisibility(View.INVISIBLE);
        this.currentCharacter = this.player;
        this.dealer.reset();
        this.player.reset();
        this.deck = new Deck(this);
        this.textMoney.setText("$" + Float.toString(this.player.getMoney()));
        this.textBet.setText("$" + this.bet);
        this.textError.setVisibility(View.INVISIBLE);
        this.resetSplitter();
        this.initialDealing();
    }

    // Reset elements related to the splitter.
    private void resetSplitter(){
        this.splitStatus = false;
        this.splitter.hideUI();
        this.splitter.reset();
    }

    // When PlayAgain button is pressed, validate the bet. If the bet is good, reset and restart the game.
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
        if(this.player.canSplit() == false){
            this.buttonSplit.setEnabled(false);
        }
        this.delayedCheck();
    }

    // current character gets one card. (Linked to HIT button)
    public void doHit(){
        this.currentCharacter.deal(this.deck.getACard());
        this.delayedCheck();
    }

    // Player passes. (Linked to PASS button)
    public void doPass(){
        //If a player passed after doing split, then they need to move onto players hand from splitter's hand.
        if(this.currentCharacter.getClass() == Splitter.class){
            this.currentCharacter = this.player;
            this.buttonSplit.setEnabled(false);
            return;
        }
        // Regular pass. Dealer will hit until its hands value is past the hitting limit.
        else {
            while (this.dealer.getValue() < this.dealer.getHittingLimit()) {
                this.dealer.deal(this.deck.getACard());
            }
        }
        this.delayedCheck();
    }

    //TODO:Implement double and splits.
    //Player doubles. Doubles the amount of bet and only gets one card. (Linked to DOUBLE button)
    public void doDouble(){
        this.doubleStatus = true;
        this.currentCharacter.deal(this.deck.getACard());
        this.delayedCheck();
        while(this.dealer.getValue() < this.dealer.getHittingLimit()){
            this.dealer.deal(this.deck.getACard());
            this.delayedCheck();
        }
    }
    //Player splits. (Linked to SPLIT button)
    public void doSplit(){
        this.splitStatus = true;
        Card splitCard = this.player.popCard();
        this.splitter.deal(splitCard);
        this.splitter.displayUI();
        this.currentCharacter = this.splitter;
    }

    // Checks current game state and resolves it if need be.
    // If the game is resolved (player wins/loses), then transitions to post-game UI.
    public void delayedCheck(){

        StateManager.State state = StateManager.checkState(currentCharacter, dealer, this.splitStatus, this.doubleStatus);

        if(state != StateManager.State.NONE) {
            // If the current state is for splitter, save current state of splitter to be used later
            // and move onto player
            if (currentCharacter.getClass() == Splitter.class) {
                this.buttonSplit.setEnabled(false);
                this.splitter.setState(state);
                this.doubleStatus = false;
                this.currentCharacter = this.player;
            }
            // Update state for player, then resolve current state, then perform postGameTransition
            else {
                ArrayList<StateManager.State> states = new ArrayList<>();
                states.add(state);
                if(this.splitStatus == true){
                    // It's possible that the state was not saved initially for splitter because it depended on the dealer values.
                    // If that is the case, we will re-check the state since we have dealer values now.
                    if(this.splitter.getState() == StateManager.State.NONE){
                        this.splitter.setState(StateManager.checkState(this.splitter, dealer, this.splitStatus, this.splitter.getDoubleStatus()));
                    }
                    states.add(this.splitter.getState());
                }
                StateManager.resolveStates(states, player, bet, blackJackMultiplier);
                TransitionManager.preparePostGameLayout(statusLayout1, statusLayout2,
                        textTotalMoney, bet, player.getMoney(), blackJackMultiplier, states);
                TransitionManager.postGameTransition(mainGameViews, postGameLayout, this.splitStatus, statusLayout1);
            }
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
