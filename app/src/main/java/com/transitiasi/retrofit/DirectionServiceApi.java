package com.transitiasi.retrofit;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
public class DirectionServiceApi {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static DirectionServiceInterface serviceInterface;

    public static DirectionServiceInterface defaultService() {
        if (serviceInterface == null) {
            serviceInterface = RetrofitConfig.newConfig(BASE_URL).create(DirectionServiceInterface.class);
        }
        return serviceInterface;
    }

}
