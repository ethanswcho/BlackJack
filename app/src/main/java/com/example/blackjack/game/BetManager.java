package com.example.blackjack.game;

import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

public class BetManager {

    public enum betState{OK, TOO_MUCH, EMPTY, ZERO}

    // Checks the validity of current bet
    public static betState checkBet(EditText et, float limit) {
        // No Input
        if (betIsEmpty(et)) {
            return betState.EMPTY;
        }
        else {
            int intBet = getIntBet(et);
            // Bet is too large (bet > player.money)
            if (intBet > limit) {
                return betState.TOO_MUCH;
            }
            // bet is zero
            else if (intBet == 0){
                return betState.ZERO;
            }
            // bet is fine
            else {
                return betState.OK;
            }
        }
    }

    // Checks if there are no inputs on the bet edit text.
    private static boolean betIsEmpty(EditText et){
        return et.getText().length() == 0;
    }

    public static int getIntBet(EditText et){
        return Integer.parseInt(et.getText().toString());
    }

    public static void setErrorMessage(betState bs, TextView t){
        String errorMessage = "If you are seeing this please let me know (this is a bug)";
        switch(bs){
            case TOO_MUCH:
                errorMessage = "<font color=#ff0000>Error! </font> <br> Your bet cannot exceed \n your money.";
                break;
            case EMPTY:
                errorMessage = "<font color=#ff0000>Error! </font> <br> Your bet is empty.";
            case ZERO:
                errorMessage = "<font color=#ff0000>Error! </font> <br> Your bet cannot be zero.";
        }
        t.setText(Html.fromHtml(errorMessage));
    }

}
