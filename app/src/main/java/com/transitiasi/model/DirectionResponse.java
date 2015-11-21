package com.transitiasi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectionResponse {
    private String status;

    private String error_message;

    private Route[] routes;

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
