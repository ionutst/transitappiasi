package com.transitiasi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {
    private Step[] steps;

    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }
}
