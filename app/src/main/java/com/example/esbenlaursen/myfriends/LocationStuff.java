package com.example.esbenlaursen.myfriends;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Esben on 01-05-2017.
 */

public class LocationStuff {

    public Location getLastKnownLocation(EditFriend e)
    {
        //FIRST PERMISSION
        if (ActivityCompat.checkSelfPermission(e, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(e, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(e, permissions ,100);
        }

        //GETTING THE MOST ACCURACY LOCATION
        android.location.LocationManager mLocationManager = (android.location.LocationManager) e.getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
