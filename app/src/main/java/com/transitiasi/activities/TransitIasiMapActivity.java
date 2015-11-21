package com.transitiasi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.transitiasi.R;
import com.transitiasi.model.DirectionResponse;
import com.transitiasi.retrofit.DirectionServiceApi;
import com.transitiasi.util.PolylineUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.schedulers.Schedulers;

public class TransitIasiMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.txt_destination)
    AutoCompleteTextView txtDestination;

    @Bind(R.id.txt_origin)
    AutoCompleteTextView txtOrigin;

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


        DirectionServiceApi.defaultService()
                .getRoutes("Copou", "Podu Ros", "AIzaSyD1kGlEz8q64IFtf1oqBEBlHA_WiuZYjI8")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<DirectionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        int d = 0;
                        d++;
                    }

                    @Override
                    public void onNext(DirectionResponse directionResponse) {
                        final List<LatLng> pathCoordinates = PolylineUtils.decodePath(directionResponse);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayPolyline(pathCoordinates);
                            }
                        });
                    }
                });

        init();
    }

    private void init() {
        addAutocompleteOptions(txtDestination);
        addAutocompleteOptions(txtOrigin);
    }
    private void addAutocompleteOptions(AutoCompleteTextView autoCompleteTextView){
        String[] items = getResources().getStringArray(R.array.stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, items);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(47.155649, 27.590058);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Iasi"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, (int) (mMap.getMaxZoomLevel() * 0.72)));
    }

    private void displayPolyline(List<LatLng> coordinates) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coordinates);
        mMap.addPolyline(polylineOptions);

        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        for (LatLng coordinate : coordinates) {
            latLngBounds.include(coordinate);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 10));
    }

}
