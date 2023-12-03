package com.example.crossyroadsgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameplayActivity extends AppCompatActivity {

    private static final int NUM_INITIAL_GRASS_ROWS = 10;
    private static final int ROW_HEIGHT_DP = 85;
    private boolean userClickedUpArrow = false;
    private LinearLayout llGameContainer;
    private ImageView ivCharacter;

    private static final int CHARACTER_MOVE_DISTANCE = 100; // Adjust this value as needed

    private int characterPositionX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        llGameContainer = findViewById(R.id.llGameContainer);
        ivCharacter = findViewById(R.id.ivCharacter);

        findViewById(R.id.upArrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the upward arrow click
                userClickedUpArrow = true;
            }
        });

        findViewById(R.id.leftArrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the left arrow click
                moveCharacterLeft();
            }
        });

        findViewById(R.id.rightArrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the right arrow click
                moveCharacterRight();
            }
        });

        // Add initial grass rows
        for (int i = 0; i < NUM_INITIAL_GRASS_ROWS; i++) {
            addGrassRow();
        }

        // Start the game loop
        startGameLoop();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, GameplayActivity.class);
    }

    private void addGrassRow() {
        ImageView grassRow = new ImageView(this);
        grassRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) dpToPx(ROW_HEIGHT_DP)));
        grassRow.setImageResource(R.drawable.grass);
        grassRow.setScaleType(ImageView.ScaleType.FIT_XY);
        llGameContainer.addView(grassRow);
    }

    private void startGameLoop() {
        final Handler handler = new Handler();

        // Start the game loop immediately
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Game loop logic goes here
                // Check if the up arrow is clicked
                if (userClickedUpArrow) {
                    // Remove the bottom row
                    removeBottomRow();

                    // Add a new row at the top
                    addRandomRowAtTop();

                    // Reset the user input flag
                    userClickedUpArrow = false;
                }

                // Check again after a delay
                handler.postDelayed(this, 100); // Adjust the delay as needed
            }
        });
    }

    private void moveCharacterLeft() {
        // Calculate the new position
        characterPositionX -= CHARACTER_MOVE_DISTANCE;
        updateCharacterPosition();
    }

    private void moveCharacterRight() {
        // Calculate the new position
        characterPositionX += CHARACTER_MOVE_DISTANCE;
        updateCharacterPosition();
    }

    private int getMaxScreenX() {
        // Calculate and return the maximum X coordinate of the screen
        // You can adjust this based on your screen size and character size
        return Math.max(0, llGameContainer.getWidth() - ivCharacter.getWidth());
    }

    private void updateCharacterPosition() {
        // Update the UI to reflect the new character position
        // For example, you can set the translationX property of ivCharacter
        // You may need to use ObjectAnimator or setTranslationX() based on your UI structure
        characterPositionX = Math.max(-500, Math.min(characterPositionX, 500));
        ivCharacter.setTranslationX(characterPositionX);
    }

    private void removeBottomRow() {
        // Remove the bottom row from llGameContainer
        if (llGameContainer.getChildCount() > 0) {
            llGameContainer.removeViewAt(llGameContainer.getChildCount() - 1);
        }
    }

    private void addRandomRowAtTop() {
        // Add a random row at the top of llGameContainer (either grass or concrete)
        if (new Random().nextBoolean()) {
            addGrassRowAtTop();
        } else {
            addConcreteRowAtTop();
        }
    }

    private void addGrassRowAtTop() {
        // Add a grass row at the top of llGameContainer
        ImageView grassRow = new ImageView(this);
        grassRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) dpToPx(ROW_HEIGHT_DP)));
        grassRow.setImageResource(R.drawable.grass);
        grassRow.setScaleType(ImageView.ScaleType.FIT_XY);

        llGameContainer.addView(grassRow, 0); // Add at the beginning
    }

    private void addConcreteRowAtTop() {
        // Add a concrete row at the top of llGameContainer
        ImageView concreteRow = new ImageView(this);
        concreteRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) dpToPx(ROW_HEIGHT_DP)));
        concreteRow.setImageResource(R.drawable.concrete);
        concreteRow.setScaleType(ImageView.ScaleType.FIT_XY);
        llGameContainer.addView(concreteRow, 0); // Add at the beginning
    }

    private float dpToPx(float dp) {
        // Helper method to convert density-independent pixels (dp) to pixels (px)
        float density = getResources().getDisplayMetrics().density;
        return dp * density;
    }
}
