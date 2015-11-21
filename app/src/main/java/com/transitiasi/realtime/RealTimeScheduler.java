package com.transitiasi.realtime;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.transitiasi.model.ShareInfo;
import com.transitiasi.retrofit.TransitIasiClientApi;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by erusu on 11/21/2015.
 */
public class RealTimeScheduler {

    public static final int REALTIME_DELAY = 2000;

    private OnRealtimeListener listener;
    private boolean stoped = false;
    private Handler handler;
    private int counter;

    public static final RealTimeScheduler INSTANCE = new RealTimeScheduler();

    private RealTimeScheduler(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                Looper.loop();
            }
        }).start();
    }

    private void register(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!stoped) {
                    register();
                    callService();
                }

            }
        },REALTIME_DELAY);
    }

    private void callService(){
        if(stoped){
            return;
        }
        TransitIasiClientApi.defaultService()
                .realtime(counter++)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ShareInfo>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("realtime", "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("realtime", e.toString());
                    }

                    @Override
                    public void onNext(List<ShareInfo> directionResponse) {
                        if (listener != null) {
                            listener.onRealTime(directionResponse);
                        }
                    }
                });
    }

    public synchronized void start(){
        register();
    }

    public synchronized void stop(){
        stoped = true;
    }

    public void setOnRealtimeListener(OnRealtimeListener listener){
        this.listener = listener;
    }

    public static interface OnRealtimeListener{
        public void onRealTime(List<ShareInfo> response);
    }
}
