package com.transitiasi.util;

import com.google.android.gms.maps.model.LatLng;
import com.transitiasi.model.DirectionResponse;
import com.transitiasi.model.Leg;
import com.transitiasi.model.Route;
import com.transitiasi.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ionut Stirban on 21/11/15.
 */
public class PolylineUtils {
    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }

    public static List<LatLng> decodePath(DirectionResponse directionResponse) {


        Route[] routes = directionResponse.getRoutes();
        if (routes == null || routes.length == 0)
            return Collections.emptyList();

        for (Route route : routes) {
            Leg[] legs = route.getLegs();
            if (legs == null || legs.length == 0)
                return Collections.emptyList();
            for (Leg leg : legs) {
                Step[] steps = leg.getSteps();
                if (steps == null || steps.length == 0)
                    return Collections.emptyList();
                String polyline = steps[0].getPolyline().getPoints();

                List<LatLng> coordinates = PolylineUtils.decode(polyline);
                return  coordinates;
            }
        }


        return Collections.emptyList();

    }
}
