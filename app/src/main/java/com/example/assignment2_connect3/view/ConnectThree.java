package com.example.assignment2_connect3.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.assignment2_connect3.R;
import com.example.assignment2_connect3.viewmodel.GameViewModel;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Objects;

public class ConnectThree extends AppCompatActivity {
    private GameViewModel gameViewModel;
    private GridLayout gameGrid;
    private View endGameLayout;
    private TextView congratsTextView;
    private final Handler handler = new Handler();
    private Animation congratsAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_three);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Connect Three");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finishAffinity());

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        gameGrid = findViewById(R.id.gameGrid);
        endGameLayout = findViewById(R.id.endGameLayout);
        congratsTextView = findViewById(R.id.congratsText);
        Button playAgainButton = findViewById(R.id.playAgain);
        Button quitButton = findViewById(R.id.quitButton);

        String playerOneName = getIntent().getStringExtra("PLAYER_ONE_NAME");
        String playerTwoName = getIntent().getStringExtra("PLAYER_TWO_NAME");
        gameViewModel.setPlayerNames(playerOneName, playerTwoName);

        playAgainButton.setOnClickListener(view -> {
            gameViewModel.resetGame();
            resetGridButtons();
            endGameLayout.setVisibility(View.GONE);
            congratsTextView.clearAnimation(); // Stop the animation
        });

        quitButton.setOnClickListener(view -> {
            congratsTextView.clearAnimation(); // Stop the animation
            finishAffinity(); // Close the app
        });

        // Observe game state changes
        gameViewModel.getGameState().observe(this, gameState -> {
            if (gameState != GameViewModel.GameState.ONGOING) {
                String winnerName = gameViewModel.getWinner().getValue();
                String message = "";

                if (gameState == GameViewModel.GameState.WIN) {
                    if (winnerName == null) {
                        winnerName = gameViewModel.getCurrentPlayer() == 1 ? gameViewModel.getPlayerOneName() : gameViewModel.getPlayerTwoName();
                    }
                    message = String.format("Congrats %s!<br/>You have won 🥳🥳", winnerName);
                } else if (gameState == GameViewModel.GameState.DRAW) {
                    message = "It's a Draw!";
                }

                congratsTextView.setText(android.text.Html.fromHtml(message));

                // Delay showing endGameLayout by 0.5 seconds after finishing the game
                handler.postDelayed(() -> {
                    endGameLayout.setVisibility(View.VISIBLE);
                    applyEndGameAnimation();
                }, 500); // 0.5 seconds delay
            }
        });

        // Initialize the game grid and logic
        initializeGameGrid();
    }

    private void initializeGameGrid() {
        int childCount = gameGrid.getChildCount();
        for (int i = 0; i < childCount; i++) {
            FrameLayout frameLayout = (FrameLayout) gameGrid.getChildAt(i);
            ImageView button = (ImageView) frameLayout.getChildAt(0);
            int position = i; // 1D position on the game board
            button.setOnClickListener(view -> {
                if (button.isEnabled() && gameViewModel.getGameState().getValue() == GameViewModel.GameState.ONGOING) {
                    int player = gameViewModel.getCurrentPlayer();
                    updateButtonState(button, player);
                    gameViewModel.makeMove(position);
                }
            });
        }
    }

    private void updateButtonState(ImageView button, int player) {
        if (player == 1) {
            button.setImageResource(R.drawable.cross); // Ensure this resource exists
        } else if (player == 2) {
            button.setImageResource(R.drawable.round); // Ensure this resource exists
        }
        button.setEnabled(false); // Disable the button after it's clicked

        // Apply animation
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        button.startAnimation(scaleUp);
    }

    private void resetGridButtons() {
        int childCount = gameGrid.getChildCount();
        for (int i = 0; i < childCount; i++) {
            FrameLayout frameLayout = (FrameLayout) gameGrid.getChildAt(i);
            ImageView button = (ImageView) frameLayout.getChildAt(0);
            button.setImageResource(0); // Clear the image
            button.setEnabled(true); // Enable the button
        }
    }

    private void disableGridButtons() {
        int childCount = gameGrid.getChildCount();
        for (int i = 0; i < childCount; i++) {
            FrameLayout frameLayout = (FrameLayout) gameGrid.getChildAt(i);
            ImageView button = (ImageView) frameLayout.getChildAt(0);
            button.setEnabled(false); // Disable all buttons
        }
    }

    private void applyEndGameAnimation() {
        congratsAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_and_scale);
        congratsAnimation.setRepeatCount(Animation.INFINITE);
        congratsTextView.startAnimation(congratsAnimation);
    }
}
