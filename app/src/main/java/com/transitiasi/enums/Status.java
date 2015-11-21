package com.transitiasi.enums;

import com.transitiasi.R;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public enum Status {
    GREEN(R.color.green), RED(R.color.red), ORANGE(R.color.orange);
    public final int color;

    Status(int color) {
        this.color = color;
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
