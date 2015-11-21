package com.transitiasi.enums;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public enum Status {
    GREEN, RED, ORANGE;

    public static Status fromString(String status){
        switch (status){
            case "green":
                return GREEN;
            case "orange":
                return ORANGE;
            case "red":
                return RED;
        }

        return null;
    }

}
