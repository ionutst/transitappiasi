package com.transitiasi.retrofit;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public class TransitIasiClientApi {

    public void realtime(int iteration, final ICallbackListener listener){

    }

    public static interface ICallbackListener {
        public void onSuccess();

        public void onFail();
    }
}
