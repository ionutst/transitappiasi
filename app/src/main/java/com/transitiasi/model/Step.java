package com.transitiasi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {
    private Polyline polyline;

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }
}
