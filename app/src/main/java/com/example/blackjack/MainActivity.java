package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // Starts the game.
    public void start(View view){
        Intent intent = new Intent(this, startGame.class);
        startActivity(intent);
    }

    //TODO!!
    public void settings(View view){
        // Adjust settings
    }

    //TODO!!
    public void about(View view){
        // Show about (link github, about myself etc)
    }
}
