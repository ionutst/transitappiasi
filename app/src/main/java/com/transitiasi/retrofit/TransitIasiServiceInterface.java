package com.transitiasi.retrofit;

import com.transitiasi.model.ShareInfo;
import com.transitiasi.model.ShareInfoResponse;
import com.transitiasi.utils.TransportItem;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public interface TransitIasiServiceInterface {
    @POST("shareLocation")
    Observable<ShareInfoResponse> shareLocation(@Body ShareInfo shareInfo);
}
