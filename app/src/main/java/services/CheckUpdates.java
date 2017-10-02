package services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class CheckUpdates extends Service {
    public static final String BROADCAST_ACTION = "service";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        // Called on service created
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onDestroy() {
        // Called on service stopped
        stopService(intent);
    }

    @Override
    public void onStart(Intent intent, int startid) {
        int i = 0;
        while (i <= 2) {
            if (i > 1) {
                i++;
                //this.onDestroy();
            } else {
                counter = i;
                i++;
                handler.removeCallbacks(sendUpdatesToUI);
                handler.postDelayed(sendUpdatesToUI, 1 * 1000); // 1 sec
            }

        }

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 7 * 1000); // 7 sec
        }
    };

    private void DisplayLoggingInfo() {
        sendBroadcast(intent);
        Toast.makeText(this, "Weather was update", Toast.LENGTH_SHORT).show();
       // stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}