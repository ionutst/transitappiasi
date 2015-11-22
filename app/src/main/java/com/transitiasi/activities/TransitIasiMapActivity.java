package com.transitiasi.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.transitiasi.R;
import com.transitiasi.model.DirectionResponse;
import com.transitiasi.model.ShareInfo;
import com.transitiasi.model.ShareInfoResponse;
import com.transitiasi.realtime.RealTimeScheduler;
import com.transitiasi.retrofit.DirectionServiceApi;
import com.transitiasi.retrofit.TransitIasiClientApi;
import com.transitiasi.util.CustomMarkerBuilder;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransitIasiMapActivity extends BaseActivity implements OnMapReadyCallback, RealTimeScheduler.OnRealtimeListener {
    private static final int CODE_SHARE = 202;
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
    private List<Marker> busMarkers = new ArrayList<>(4);

    private String[] stations;
    private Map<String, String> stationsCoordinates;
    private ImageView ic_share;

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

        ic_share = (ImageView) v.findViewById(R.id.ic_share);
        setupShare();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
    protected void onDestroy() {
        super.onDestroy();
        RealTimeScheduler.INSTANCE.stop();
	}
    private void setupShare() {
        ic_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransitIasiMapActivity.this, ShareActivity.class);
                startActivityForResult(intent, CODE_SHARE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RealTimeScheduler.INSTANCE.stop();

    }

    private void searchForRoute(String start, String destination) {

        progressDialog = ProgressDialog.show(this, null, getString(R.string.searching));

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, stations);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

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

    private void removeBusMarkers() {
        for (Marker marker : busMarkers) {
            marker.remove();
        }
    }

    private void addBusMarker(LatLng latLng, Bitmap busMarker) {
        final Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(busMarker))
                .anchor(0.5f, 1));

        busMarkers.add(marker);
    }

    @Override
    public void onRealTime(List<ShareInfo> response) {
        if (response == null) {
            return;
        }
        removeBusMarkers();
        for (ShareInfo shareInfo : response) {
            addTransportMarker(shareInfo);
        }
        Log.d("realtime", "onRealtime");
    }

    private void addTransportMarker(ShareInfo shareInfo) {
//        shareInfo.setLat(47.151135);
//        shareInfo.setLng(27.587258);
//        shareInfo.setLabel("41");
//        shareInfo.setStatus("RED");
        LatLng latLng = new LatLng(shareInfo.getLat(), shareInfo.getLng());

        final Bitmap bmp = CustomMarkerBuilder.buildMarkerForBus(shareInfo, getResources());

        addBusMarker(latLng, bmp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_SHARE) {
            ic_share.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_share));
            ic_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSharing();
                    ic_share.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
                    setupShare();
                }
            });
        }
    }

    private void stopSharing(){
        TransitIasiClientApi.defaultService()
                .stopShare(new ShareInfo())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShareInfoResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("INFO", e.toString());
                    }

                    @Override
                    public void onNext(final ShareInfoResponse response) {
                        Log.d("INFO", response.getResponse());

                        //progress.setVisibility(View.GONE);
                        Toast.makeText(TransitIasiMapActivity.this, response.getResponse(), Toast.LENGTH_SHORT).show();
                        //setResult(RESULT_OK);
                        //finish();

                    }
                });
    }
}
