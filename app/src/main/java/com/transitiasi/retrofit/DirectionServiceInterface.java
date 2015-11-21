package com.transitiasi.retrofit;

import com.transitiasi.model.DirectionResponse;



import retrofit.Call;
import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
public interface DirectionServiceInterface {
    @GET("json")
    Observable<DirectionResponse> getRoutes(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
}
