package com.hooooong.servicebasic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Service 관련 flag
    boolean isService = false;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, MyService.class);
    }

    // 서비스 시작
    public void start(View view) {
        startService(intent);
    }

    // 서비스 종료
    public void stop(View view) {
        stopService(intent);
    }


    // Service 를 받아두는 변수
    MyService service;
    // System 자체에서 Intent 를 통해 Service 를 호출하기 때문에 MyService 를 접근할 방법이 없다.
    // 그러기 때문에 Connection 객체와 데이터에 접근할 수 있는 Binder 를 제공한다.
    ServiceConnection con = new ServiceConnection() {
        // 서비스와 연결되는 순간 호출되는 함수
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isService = true;
            // onServiceConnected 가 호출이 되면 IBinder 객체를 넘겨받을 수 있다.
            // Service 내부 객체에서 IBinder Interface 를 구현한 Binder 를 상속받아야한다.
            service = ((MyService.CustomBinder) iBinder).getService();

            Toast.makeText(MainActivity.this, "Total : " + service.getTotal(), Toast.LENGTH_SHORT).show();
        }

        // 서비스가 중단되거나 연결이 도중에 끊겼을 때 발생한다.
        // 예 ) 정삭적으로 stop 이 호출이 되고, onDestroy 가 발생하면 onServiceDisconnected() 가 호출이 되지 않는다.
        // 왜 이렇게 만들었을까? 나도몰라~
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void bind(View view) {
        // Context.BIND_AUTO_CREATE
        // bindService 를 호출했을 때 Service 가 없을 경우 onCreate 를 호출해주는 flag
        bindService(intent, con, Context.BIND_AUTO_CREATE);
    }

    public void unbind(View view) {
        if (isService) {
            unbindService(con);
            isService = false;
        }
    }
    public void startFore(View view){
    }

    public void stopFore(View view){
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*stopService(intent);
        if (isService) {
            unbindService(con);
            isService = false;
        }*/
    }
}
