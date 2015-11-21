package com.transitiasi.retrofit;

import com.transitiasi.model.ShareInfo;
import com.transitiasi.utils.TransportItem;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public interface TransitIasiServiceInterface {
    @POST()
    Observable<String> shareLocation(TransportItem transportItem);

    @GET("/realtime")
    Observable<List<ShareInfo>> realtime(@Query("iteration") int iteration);
}

