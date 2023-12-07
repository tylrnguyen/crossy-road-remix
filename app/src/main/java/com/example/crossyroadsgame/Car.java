package com.example.crossyroadsgame;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import android.os.Handler;
import android.os.Looper;

public class Car extends AppCompatImageView {
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT = -1;
    private Handler uiHandler;
    private int speed;

    private int moveDirection = MOVE_RIGHT; // Default direction is right

    public Car(Context context, int speed, int width, int height, Handler uiHandler) {
        super(context);
        this.speed = speed;
        this.uiHandler = uiHandler;
        setImageResource(R.drawable.car);
        setRotation(90);
        // Set the size of the car image
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        setLayoutParams(params);
    }

    public void setInitialPosition(float x, float y) {
        setTranslationX(x);
        setTranslationY(y);
    }

    public void move(final int screenWidth) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                setTranslationX(getTranslationX() + (speed * moveDirection));

                // Check if the car is off the screen based on the current direction
                if (moveDirection == MOVE_RIGHT && getTranslationX() > screenWidth) {
                    // Reset the car's position to the left edge to continue moving
                    setTranslationX(-getWidth());
                } else if (moveDirection == MOVE_LEFT && getTranslationX() + getWidth() < 0) {
                    // Reset the car's position to the right edge to continue moving
                    setTranslationX(screenWidth);
                }

                // Adjust rotation based on movement direction
                setRotation(moveDirection == MOVE_RIGHT ? 90 : 270);
            }
        });
    }

    public void setMoveDirection(int direction) {
        // Set the direction of the car's movement
        moveDirection = direction;
    }
}