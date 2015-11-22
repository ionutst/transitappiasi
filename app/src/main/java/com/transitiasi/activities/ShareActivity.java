package com.transitiasi.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.transitiasi.R;
import com.transitiasi.enums.Status;
import com.transitiasi.enums.TransportType;
import com.transitiasi.adapters.TransitIasiLinearLayoutManager;
import com.transitiasi.adapters.TransportationAdapter;
import com.transitiasi.model.ShareInfo;
import com.transitiasi.model.ShareInfoResponse;
import com.transitiasi.retrofit.TransitIasiClientApi;
import com.transitiasi.util.PlatformUtils;
import com.transitiasi.utils.TransportItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShareActivity extends BaseActivity {
    private static final String SELECTED = "selected";
    private static final String UNSELECTED = "unselected";
    private IItemSelected itemSelected;
    private List<TransportItem> trams;
    private List<TransportItem> buses;
    private List<TransportItem> minibuses;
    private TransportationAdapter tramAdapter;
    private TransportationAdapter busAdapter;
    private TransportationAdapter minibusAdapter;
    private ShareInfo shareInfo;
    private final ImageView[] statusViews = new ImageView[3];
    private final int[] selectedIcons = {R.drawable.ic_empty_selected, R.drawable.ic_comfy_selected, R.drawable.ic_full_selected};
    private final int[] unselectedIcons = {R.drawable.ic_empty_unselected, R.drawable.ic_comfy_unselected, R.drawable.ic_full_unselected};
    private final Status[] statuses = {Status.GREEN, Status.ORANGE, Status.RED};

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

    @Bind(R.id.progress)
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        ButterKnife.bind(this);
        createViewsList();
        shareInfo = new ShareInfo();
        setSupportActionBar(toolbar);
        setupTramList();
        setupBusList();
        setupMiniBusList();
        setupToolBar();
        setupChangesInStatus();
    }

    private void createViewsList() {
        statusViews[0] = iv_empty_transportation;
        statusViews[1] = iv_comfy_transportation;
        statusViews[2] = iv_full_transportation;
    }

    private void setupChangesInStatus() {
        for (int i = 0; i < statusViews.length; i++) {
            final int position = i;
            statusViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelection(position);
                    shareInfo.setStatus(statuses[position].toString());
                }
            });
        }
    }


    private void setSelection(int index) {
        for (int i = 0; i < statusViews.length; i++) {
            if (i == index) {
                statusViews[i].setImageResource(selectedIcons[i]);
                shareInfo.setStatus(Status.ORANGE.toString());
            } else {
                statusViews[i].setImageResource(unselectedIcons[i]);
            }
        }
    }

    private void setupToolBar() {
        View v = LayoutInflater.from(this).inflate(R.layout.share_activity_toolbar_layout, null);
        toolbar.addView(v);
        ImageView iv = (ImageView) v.findViewById(R.id.ic_validate);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PlatformUtils.isLocationEnabled(ShareActivity.this)) {
                    updateCurrentLocation();
                } else {
                    Toast.makeText(ShareActivity.this, R.string.please_turn_on_location, Toast.LENGTH_SHORT).show();
                }
                Log.d("INFO", shareInfo.getLat() + " " + shareInfo.getLng());
            }
        });
    }


    private void setupTramList() {
        RecyclerView rv_trams = (RecyclerView) card_tram.findViewById(R.id.rv_transportation);
        TextView tv_transportation = (TextView) card_tram.findViewById(R.id.tv_transportation);
        tv_transportation.setText(getString(R.string.tram));
        String[] tramNumbers = getResources().getStringArray(R.array.tram_numbers);
        trams = createItems(tramNumbers);
        tramAdapter = new TransportationAdapter(ShareActivity.this, trams, new IItemSelected() {
            @Override
            public void onClick(String transportationLabel) {
                shareInfo.setLabel(transportationLabel);
                shareInfo.setType(TransportType.T.toString());
                emptyOtherList(buses, busAdapter);
                emptyOtherList(minibuses, minibusAdapter);
            }
        });
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
        buses = createItems(busNumbers);
        busAdapter = new TransportationAdapter(ShareActivity.this, buses, new IItemSelected() {
            @Override
            public void onClick(String transportationLabel) {
                shareInfo.setLabel(transportationLabel);
                shareInfo.setType(TransportType.B.toString());
                emptyOtherList(trams, tramAdapter);
                emptyOtherList(minibuses, minibusAdapter);
            }
        });
        TransitIasiLinearLayoutManager lManager = new TransitIasiLinearLayoutManager(ShareActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_trams.setHasFixedSize(true);
        rv_trams.setLayoutManager(lManager);
        rv_trams.setAdapter(busAdapter);

    }

    private void setupMiniBusList() {
        final RecyclerView rv_trams = (RecyclerView) card_minibus.findViewById(R.id.rv_transportation);
        TextView tv_transportation = (TextView) card_minibus.findViewById(R.id.tv_transportation);
        tv_transportation.setText(getString(R.string.minibus));
        String[] miniBusNumbers = getResources().getStringArray(R.array.minibus_numbers);
        minibuses = createItems(miniBusNumbers);
        minibusAdapter = new TransportationAdapter(ShareActivity.this, minibuses, new IItemSelected() {
            @Override
            public void onClick(String transportationLabel) {
                shareInfo.setLabel(transportationLabel);
                shareInfo.setType(TransportType.b.toString());
                emptyOtherList(trams, tramAdapter);
                emptyOtherList(buses, busAdapter);
            }
        });
        TransitIasiLinearLayoutManager lManager = new TransitIasiLinearLayoutManager(ShareActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_trams.setHasFixedSize(true);
        rv_trams.setLayoutManager(lManager);
        rv_trams.setAdapter(minibusAdapter);
    }


    private List<TransportItem> createItems(String[] strings) {
        List<TransportItem> items = new ArrayList<>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            TransportItem item = new TransportItem();
            item.setNumber(strings[i]);
            item.setSelected(false);
            items.add(item);
        }
        return items;
    }

    private void emptyOtherList(List<TransportItem> items, TransportationAdapter adapter) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                items.get(i).setSelected(false);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private void updateCurrentLocation() {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                               @Override
                               public void call(Location location) {
                                   Log.d("INFO2", location.getLatitude() + " " + location.getLongitude());
                                   shareInfo.setLat(location.getLatitude());
                                   shareInfo.setLng(location.getLongitude());
                                   if (shareInfo.getLabel() == null) {
                                       Toast.makeText(ShareActivity.this, R.string.please_select, Toast.LENGTH_SHORT).show();
                                       return;
                                   }

                                   if (!PlatformUtils.isNetworkAvailable(ShareActivity.this)) {
                                       Toast.makeText(ShareActivity.this, R.string.please_turn_on_internet, Toast.LENGTH_SHORT).show();
                                       return;
                                   }
                                   if (location.getLatitude() == 0 && location.getLongitude() == 0) {
                                       Toast.makeText(ShareActivity.this, R.string.please_turn_on_location, Toast.LENGTH_SHORT).show();
                                       return;
                                   }
                                   sendInfo();

                               }
                           }

                );
    }

    private void sendInfo() {
        progress.setVisibility(View.VISIBLE);
        TransitIasiClientApi.defaultService()
                .shareLocation(shareInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShareInfoResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final ShareInfoResponse response) {
                        Log.d("INFO", response.getResponse());

                        progress.setVisibility(View.GONE);
                        Toast.makeText(ShareActivity.this, response.getResponse(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();

                    }
                });

    }

    public interface IItemSelected {
        void onClick(String transportationLabel);
    }

}
