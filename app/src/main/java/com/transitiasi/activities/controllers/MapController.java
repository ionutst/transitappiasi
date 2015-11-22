package com.transitiasi.activities.controllers;

import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * Created by Ionut Stirban on 22/11/15.
 */
public class MapController {
    private GoogleMap map;
    private Polyline polyline;
    private List<Marker> markers = new ArrayList<>(4);
    private List<Marker> busMarkers = new ArrayList<>(4);

    public MapController(GoogleMap map) {
        this.map = map;
    }

    public void removeMarkers() {
        Iterator<Marker> markerIterator = markers.iterator();
        while (markerIterator.hasNext()) {
            final Marker marker = markerIterator.next();
            marker.remove();

            markerIterator.remove();
        }
    }

    public void addMarker(LatLng latLng) {
        final Marker marker = map.addMarker(new MarkerOptions().position(latLng));
        markers.add(marker);
    }
    public  void removePolyline(){
        if (polyline != null)
            polyline.remove();
    }

    public void removeBusMarkers() {
        for (Marker marker : busMarkers) {
            marker.remove();
        }
    }

    public void addBusMarker(LatLng latLng, Bitmap busMarker) {
        final Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(busMarker))
                .anchor(0.5f, 1));

        busMarkers.add(marker);
    }
}
