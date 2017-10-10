package com.hooooong.servicebasic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Service 또한 Activity 와 유사하게 생명주기를 가지고 있다.
 * Activity 와 Service 는 동일한 Main Thread 에서 시작하므로 어떠한 반복적인 로직을 만드려면 Thread 안에서 로직을 돌려야 한다.
 * 하지만 extends Service 말고 extends IntentService 는 내부적으로 Thread 가 생성되기 때문에 좋당~
 *
 * 1. Service 가 없을 때 생성되는 startService
 *  - Service 가 있을 때 startService 는 호출이 되지 않고, onStartCommand 가 호출이 된다.
 *  - startService();
 *
 * 2. Service 가 있을 때 연결시켜주는 bindService
 *  - Service 가 없을 때는 startService 를 자동으로 해주는 예외처리가 있다.
 *  - bindService();
 */
public class MyService extends Service {
    public MyService() {
    }

    // Component 는 Binder 를 통해 Service 에 접근할 수 있다.
    // System 자체에서 Intent 를 통해 Service 를 호출하기 때문에 MyService 를 접근할 방법이 없다.
    // 그러기 때문에 Binder 와 Connection 객체를 제공한다.
    class CustomBinder extends Binder {
        public CustomBinder() {
        }
        public MyService getService(){
            return MyService.this;
        }
    }

    IBinder iBinder = new CustomBinder();

    private int total = 0 ;
    private static final int FLAG = 921013;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "---------onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyService", "---------onBind()");
        for(int i = 0 ; i<1000; i++){
            total += i;
            System.out.println(i + "");
            /*if(i == 999){
                stopSelf(startId);
            }*/
        }
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService", "---------onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Foreground Service
        // Foreground id
        startForegroundService();

        // startService 를 하더라도 onCreate 를 호출한 이력이 있으면 onStartCommand 를 호출한다.
        // destroy 를 하게 되면 onCreate 는 최초 한번 호출한다.
        Log.d("MyService", "---------onStartCommand()");

        for(int i = 0 ; i<1000; i++){
            total += i;
            System.out.println(i + "");
            /*if(i == 999){
                stopSelf(startId);
            }*/
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void startForegroundService() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Noti Title").setContentText("Noti Context").build();
        /*NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // 노티바 노출시키기
        // 노티피케이션 매니저를 통해서 노티바를 출력
        manager.notify(FLAG, notification);*/
        startForeground(FLAG, notification);
    }

    private void stopForegroundService(){
        //stopForeground(STOP_FOREGROUND_REMOVE);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(FLAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "---------onDestroy()");
        stopForegroundService();
    }

    public int getTotal(){
        return total;
    }
}
