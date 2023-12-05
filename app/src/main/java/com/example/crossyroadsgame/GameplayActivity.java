package com.example.crossyroadsgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class GameplayActivity extends AppCompatActivity {

    private static final int NUM_INITIAL_GRASS_ROWS = 10;
    private static final int ROW_HEIGHT_DP = 85;
    private boolean userClickedUpArrow = false;
    private LinearLayout llGameContainer;
    private ImageView ivCharacter;
    private static final int CHARACTER_MOVE_DISTANCE = 125;
    private int characterPositionX = 0;

    private int score = 0;
    private TextView scoreTextView;
    private ArrayList<Car> carList = new ArrayList<>();


    private static final int MAX_TREES_PER_ROW = 3; // Adjust based on screen size
    private static final int TREE_SPACING_DP = 200; // Adjust for spacing between trees
    private ArrayList<ImageView> treeImageViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        llGameContainer = findViewById(R.id.llGameContainer);
        ivCharacter = findViewById(R.id.ivCharacter);

        scoreTextView = findViewById(R.id.scoreTextView);

        findViewById(R.id.upArrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the upward arrow click
                userClickedUpArrow = true;
                updateScore(score + 1);
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

    private void updateScore(int newScore) {
        score = newScore;
        scoreTextView.setText("Score: " + score);
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

                // Check for collisions on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkCollisions();
                    }
                });

                // Move the car on the main thread
                for (int i = carList.size() - 1; i >= 0; i--) {
                    final Car car = carList.get(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            car.move(llGameContainer.getWidth());
                        }
                    });
                }

                // Check if the up arrow is clicked
                if (userClickedUpArrow) {
                    // Remove the bottom row
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            removeBottomRow();
                        }
                    });

                    // Add a new row at the top
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addRandomRowAtTop();
                        }
                    });

                    // Reset the user input flag
                    userClickedUpArrow = false;
                }

                // Check again after a delay
                handler.postDelayed(this, 100); // Adjust the delay as needed
            }
        });
    }

    public void checkCollisions() {
        Log.d("Collision", "Checking collisions...");

        boolean collisions = false;

        // Get the character's position and dimensions
        float characterX = ivCharacter.getX();
        float characterY = ivCharacter.getY();
        int characterWidth = ivCharacter.getWidth();
        int characterHeight = ivCharacter.getHeight();

        Log.d("Character", "X: " + characterX + ", Y: " + characterY +
                ", Width: " + characterWidth + ", Height: " + characterHeight);

        // Check for collisions with cars
        for (Car car : carList) {
            if (car.collidesWithPlayer(characterX, characterY, characterWidth, characterHeight)) {
                Log.d("Collision", "Collision with car!");
                handleCollision();
            }
        }
    }

    private void handleCollision() {
        Log.d("Collision", "Collision detected! Game Over!");
        finish();
        // You can also start a new activity for the end game screen using an Intent.
        // Example: startActivity(new Intent(this, EndGameActivity.class));
        // For now, let's finish the current activity (GameplayActivity).
    }

    private void moveCharacterLeft() {
        characterPositionX -= CHARACTER_MOVE_DISTANCE;
        updateCharacterPosition();
    }

    private void moveCharacterRight() {
        characterPositionX += CHARACTER_MOVE_DISTANCE;
        updateCharacterPosition();
    }

    private void updateCharacterPosition() {
        characterPositionX = Math.max(-500, Math.min(characterPositionX, 500));
        ivCharacter.setTranslationX(characterPositionX);
    }
    private void removeBottomRow() {
        if (llGameContainer.getChildCount() > 0) {
            llGameContainer.removeViewAt(llGameContainer.getChildCount() - 1);
        }
    }
    private void addRandomRowAtTop() {
        if (new Random().nextBoolean()) {
            addGrassRowWithTrees();
        } else {
            addConcreteRowAtTop();
        }
    }
    private void addGrassRowWithTrees() {
        RelativeLayout grassWithTreesRow = new RelativeLayout(this);
        grassWithTreesRow.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) dpToPx(ROW_HEIGHT_DP)));

        grassWithTreesRow.setBackgroundResource(R.drawable.grass);

        llGameContainer.addView(grassWithTreesRow, 0);

        Random random = new Random();
        for (int i = 0; i < MAX_TREES_PER_ROW; i++) {
            if (random.nextBoolean()) {
                ImageView tree = new ImageView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (int) dpToPx(TREE_SPACING_DP),
                        RelativeLayout.LayoutParams.MATCH_PARENT);

                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin = i * TREE_SPACING_DP;

                tree.setLayoutParams(params);
                tree.setImageResource(R.drawable.tree);
                grassWithTreesRow.addView(tree);
                treeImageViews.add(tree);
            }
        }
    }


    private void addConcreteRowAtTop() {
        RelativeLayout concreteRow = new RelativeLayout(this);
        concreteRow.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) dpToPx(ROW_HEIGHT_DP)));

        ImageView concreteImageView = new ImageView(this);
        concreteImageView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        concreteImageView.setImageResource(R.drawable.concrete);
        concreteImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        concreteRow.addView(concreteImageView);

        // Add the car to the concrete row
        addCarToConcreteRow(concreteRow);

        // Add the concrete row to llGameContainer
        llGameContainer.addView(concreteRow, 0);
    }

    private void addCarToConcreteRow(RelativeLayout concreteRow) {
        int carWidth = 300; // Set the desired width
        int carHeight = 300; // Set the desired height

        int yOffset = 0;
        Car car = new Car(this, getRandomSpeed(), carWidth, carHeight, new Handler());

        // Set the initial position and direction of the car
        if (new Random().nextBoolean()) {
            // Move from left to right
            car.setInitialPosition(0, yOffset);
            car.setMoveDirection(Car.MOVE_RIGHT);
        } else {
            // Move from right to left
            car.setInitialPosition(llGameContainer.getWidth() - carWidth, yOffset);
            car.setMoveDirection(Car.MOVE_LEFT);
        }

        concreteRow.addView(car);
        carList.add(car);
    }

    private int getRandomSpeed() {
        // Set the range for random speeds (adjust as needed)
        int minSpeed = 5;
        int maxSpeed = 40;

        // Generate a random speed within the specified range
        return new Random().nextInt((maxSpeed - minSpeed) + 1) + minSpeed;
    }



    private float dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return dp * density;
    }
}