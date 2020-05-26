package kr.ac.koreatech.swkang.msp18_dcgps;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView longText;
    TextView latiText;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean isPermitted = false;
    boolean isServiceRun = false;

    private BroadcastReceiver MyGPSReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("kr.ac.koreatech.msp.dcgpsmonitor")) {
                double longitude = intent.getDoubleExtra("longitude", 0.0);
                double latitude = intent.getDoubleExtra("latitude", 0.0);
                longText.setText("GPS Longitude: " + longitude);
                latiText.setText("GPS Latitude: " + latitude);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longText = findViewById(R.id.gpslong);
        latiText = findViewById(R.id.gpslati);

        requestRuntimePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("kr.ac.koreatech.msp.dcgpsmonitor");
        registerReceiver(MyGPSReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyGPSReceiver);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.startMonitor) {
            if(isPermitted) {
                Intent intent = new Intent(this, PeriodicMonitorService.class);
                startService(intent);
                isServiceRun = true;
            } else {
                Toast.makeText(this, "위치 데이터에 대한 런타임 퍼미션이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == R.id.stopMonitor) {
            if (isServiceRun) {
                stopService(new Intent(this, PeriodicMonitorService.class));
                longText.setText("GPS Longitude: " + 0);
                latiText.setText("GPS Latitude: " + 0);
            }
        }
    }

    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            // ACCESS_FINE_LOCATION 권한이 있는 것
            isPermitted = true;
        }
        //*********************************************************************
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // read_external_storage-related task you need to do.

                    // ACCESS_FINE_LOCATION 권한을 얻음
                    isPermitted = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    // 권한을 얻지 못 하였으므로 location 요청 작업을 수행할 수 없다
                    // 적절히 대처한다
                    isPermitted = false;

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
