package kr.ac.koreatech.swkang.msp15_alarmmanagerinservice;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {

        if(view.getId() == R.id.start) {
            Intent intent = new Intent(MainActivity.this, AlarmService.class);
            startService(intent);
        } else if(view.getId() == R.id.stop) {
            stopService(new Intent(MainActivity.this, AlarmService.class));
        }
    }
}
