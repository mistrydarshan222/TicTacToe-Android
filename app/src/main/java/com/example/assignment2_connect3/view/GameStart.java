package com.example.assignment2_connect3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.assignment2_connect3.R;



public class GameStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connect 3");

        EditText playerOneEditText = findViewById(R.id.player1);
        EditText playerTwoEditText = findViewById(R.id.player2);
        Button startGameButton = findViewById(R.id.startbutton);

        startGameButton.setOnClickListener(view -> {
            String playerOneName = playerOneEditText.getText().toString().trim();
            String playerTwoName = playerTwoEditText.getText().toString().trim();

            if (playerOneName.isEmpty()) {
                playerOneEditText.setError("Player 1 name is required");
                playerOneEditText.requestFocus();
            } else if (playerTwoName.isEmpty()) {
                playerTwoEditText.setError("Player 2 name is required");
                playerTwoEditText.requestFocus();
            } else {
                Intent intent = new Intent(GameStart.this, ConnectThree.class);
                intent.putExtra("PLAYER_ONE_NAME", playerOneName);
                intent.putExtra("PLAYER_TWO_NAME", playerTwoName);
                startActivity(intent);
            }
        });
    }
}


