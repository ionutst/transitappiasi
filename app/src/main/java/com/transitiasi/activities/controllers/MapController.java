package com.transitiasi.activities.controllers;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.transitiasi.enums.Status;
import com.transitiasi.model.ShareInfo;

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

    public MapController(final Activity activity, GoogleMap map) {
        this.map = map;

        this.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(MapController.this.activity, marker.getSnippet(), Toast.LENGTH_LONG).show();
                Snackbar.make(activity.findViewById(android.R.id.content), marker.getTitle()+" "+marker.getSnippet(), Snackbar.LENGTH_LONG).show();
                return true;
            }
        });
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

    public void removePolyline() {
        if (polyline != null)
            polyline.remove();
    }

    public void addPolyline(PolylineOptions polylineOptions) {
        polyline = map.addPolyline(polylineOptions);
    }

    public void removeBusMarkers() {
        for (Marker marker : busMarkers) {
            marker.remove();
        }
    }

    public void addBusMarker(Resources res, ShareInfo shareInfo, LatLng latLng, Bitmap busMarker) {

        final String statusLabel = res.getString(Status.fromString(shareInfo.getStatus()).label);

        final Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(busMarker))
                .anchor(0.5f, 1)
                .title(shareInfo.getType() + shareInfo.getLabel())
                .snippet(statusLabel));

        busMarkers.add(marker);
    }
}
