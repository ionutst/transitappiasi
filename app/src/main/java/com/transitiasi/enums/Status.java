package com.transitiasi.enums;

import com.transitiasi.R;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public enum Status {
    GREEN, RED, ORANGE;

    public static int getColor(String color) {
        switch (color) {
            case "GREEN":
                return R.color.green;

            case "RED":
                return R.color.red;

            case "ORANGE":
                return R.color.orange;


        }
        return R.color.red;
    }
}
