package com.transitiasi.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.transitiasi.R;
import com.transitiasi.adapters.TransitIasiLinearLayoutManager;
import com.transitiasi.adapters.TransportationAdapter;
import com.transitiasi.utils.TransportItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShareActivity extends AppCompatActivity {
    private static final String SELECTED = "selected";
    private static final String UNSELECTED = "unselected";
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.card_tram)
    CardView card_tram;

    @Bind(R.id.card_bus)
    CardView card_bus;

    @Bind(R.id.card_minibus)
    CardView card_minibus;

    @Bind(R.id.iv_empty_transportation)
    ImageView iv_empty_transportation;

    @Bind(R.id.iv_full_transportation)
    ImageView iv_full_transportation;

    @Bind(R.id.iv_comfy_transportation)
    ImageView iv_comfy_transportation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupTramList();
        setupBusList();
        setupMiniBusList();
        setupLevelOfOcupation();
        setupToolBar();
    }

    private void setupToolBar() {
        View v = LayoutInflater.from(this).inflate(R.layout.share_activity_toolbar_layout, null);
        toolbar.addView(v);
        ImageView iv = (ImageView) v.findViewById(R.id.ic_validate);
    }


    private void setupTramList() {
        RecyclerView rv_trams = (RecyclerView) card_tram.findViewById(R.id.rv_transportation);
        TextView tv_transportation = (TextView) card_tram.findViewById(R.id.tv_transportation);
        tv_transportation.setText(getString(R.string.tram));
        String[] tramNumbers = getResources().getStringArray(R.array.tram_numbers);
        TransportationAdapter tramAdapter = new TransportationAdapter(ShareActivity.this, createItems(tramNumbers));
        TransitIasiLinearLayoutManager lManager = new TransitIasiLinearLayoutManager(ShareActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_trams.setHasFixedSize(true);
        rv_trams.setLayoutManager(lManager);
        rv_trams.setAdapter(tramAdapter);
    }

    private void setupBusList() {
        RecyclerView rv_trams = (RecyclerView) card_bus.findViewById(R.id.rv_transportation);
        TextView tv_transportation = (TextView) card_bus.findViewById(R.id.tv_transportation);
        tv_transportation.setText(getString(R.string.bus));
        String[] busNumbers = getResources().getStringArray(R.array.bus_numbers);
        TransportationAdapter tramAdapter = new TransportationAdapter(ShareActivity.this, createItems(busNumbers));
        TransitIasiLinearLayoutManager lManager = new TransitIasiLinearLayoutManager(ShareActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_trams.setHasFixedSize(true);
        rv_trams.setLayoutManager(lManager);
        rv_trams.setAdapter(tramAdapter);

    }

    private void setupMiniBusList() {
        final RecyclerView rv_trams = (RecyclerView) card_minibus.findViewById(R.id.rv_transportation);
        TextView tv_transportation = (TextView) card_minibus.findViewById(R.id.tv_transportation);
        tv_transportation.setText(getString(R.string.minibus));
        String[] miniBusNumbers = getResources().getStringArray(R.array.minibus_numbers);
        TransportationAdapter tramAdapter = new TransportationAdapter(ShareActivity.this, createItems(miniBusNumbers));
        TransitIasiLinearLayoutManager lManager = new TransitIasiLinearLayoutManager(ShareActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_trams.setHasFixedSize(true);
        rv_trams.setLayoutManager(lManager);
        rv_trams.setAdapter(tramAdapter);
    }

    private void setupLevelOfOcupation() {
        iv_empty_transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_empty_transportation.getTag() == null || iv_empty_transportation.getTag().equals(UNSELECTED)) {
                    iv_empty_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_selected));
                    iv_empty_transportation.setTag(SELECTED);
                    iv_full_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_unselected));
                    iv_full_transportation.setTag(UNSELECTED);
                    iv_comfy_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_comfy_unselected));
                    iv_comfy_transportation.setTag(UNSELECTED);
                } else {
                    iv_empty_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_unselected));
                    iv_empty_transportation.setTag(UNSELECTED);
                }
            }
        });

        iv_comfy_transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_comfy_transportation.getTag() == null || iv_comfy_transportation.getTag().equals(UNSELECTED)) {
                    iv_comfy_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_comfy_selected));
                    iv_comfy_transportation.setTag(SELECTED);
                    iv_full_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_unselected));
                    iv_full_transportation.setTag(UNSELECTED);
                    iv_empty_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_unselected));
                    iv_empty_transportation.setTag(UNSELECTED);
                } else {
                    iv_comfy_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_comfy_unselected));
                    iv_comfy_transportation.setTag(UNSELECTED);
                }
            }
        });

        iv_full_transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_full_transportation.getTag() == null || iv_full_transportation.getTag().equals(UNSELECTED)) {
                    iv_full_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_selected));
                    iv_full_transportation.setTag(SELECTED);
                    iv_empty_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_unselected));
                    iv_empty_transportation.setTag(UNSELECTED);
                    iv_comfy_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_comfy_unselected));
                    iv_comfy_transportation.setTag(UNSELECTED);
                } else {
                    iv_full_transportation.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_unselected));
                    iv_full_transportation.setTag(UNSELECTED);
                }
            }
        });
    }

    private List<TransportItem> createItems(String[] strings) {
        List<TransportItem> items = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            TransportItem item = new TransportItem();
            item.setNumber(strings[i]);
            item.setSelection(false);
            items.add(item);
        }
        return items;
    }


}
