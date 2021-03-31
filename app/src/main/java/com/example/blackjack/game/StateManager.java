package com.example.blackjack.game;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;
import com.example.blackjack.players.Character;

import java.util.ArrayList;

public class StateManager {

    public static enum State{BLACKJACK, WIN, LOSS, DOUBLE_WIN, DOUBLE_LOSS, TIE, NONE}

    // Given player and dealer, check their hand and return the current gameState.
    public static State checkState(Character player, Dealer dealer, Boolean splitStatus, Boolean doubleStatus){
        // Check for BlackJack
        if (player.getNumCards() == 2 && player.getValue() == 21 && splitStatus == false){
            return State.BLACKJACK;
        }

        // Check for busts
        // player bust (loss)
        else if (player.getValue() > 21) {
            if(doubleStatus) return State.DOUBLE_LOSS;
            else return State.LOSS;
        }
        // dealer bust (win)
        else if (dealer.getValue() > 21) {
            if(doubleStatus) return State.DOUBLE_WIN;
            else return State.WIN;
        }

        // No player/dealer busts
        // Compare values.
        if(dealer.getValue() >= dealer.getHittingLimit()) {
            // player > dealer (win)
            if (player.getValue() > dealer.getValue()) {
                if(doubleStatus) return State.DOUBLE_WIN;
                else return State.WIN;
            }
            // player < dealer (loss)
            else if (player.getValue() < dealer.getValue()) {
                if(doubleStatus) return State.DOUBLE_LOSS;
                else return State.LOSS;
            }
            // player == dealer (tie)
            else if (player.getValue() == dealer.getValue()){
                return State.TIE;
            }
        }
        // No state. Player should continue.
        return State.NONE;
    }

    // Resolve current state(s) by adding/subtracting money from player.
    public static void resolveStates(ArrayList<State> states, Player player, int bet, float blackJackMultiplier) {

        for (State state : states) {
            switch (state) {
                case BLACKJACK:
                    player.winMoney(bet * blackJackMultiplier);
                    break;
                case WIN:
                    player.winMoney(bet);
                    break;
                case DOUBLE_WIN:
                    player.winMoney(bet * 2);
                    break;
                case LOSS:
                    player.loseMoney(bet);
                    break;
                case DOUBLE_LOSS:
                    player.loseMoney(bet * 2);
                    break;
                case TIE:
                    break;
            }
        }
    }

}
