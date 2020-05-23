package kr.ac.koreatech.swkang.msp17_dcstepmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView rmsText;
    private double rms;

    private BroadcastReceiver MyStepReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("kr.ac.koreatech.msp.dcstepmonitor")) {
                rms = intent.getDoubleExtra("rms", 0.0);
                rmsText.setText("rms: " + rms);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rmsText = (TextView)findViewById(R.id.rms);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("kr.ac.koreatech.msp.dcstepmonitor");
        registerReceiver(MyStepReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyStepReceiver);
    }

    // Start/Stop 버튼을 눌렀을 때 호출되는 콜백 메소드
    // Activity monitoring을 수행하는 service 시작/종료
    public void onClick(View v) {
        if(v.getId() == R.id.startMonitor) {
            Intent intent = new Intent(this, PeriodicMonitorService.class);
            startService(intent);
        } else if(v.getId() == R.id.stopMonitor) {
            stopService(new Intent(this, PeriodicMonitorService.class));
            rmsText.setText("rms: " + 0);
        }
    }
}
