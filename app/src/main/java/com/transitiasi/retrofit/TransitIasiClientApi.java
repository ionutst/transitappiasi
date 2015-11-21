package com.transitiasi.retrofit;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public class TransitIasiClientApi {
    private static final String BASE_URL = "https://morning-anchorage-4243.herokuapp.com/shareLocation";
    private static TransitIasiServiceInterface serviceInterface;

    public static TransitIasiServiceInterface defaultService() {
        if (serviceInterface == null) {
            serviceInterface = RetrofitConfig.newConfig(BASE_URL).create(TransitIasiServiceInterface.class);
        }
        return serviceInterface;
    }


}
