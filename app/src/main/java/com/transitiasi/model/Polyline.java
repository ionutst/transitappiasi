package com.transitiasi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Polyline {
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
