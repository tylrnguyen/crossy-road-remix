package com.example.crossyroadsgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class GameplayActivity extends AppCompatActivity {

    private int NUM_INITIAL_GRASS_ROWS = 10;
    private static final int ROW_HEIGHT_DP = 85;
    private boolean userClickedUpArrow = false;
    private LinearLayout llGameContainer;
    private static final int CHARACTER_MOVE_DISTANCE = 85;
    private int characterPositionX = 0;
    private ImageView ivCharacter;
    private int score = 0;
    private TextView scoreTextView;
    private ArrayList<Car> carList = new ArrayList<>();
    private ArrayList<Tree> treeList = new ArrayList<>();
    private static final String SCORE_KEY = "SCORE";
    private static final int MAX_TREES_PER_ROW = 2; // Adjust based on screen size
    private static final int TREE_SPACING_DP = 175; // Adjust for spacing between trees
    private ArrayList<ImageView> treeImageViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        llGameContainer = findViewById(R.id.llGameContainer);
        scoreTextView = findViewById(R.id.scoreTextView);
        ivCharacter = findViewById(R.id.ivCharacter);

        score = getIntent().getIntExtra(SCORE_KEY, 0);
        updateScore(score);

        // Add initial grass rows
        for (int i = 0; i < NUM_INITIAL_GRASS_ROWS; i++) {
            addGrassRow();
        }

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

        // Start the game loop
        startGameLoop();
    }

    private void updateScore(int newScore) {
        score = newScore;
        scoreTextView.setText("Score: " + score);
    }

    public static Intent newIntent(Context context, int score) {
        Intent intent = new Intent(context, GameplayActivity.class);
        intent.putExtra(SCORE_KEY, score);
        return intent;
    }

    private void addGrassRow() {
        ImageView grassRow = new ImageView(this);
        grassRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) dpToPx(ROW_HEIGHT_DP)));
        grassRow.setImageResource(R.drawable.grass);
        grassRow.setScaleType(ImageView.ScaleType.FIT_XY);

        // Add the grass row to llGameContainer
        llGameContainer.addView(grassRow);
    }


    private void startGameLoop() {
        final Handler handler = new Handler();

        // Start the game loop immediately
        handler.post(new Runnable() {
            @Override
            public void run() {

                // Check for collisions on the main thread
                checkCollisions();

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
                Tree tree = new Tree(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        Tree.getTreeWidth(),
                        Tree.getTreeHeight());

                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                // Adjust the range of spacing between 200 and 400
                params.leftMargin = i * (200 + random.nextInt(450));

                tree.setLayoutParams(params);
                grassWithTreesRow.addView(tree);
                treeList.add(tree);
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

    private boolean hasCollided = false;

    private void checkCollisions() {
        if (!hasCollided) {
            for (Car car : carList) {
                if (isCollisionDetected(ivCharacter, car)) {
                    // Collision with a car, end the game
                    hasCollided = true; // Set the flag to true to avoid repeated calls
                    endGame();
                    return;
                }
            }
        }
    }


    private boolean isCollisionDetected(View view1, View view2) {
        int[] location1 = new int[2];
        view1.getLocationOnScreen(location1);
        RectF rect1 = new RectF(
                location1[0] + view1.getPaddingStart(),
                location1[1] + view1.getPaddingTop(),
                location1[0] + view1.getWidth() - view1.getPaddingEnd(),
                location1[1] + view1.getHeight() - view1.getPaddingBottom());
        int[] location2 = new int[2];
        view2.getLocationOnScreen(location2);
        RectF rect2 = new RectF(
                location2[0] + view2.getPaddingStart(),
                location2[1] + view2.getPaddingTop(),
                location2[0] + view2.getWidth() - view2.getPaddingEnd(),
                location2[1] + view2.getHeight() - view2.getPaddingBottom());
        // Check if the rectangles overlap
        boolean collisionDetected = rect1.intersect(rect2);
        // Reset the collision flag only if the views are not colliding anymore
        if (!collisionDetected) {
            hasCollided = false;
        }
        return collisionDetected;
    }




    private void endGame() {
        startActivity(EndGameActivity.newIntent(this, score));
    }

    private float dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return dp * density;
    }
}