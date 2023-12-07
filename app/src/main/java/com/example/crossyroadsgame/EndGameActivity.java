package com.example.crossyroadsgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;


import androidx.appcompat.app.AppCompatActivity;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_end);

        int score = getIntent().getIntExtra("SCORE", 0);

        TextView gameOverText = findViewById(R.id.gameOverText);
        TextView userScoreTextView = findViewById(R.id.userScoreTextView);
        TextView highScoreTextView = findViewById(R.id.highScoreTextView);

        // Set the score in the UI
        userScoreTextView.setText("Your Score: " + score);

        // Retrieve the high score from SharedPreferences

        int highScore = getHighScore();
        highScoreTextView.setText("High Score: " + highScore);



        // Check if the current score is higher than the stored high score
        if (score > highScore) {
            // Update the high score in SharedPreferences
            updateHighScore(score);
            highScoreTextView.setText("New High Score: " + score);
        }



        Button replayButton = findViewById(R.id.replayButton);
        Button closeButton = findViewById(R.id.closeButton);


        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replayGame();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGameOverScreen();
            }
        });
    }

    // Add these methods for updating and retrieving the high score

    private void updateHighScore(int newHighScore) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HIGH_SCORE", newHighScore);
        editor.apply();
    }

    private int getHighScore() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return preferences.getInt("HIGH_SCORE", 0);
    }
    public static Intent newIntent(Context context, int score) {
        Intent intent = new Intent(context, EndGameActivity.class);
        intent.putExtra("SCORE", score);
        return intent;
    }


    public void replayGame() {
        // Set the score to 0 before starting a new gameplay session
        int score = 0;

        // Start the existing GameplayActivity and pass the score
        startActivity(GameplayActivity.newIntent(this, score));

        // Note: Do not finish the current activity to keep the end game screen open
    }


    public void closeGameOverScreen() {
        // Finish the current activity
        finish();

        // Start the StartGameActivity or the activity responsible for starting a new game
        startActivity(new Intent(this, StartActivity.class));
    }
}
