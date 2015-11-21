package com.transitiasi.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.transitiasi.R;
import com.transitiasi.model.DirectionResponse;
import com.transitiasi.retrofit.DirectionServiceApi;
import com.transitiasi.retrofit.TransitIasiClientApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TransitIasiMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Bind(R.id.toolbar)
    Toolbar toolbar;


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

        DirectionServiceApi.defaultService().getRoutes("Copou","Podu Ros","AIzaSyD1kGlEz8q64IFtf1oqBEBlHA_WiuZYjI8").enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Response<DirectionResponse> response, Retrofit retrofit) {
                int d = 0;
                d++;
            }

            @Override
            public void onFailure(Throwable t) {
                int d = 0;
                d++;
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(47.155649, 27.590058);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Iasi"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, (int) (mMap.getMaxZoomLevel() * 0.72)));
    }

}
