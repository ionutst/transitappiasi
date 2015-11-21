package com.transitiasi.enums;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public enum TransportType {
    T, B, b;

    public static TransportType fromString(String type){
        switch (type){
            case "T":
                return T;
            case "B":
                return B;
            case "b":
                return b;
        }
        return null;
    }
}
