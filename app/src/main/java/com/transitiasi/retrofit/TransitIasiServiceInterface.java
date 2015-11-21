package com.transitiasi.retrofit;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public interface TransitIasiServiceInterface {
//    @GET("blabla")
//    Observable<Response> listRoutes();

    @GET("/realtime")
    public abstract void realtime(@Query("iteration") int iteration);
}
