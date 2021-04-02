package com.example.blackjack.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView arrowDealer, arrowPlayer, arrowSplitter;
    View currentLayout;
    ArrayList<View> mainGameViews;
    Boolean splitStatus, doubleStatus;
    Handler handler;
    Runnable delayedDeal, delayedCheck;

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

        this.arrowDealer = findViewById(R.id.arrow_dealer);
        this.arrowPlayer = findViewById(R.id.arrow_player);
        this.arrowSplitter = findViewById(R.id.arrow_splitter);

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

        this.handler = new Handler(Looper.getMainLooper());
        this.delayedCheck = new Runnable(){
            @Override
            public void run(){
                check();
            }
        };
        this.delayedDeal = new Runnable() {
            @Override
            public void run() {
                dealer.deal(deck.getACard());
                // Until dealer's value exceeds hitting limit, keep hitting.
                if (dealer.getValue() < dealer.getHittingLimit()) {
                    handler.postDelayed(this, 1000);
                }
                else{
                    handler.postDelayed(delayedCheck, 1000);
                }
            }
        };

        this.player = new Player(this.playerLayout, this.textPlayer, this.arrowPlayer, this.startingMoney);
        this.splitter = new Splitter(this.splitterLayout, this.textSplitter, this.arrowSplitter, this.textSplitterBust);
        this.dealer = new Dealer(this.dealerLayout, this.textDealer, this.arrowDealer);
        this.dealer.setHittingLimit(this.hittingLimit);
        this.mainGameViews = TransitionManager.getGameViews(this.currentLayout, this.postGameLayout);
        this.reset();
    }

    //Reset player/dealer cards and the deck, then does the initial dealing. (Called onCreate and each hand)
    private void reset(){
        this.doubleStatus = false;
        this.currentCharacter = this.player;
        this.dealer.reset();
        this.player.reset();
        this.deck = new Deck(this);
        this.textMoney.setText("$" + Float.toString(this.player.getMoney()));
        this.textBet.setText("$" + this.bet);
        this.postGameLayout.setVisibility(View.INVISIBLE);
        this.textError.setVisibility(View.INVISIBLE);
        this.buttonDouble.setEnabled(true);
        this.buttonSplit.setEnabled(false);
        this.resetSplitter();
        this.currentCharacter.enableArrow();
        this.dealer.disableArrow();
        this.initialDealing();
    }

    //TODO: probbly belongs in splitter
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
            TransitionManager.preGameTransition(mainGameViews, postGameLayout);
            this.reset();
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
        if(this.player.canSplit() == true){
            this.buttonSplit.setEnabled(true);
        }
        this.check();
    }

    // current character gets one card. (Linked to HIT button)
    // After a player hits, dopuble cannot be done. So disable the double button.
    public void doHit(){
        this.currentCharacter.deal(this.deck.getACard());
        this.buttonDouble.setEnabled(false);
        this.buttonSplit.setEnabled(false);
        this.check();
    }

    // Player passes. (Linked to PASS button)
    public void doPass(){
        //If a player passed after doing split, then we save current state into Splitter, then
        // move onto the players hand.
        if(this.currentCharacter.getClass() == Splitter.class){
            splitTransition();
        }
        // Regular (non-split) pass. Dealer will hit until its hands value is past the hitting limit.
        // Will display a new card every 1 second for animation effect.
        // After the dealer's value exceeds its hitting limit, will execute delayedCheck() after 1 second.
        else {
            this.playerTransition();
            this.disableButtons();
            this.handler.postDelayed(delayedDeal, 1000);
        }
    }

    // Disable the four clickable buttons.
    // Used during doPass do ensure players cannot overload handler callbacks
    private void disableButtons(){
        this.buttonDouble.setEnabled(false);
        this.buttonHit.setEnabled(false);
        this.buttonPass.setEnabled(false);
        this.buttonSplit.setEnabled(false);
    }

    //Player doubles. Doubles the amount of bet, gets only one card and then passes. (Linked to DOUBLE button)
    public void doDouble(){
        this.doubleStatus = true;
        this.currentCharacter.deal(this.deck.getACard());
        // Double and the hand busted
        if(this.currentCharacter.isBusted()){
            // If this is a split hand that busted, then we need to move onto the main hand
            if(this.splitStatus == true && this.currentCharacter.getClass() == Splitter.class){
                this.splitTransition();
            }
            // If player hand busted, then end the game here after 1s, so player can see their bust
            else{
                this.handler.postDelayed(delayedCheck, 1000);
            }
        }
        else{
            this.doPass();
        }
    }
    //Player splits. (Linked to SPLIT button)
    public void doSplit(){
        this.splitStatus = true;
        Card splitCard = this.player.popCard();
        this.splitter.deal(splitCard);
        this.splitter.displayUI();
        this.splitter.enableArrow();
        this.player.disableArrow();
        this.currentCharacter = this.splitter;
    }

    // Saves current splitter's state, then move onto the players hand.
    // Also disables buttonSplit since only one split is allowed per hand.
    private void splitTransition(){
        this.buttonSplit.setEnabled(false);
        this.splitter.setState(StateManager.State.NONE);
        this.splitter.setDoubleStatus(this.doubleStatus);
        this.doubleStatus = false;
        this.splitter.disableArrow();
        this.currentCharacter = this.player;
        this.currentCharacter.enableArrow();
        this.buttonDouble.setEnabled(true);
    }

    private void playerTransition(){
        this.currentCharacter.disableArrow();
        this.dealer.enableArrow();
    }

    // Checks current game state and resolves it if need be.
    // If the game is resolved (player wins/loses), then transitions to post-game UI.
    public void check(){
        StateManager.State state = StateManager.checkState(currentCharacter, dealer,
                this.splitStatus, this.doubleStatus);

        if(state != StateManager.State.NONE) {
            // If the current state is for splitter, save current state of splitter to be used later
            // and move onto player
            if (currentCharacter.getClass() == Splitter.class) {
                splitTransition();
            }
            // Update state for player, then resolve current state, then perform postGameTransition
            else {
                // If split happened and the split hand is waiting dealer
                // e.g. split hand didn't bust, but player hand did. Then the game shouldn't end there,
                // we need the dealer to get his cards so we can evaluate the split hand.
                if (this.splitter.isWaitingDealer()){
                    this.splitter.setWaitingDealer(false);
                    this.doPass();
                }
                ArrayList<StateManager.State> states = new ArrayList<>();
                states.add(state);
                if(this.splitStatus == true){
                    // It's possible that the state was not saved initially for splitter because it depended on dealer's hand.
                    // If that is the case, we will re-check the state since dealer has been dealt its cards
                    if(this.splitter.getState() == StateManager.State.NONE){
                        this.splitter.setState(StateManager.checkState(this.splitter, dealer,
                                this.splitStatus, this.splitter.getDoubleStatus()));
                    }
                    states.add(this.splitter.getState());
                }
                StateManager.resolveStates(states, player, bet, blackJackMultiplier);
                TransitionManager.preparePostGameLayout(statusLayout1, statusLayout2,
                        textTotalMoney, textBetAmount, bet, player.getMoney(), blackJackMultiplier, states);

                // If it is 2-card BJ or player just hit a card that is about to make them bust
                // Let them have 1 second to view their field instead of moving immediately to post game transition
                if(state == StateManager.State.BLACKJACK || this.dealer.getNumCards() == 1){
                    Runnable delayedTransition = new Runnable(){
                        @Override
                        public void run(){
                            TransitionManager.postGameTransition(mainGameViews, postGameLayout, splitStatus, statusLayout1);
                        }
                    };
                    handler.postDelayed(delayedTransition, 1000);
                }
                else {
                    TransitionManager.postGameTransition(mainGameViews, postGameLayout, this.splitStatus, statusLayout1);
                }
            }
        }
    }
}
