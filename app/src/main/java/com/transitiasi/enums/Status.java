package com.transitiasi.enums;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.transitiasi.R;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public enum Status {
    GREEN(R.color.green, R.string.empty_transportation), RED(R.color.red, R.string.full_transportation), ORANGE(R.color.orange, R.string.comfy_transportation);
    public final int color;
    public final int label;

    Status(@ColorRes int color,@StringRes int label) {
        this.color = color;
        this.label = label;
    }

    public static Status fromString(String status) {
        switch (status) {
            case "green":
                return GREEN;
            case "orange":
                return ORANGE;
            case "red":
                return RED;
        }

        throw new IllegalArgumentException("Wrong input type, expected green, orange or red");
    }
}
