package kr.ac.koreatech.swkang.msp18_dcgps;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSMonitor implements LocationListener {

    Context context;
    LocationManager locationManager;

    public GPSMonitor(Context context) {
        this.context = context;

        locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
    }

    public void onStart() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch(SecurityException ex) {
            Log.d("GPSMonitor", "SecurityException: permission required");
        }

    }

    public void onStop() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public void onLocationChanged(Location location) {
        double lng = location.getLongitude();
        double lat = location.getLatitude();

        Intent intent = new Intent("kr.ac.koreatech.msp.dcgpsmonitor");
        intent.putExtra("longitude", lng);
        intent.putExtra("latitude", lat);
        context.sendBroadcast(intent);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }
}
