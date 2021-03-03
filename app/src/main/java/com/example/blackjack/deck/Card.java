package com.example.blackjack.deck;

import android.content.res.Resources;
import android.widget.ImageView;

public class Card {

    public enum Suit {Spade, Club, Heart, Diamond}

    public enum Name {
        Ace(1), Two(2), Three(3), Four(4), Five(5), Six(6),
        Seven(7), Eight(8), Nine(9), Ten(10), Jack(10), Queen(10), King(10);

        private int value;
        Name(int v){
            this.value=v;
        }
        public int getValue(){
            return value;
        }
    }

    Suit suit;
    Name name;
    int value;

    // Initialize card with given suite, name, and value that matches the name
    public Card(Suit s, Name n){
        suit=s;
        name=n;
        value=n.getValue();
    }

    // Get a string that appended Name + Suit of the card - used to find image files of the card
    private String getImageName(){
        return this.name.name() + this.suit.name();
    }

    public ImageView getCardPhoto(){
        Resources res = Resources.getResou

    }

    public Suit getSuit(){
        return this.suit;
    }

    public Name getName(){
        return this.name;
    }

    public int getValue(){
        return this.value;
    }

}
