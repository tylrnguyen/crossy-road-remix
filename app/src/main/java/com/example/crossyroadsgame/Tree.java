package com.example.crossyroadsgame;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Tree extends ImageView {

    private static final int TREE_WIDTH = 50; // Set the desired width
    private static final int TREE_HEIGHT = 50; // Set the desired height

    public Tree(Context context) {
        super(context);
        initializeTree();
    }

    private void initializeTree() {
        // Set initial position and size for the tree
        setLayoutParams(new RelativeLayout.LayoutParams(
                TREE_WIDTH,
                TREE_HEIGHT));
        setImageResource(R.drawable.tree); // Set the drawable for the tree (customize as needed)
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // You may set other properties such as initial position, etc.
    }

    // Add any other methods or properties specific to the tree class here
}

