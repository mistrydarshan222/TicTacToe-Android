package com.example.assignment2_connect3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {

    public enum GameState {
        ONGOING, WIN, DRAW
    }

    private final MutableLiveData<GameState> gameState = new MutableLiveData<>(GameState.ONGOING);
    private final MutableLiveData<String> winner = new MutableLiveData<>();
    private final int[] gameBoard = new int[9]; // Represents the game board in a 1D array
    private final int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
            {0, 4, 8}, {2, 4, 6} // diagonals
    };
    private int currentPlayer = 1; // 1 for Player 1 (X), 2 for Player 2 (O)
    private String playerOneName;
    private String playerTwoName;
    private int moveCount = 0; // Count of moves made

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public LiveData<GameState> getGameState() {
        return gameState;
    }

    public LiveData<String> getWinner() {
        return winner;
    }

    public void setPlayerNames(String playerOneName, String playerTwoName) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void makeMove(int position) {
        if (gameBoard[position] == 0 && gameState.getValue() == GameState.ONGOING) {
            gameBoard[position] = currentPlayer;
            moveCount++;
            if (checkForWinOrDraw()) {
                return;
            }
            currentPlayer = currentPlayer == 1 ? 2 : 1; // Switch player
        }
    }

    public void resetGame() {
        for (int i = 0; i < gameBoard.length; i++) {
            gameBoard[i] = 0;
        }
        currentPlayer = 1;
        moveCount = 0;
        gameState.setValue(GameState.ONGOING);
        winner.setValue(null);
    }

    private boolean checkForWinOrDraw() {
        for (int[] winningPosition : winningPositions) {
            if (gameBoard[winningPosition[0]] == gameBoard[winningPosition[1]]
                    && gameBoard[winningPosition[1]] == gameBoard[winningPosition[2]]
                    && gameBoard[winningPosition[0]] != 0) {

                gameState.setValue(GameState.WIN);
                String winnerName = currentPlayer == 1 ? playerOneName : playerTwoName;
                winner.setValue(winnerName);
                return true;
            }
        }
        if (moveCount == 9) {
            gameState.setValue(GameState.DRAW);
            return true;
        }
        return false;
    }
}
