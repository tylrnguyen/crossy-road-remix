package com.example.crossyroadsgame;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;

public class Character extends AppCompatImageView {

    private int positionX;


    public Character(Context context, int initialPositionX, int bottomRowY) {
        super(context);
        this.positionX = initialPositionX;

        // Set layout parameters, scale type, and other attributes as needed
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.leftMargin = initialPositionX;
        setLayoutParams(layoutParams);

        // Set the character image resource
        setImageResource(R.drawable.person);

        // Additional setup for the character, if necessary
        // ...
    }
    public int getPositionX() {
        return positionX;
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    public void moveLeft(int moveDistance) {
        // Implement the logic to move the character to the left within the bottom row
        positionX = Math.max(0, positionX - moveDistance);
    }

    public void moveRight(int moveDistance, int screenWidth) {
        // Implement the logic to move the character to the right within the bottom row
        positionX = Math.min(screenWidth - getWidth(), positionX + moveDistance);
    }

    // Additional methods for collision detection or other character-related functionalities
    // ...
}

