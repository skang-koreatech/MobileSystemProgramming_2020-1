package kr.ac.koreatech.swkang.accellinearaccel;

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
    private Sensor mLinearAccel;
    TextView mText;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccel = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLinearAccel = mSensorManger.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mText = findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // SensorEventListener 등록
        mSensorManger.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
        mSensorManger.registerListener(this, mLinearAccel, SensorManager.SENSOR_DELAY_UI);
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
            data = "Accel: \nx: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2];
        } else if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            data = data + "\nLinear Accel: \nx: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2];
        }
        mText.setText(data);
    }
}
