package kr.ac.koreatech.swkang.msp02_accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManger;
    private Sensor mAccel;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccel = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mText = findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // SensorEventListener 등록
        mSensorManger.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // SensorEventListener 해제
        mSensorManger.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mText.setText("Accel: x: " + event.values[0]
                    + " y: " + event.values[1] + " z: " + event.values[2]);
        }
    }
}
