package com.transitiasi.retrofit;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
public class DirectionServiceApi {
    public static final String MODE = "TRANSIT";
    public static final String TRANSIT_MODE = "bus";

    public static final String MAP_API_KEY = "AIzaSyD1kGlEz8q64IFtf1oqBEBlHA_WiuZYjI8";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static DirectionServiceInterface serviceInterface;

    public static DirectionServiceInterface defaultService() {
        if (serviceInterface == null) {
            serviceInterface = RetrofitConfig.newConfig(BASE_URL).create(DirectionServiceInterface.class);
        }
        return serviceInterface;
    }

}
