package com.example.doandreward.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.example.doandreward.R;

public class Metrics {

    private final DisplayMetrics displayMetrics;
    private final int height;
    private final int width;
    private final Drawable deleteIcon;
    private final Drawable completeIcon;
    private final Drawable buyIcon;
    private final int deleteColor;
    private final int completeColor;
    private final int buyColor;

    public Metrics(Context context) {
        this.displayMetrics = context.getResources().getDisplayMetrics();
        height = (int)(displayMetrics.heightPixels / displayMetrics.density);
        width = (int)(displayMetrics.widthPixels / displayMetrics.density);
        deleteIcon = context.getResources().getDrawable(R.drawable.baseline_delete_24, null);
        completeIcon = context.getResources().getDrawable(R.drawable.baseline_check_circle_24, null);
        buyIcon =context.getResources().getDrawable(R.drawable.baseline_take_prize_24, null);
        deleteColor = context.getResources().getColor(android.R.color.holo_red_light, null);
        completeColor = context.getResources().getColor(android.R.color.holo_green_light, null);
        buyColor = context.getResources().getColor(android.R.color.holo_purple, null);
    }

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Drawable getDeleteIcon() {
        return deleteIcon;
    }

    public Drawable getCompleteIcon() {
        return completeIcon;
    }

    public Drawable getBuyIcon() { return buyIcon; }

    public int getDeleteColor() {
        return deleteColor;
    }

    public int getCompleteColor() {
        return completeColor;
    }

    public int getBuyColor() { return buyColor; }
}
