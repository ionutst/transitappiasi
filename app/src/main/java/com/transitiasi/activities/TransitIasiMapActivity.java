package com.transitiasi.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.transitiasi.R;
import com.transitiasi.model.DirectionResponse;
import com.transitiasi.model.ShareInfo;
import com.transitiasi.realtime.RealTimeScheduler;
import com.transitiasi.retrofit.DirectionServiceApi;
import com.transitiasi.util.PolylineUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.schedulers.Schedulers;

public class TransitIasiMapActivity extends BaseActivity implements OnMapReadyCallback,RealTimeScheduler.OnRealtimeListener {
    private static final LatLng IASI = new LatLng(47.155649, 27.590058);
    private GoogleMap map;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.txt_origin)
    AutoCompleteTextView txtOrigin;

    @Bind(R.id.txt_destination)
    AutoCompleteTextView txtDestination;

    private Dialog progressDialog;

    private Polyline polyline;
    private List<Marker> markers = new ArrayList<>(4);

    private String[] stations;
    private Map<String, String> stationsCoordinates;

    //click listeners
    @OnClick(R.id.imgb_go)
    void onGoClicked() {
        final String start = txtOrigin.getText().toString();
        final String end = txtDestination.getText().toString();
        if (start.trim().isEmpty()) {
            Toast.makeText(this, R.string.error_from_mandatory, Toast.LENGTH_SHORT).show();
            return;
        }
        if (end.trim().isEmpty()) {
            Toast.makeText(this, R.string.error_destination_mandatory, Toast.LENGTH_LONG).show();
            return;
        }
        searchForRoute(start, end);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        ButterKnife.bind(this);

        View v = LayoutInflater.from(this).inflate(R.layout.share_toolbar, null);
        toolbar.addView(v);
        setSupportActionBar(toolbar);

        ImageView ic_share = (ImageView) v.findViewById(R.id.ic_share);
        ic_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransitIasiMapActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //PolylineUtils.buildJsonForServer();

        stations = getResources().getStringArray(R.array.stations);
        stationsCoordinates = new HashMap<>(stations.length);
        String[] stationsCoordinatesAsString = getResources().getStringArray(R.array.stations);
        for (int i = 0; i < stationsCoordinatesAsString.length; i++) {
            stationsCoordinates.put(stations[i], stationsCoordinatesAsString[i]);
        }
        init();
        RealTimeScheduler.INSTANCE.setOnRealtimeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RealTimeScheduler.INSTANCE.stop();
    }

    private void searchForRoute(String start, String destination) {
//        DirectionServiceApi.defaultService()
//                .getRoutesUsingCall(DirectionServiceApi.MODE, start, destination, DirectionServiceApi.MAP_API_KEY, DirectionServiceApi.TRANSIT_MODE)
//                .enqueue(new Callback<DirectionResponse>() {
//                    @Override
//                    public void onResponse(Response<DirectionResponse> response, Retrofit retrofit) {
//                        int d = 0;
//                        d++;
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        int d = 0;
//                        d++;
//                    }
//                });
        progressDialog = ProgressDialog.show(this, null, getString(R.string.searching));

//        start = "47.175776,27.571667";
//        destination = "47.156013,27.603916";
        if (stationsCoordinates.containsKey(start)) {
            start = stationsCoordinates.get(start);
        }
        if (stationsCoordinates.containsKey(destination)) {
            destination = stationsCoordinates.get(destination);
        }
        Log.d("coordinates", "start: " + start + " destination: " + destination);

        DirectionServiceApi.defaultService()
                .getRoutes(DirectionServiceApi.MODE.toLowerCase(), start, destination, DirectionServiceApi.MAP_API_KEY, DirectionServiceApi.TRANSIT_MODE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<DirectionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(DirectionResponse directionResponse) {
                        final List<LatLng> pathCoordinates = PolylineUtils.decodePath(directionResponse);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                displayPolyline(pathCoordinates);
                            }
                        });
                    }
                });
    }

    private void init() {
        addAutocompleteOptions(txtDestination);
        addAutocompleteOptions(txtOrigin);
    }

    private void addAutocompleteOptions(AutoCompleteTextView autoCompleteTextView) {
        String[] items = getResources().getStringArray(R.array.stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, items);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(47.155649, 27.590058);
        //map.addMarker(new MarkerOptions().position(sydney).title("Iasi"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(IASI, (int) (map.getMaxZoomLevel() * 0.72)));
    }

    private void removeMarkers() {
        Iterator<Marker> markerIterator = markers.iterator();
        while (markerIterator.hasNext()) {
            final Marker marker = markerIterator.next();
            marker.remove();

            markerIterator.remove();
        }
    }

    private void addMarker(LatLng latLng) {
        final Marker marker = map.addMarker(new MarkerOptions().position(latLng));
        markers.add(marker);

    }

    private void displayPolyline(List<LatLng> coordinates) {
        if (polyline != null)
            polyline.remove();

        if (coordinates == null || coordinates.size() < 2) {
            Toast.makeText(this, R.string.no_route_found, Toast.LENGTH_SHORT).show();
            return;
        }

        removeMarkers();

        addMarker(coordinates.get(0));
        addMarker(coordinates.get(coordinates.size() - 1));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coordinates);
        polylineOptions.color(Color.BLUE);

        polyline = map.addPolyline(polylineOptions);

        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        for (LatLng coordinate : coordinates) {
            latLngBounds.include(coordinate);
        }

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 10));

        RealTimeScheduler.INSTANCE.start();
    }


    @Override
    public void onRealTime(List<ShareInfo> response) {
        if(response == null){
            return;
        }
        for(ShareInfo shareInfo:response){
            //call anca method
        }
        Log.d("realtime","onRealtime");
    }
}
