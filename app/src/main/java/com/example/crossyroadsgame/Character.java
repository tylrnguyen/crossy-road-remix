/*
package com.example.crossyroadsgame;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;

public class Character extends AppCompatImageView {

    private int characterPositionX;
    private int characterPositionY;

    private LinearLayout llGameContainer;

    private static final int CHARACTER_WIDTH = 250; // Set the desired width
    private static final int CHARACTER_HEIGHT = 250; // Set the desired height


    public Character(Context context, LinearLayout gameContainer) {
        super(context);
        llGameContainer = gameContainer; // Set the reference to the game container
        initializeCharacter();
    }

    private void initializeCharacter() {
        // Set the image resource or background for your character
        // For example:
        setImageResource(R.drawable.person);

        // Adjust the scale type to fit the entire image within the specified dimensions
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setElevation(Float.MAX_VALUE);

        // Set the layout parameters
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                CHARACTER_WIDTH, CHARACTER_HEIGHT);

        // Set the character's position to be in the bottom row and centered horizontally
        params.topMargin = llGameContainer.getHeight() - CHARACTER_HEIGHT;
        params.leftMargin = (llGameContainer.getWidth() - CHARACTER_WIDTH) / 2;

        // Apply the layout parameters
        setLayoutParams(params);
    }


    public int getCharacterPositionX() {
        return characterPositionX;
    }

    public int getCharacterPositionY() {
        return characterPositionY;
    }

    // You can add more methods to handle character movements, animations, etc.
}

 */