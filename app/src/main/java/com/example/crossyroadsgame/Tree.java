package com.example.crossyroadsgame;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;

public class Tree extends AppCompatImageView {

    private static final int TREE_WIDTH = 500; // Set the desired width
    private static final int TREE_HEIGHT = 500; // Set the desired height

    public Tree(Context context) {
        super(context);
        initializeTree();
    }
    private void initializeTree() {
        // Set initial position and size for the tree
        setLayoutParams(new RelativeLayout.LayoutParams(
                getTreeWidth(),
                getTreeHeight()));
        setImageResource(R.drawable.tree); // Set the drawable for the tree (customize as needed)
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // You may set other properties such as initial position, etc.
    }
    public static int getTreeWidth() {
        return TREE_WIDTH;
    }
    public static int getTreeHeight() {
        return TREE_HEIGHT;
    }
}

