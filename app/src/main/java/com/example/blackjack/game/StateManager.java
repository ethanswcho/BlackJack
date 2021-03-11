package com.example.blackjack.game;
import com.example.blackjack.players.Dealer;
import com.example.blackjack.players.Player;

public class StateManager {

    public static enum State{BLACKJACK, WIN, LOSS, TIE, NONE}

    // Given player and dealer, check their hand and return the current gameState.
    public static State checkState(Player player, Dealer dealer){
        // Check for BlackJack
        if (player.getNumCards() == 2 && player.getValue() == 21){
            return State.BLACKJACK;
        }

        // Check for busts
        // player bust (loss)
        else if (player.getValue() > 21) {
            return State.LOSS;
        }
        // dealer bust (win)
        else if (dealer.getValue() > 21) {
            return State.WIN;
        }

        // No player/dealer busts
        // Compare values.
        if(dealer.getValue() >= dealer.getHittingLimit()) {
            // player > dealer (win)
            if (player.getValue() > dealer.getValue()) {
                return State.WIN;
            }
            // player < dealer (loss)
            else if (player.getValue() < dealer.getValue()) {
                return State.LOSS;
            }
            // player == dealer (tie)
            else if (player.getValue() == dealer.getValue()){
                return State.TIE;
            }
        }
        // No state. Player should continue.
        return State.NONE;
    }

    // Resolve current state by adding/subtracting money from player.
    public static void resolveState(State state, Player player, int bet, float blackJackMultiplier){
        switch(state){
            case BLACKJACK:
                player.winMoney(bet * blackJackMultiplier);
                break;
            case WIN:
                player.winMoney(bet);
                break;
            case LOSS:
                player.loseMoney(bet);
                break;
            case TIE:
                break;
        }
    }

}
