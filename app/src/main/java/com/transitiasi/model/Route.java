package com.transitiasi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private Leg[] legs;

    public Leg[] getLegs() {
        return legs;
    }

    public void setLegs(Leg[] legs) {
        this.legs = legs;
    }
}
