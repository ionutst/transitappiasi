package com.transitiasi.model;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public class ShareInfo {
    public static final String TRAM = "T";

    private String label;
    private String type;
    private double lat;
    private double lng;
    private String status;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status == null){
            throw new IllegalArgumentException("status is null");
        }

        this.status = status.toLowerCase();
    }

    @Override
    public String toString() {
        return label+type+status;
    }
}
