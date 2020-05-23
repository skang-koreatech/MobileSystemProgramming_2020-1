package kr.ac.koreatech.swkang.msp15_alarmmanagerinservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmService extends Service {

    AlarmManager am;
    PendingIntent pendingIntent;
    Vibrator vib;

    // Alarm 시간이 되었을 때 안드로이드 시스템이 전송해주는 broadcast를 받을 BroadcastReceiver 정의
    // 그리고 다시 동일 시간 후 alarm이 발생하도록 설정한다.
    private BroadcastReceiver AlarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("kr.ac.koreatech.msp.alarm")) {

                //*****************
                // Alarm이 발생하였을 때 필요한 작업 수행
                //-----------------
                // Alarm receiver에서는 장시간에 걸친 연산을 수행하지 않도록 한다
                // Alarm을 발생할 때 안드로이드 시스템에서 wakelock을 잡기 때문에 CPU를 사용할 수 있지만
                // 그 시간은 제한적이기 때문에 애플리케이션에서 필요하면 wakelock을 잡아서 연산을 수행해야 함
                //*****************

                // 여기서는 알람이 발생했다는 것만 확인하기 위해 토스트 메시지 표시, 진동 울림만 수행
                Toast.makeText(AlarmService.this, "Alarm fired!!", Toast.LENGTH_SHORT).show();
                vib.vibrate(500);

                // 다시 5초 후 알람 발생을 위해서 알람 설정
                Intent in = new Intent("kr.ac.koreatech.msp.alarm");
                pendingIntent = PendingIntent.getBroadcast(AlarmService.this, 0, in, 0);
                am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 5000, pendingIntent);
            }

        }
    };

    @Override
    public void onCreate() {

        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        // Alarm 발생 시 전송되는 broadcast를 수신할 receiver 등록
        IntentFilter intentFilter = new IntentFilter("kr.ac.koreatech.msp.alarm");
        registerReceiver(AlarmReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Start Alarm", Toast.LENGTH_SHORT).show();

        //****** 알람 설정 *******************
        // 알람 발생 시 안도로이드 시스템이 broadcast를 전송하도록 PendingIntent 객체 생성
        Intent in = new Intent("kr.ac.koreatech.msp.alarm");
        pendingIntent = PendingIntent.getBroadcast(AlarmService.this, 0, in, 0);

        // 5초 후 알람 발생 설정
        am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(AlarmService.this, "Stop Alarm", Toast.LENGTH_SHORT).show();

        try {
            // Alarm 발생 시 전송되는 broadcast 수신 receiver를 해제
            unregisterReceiver(AlarmReceiver);
        } catch(IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        // AlarmManager에 등록한 alarm 취소
        am.cancel(pendingIntent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
