Android Programing
----------------------------------------------------
### 2017.10.10 15일차

#### 예제
____________________________________________________

- [Service 예제](https://github.com/Hooooong/DAY22_Service/blob/master/app/src/main/java/com/hooooong/servicebasic/MyService.java)

#### 공부정리
____________________________________________________

##### __Service__

- Service 란?

  > Service는 백그라운드에서 오래 실행되는 작업을 수행할 수 있는 애플리케이션 구성 요소이며 사용자 인터페이스를 제공하지 않는다. 또 다른 애플리케이션 구성 요소가 Service 를 시작할 수 있으며, 이는 사용자가 또 다른 애플리케이션으로 전환하더라도 백그라운드에서 계속해서 실행된다. <br>이외에도, 구성 요소를 Service 에 바인드하여 Service 와 상호작용할 수 있으며, 심지어는 프로세스 간 통신(IPC)도 수행할 수 있다. 예를 들어 한 Service 는 네트워크 트랜잭션을 처리하고, 음악을 재생하고 파일 I/O를 수행하거나 콘텐츠 제공자와 상호작용할 수 있으며 이 모든 것을 백그라운드에서 수행할 수 있다.

  - Service 는 `start` 와 `bind` 로 나뉠 수 있다.

  - 간단하게 보면 `start`는 일방적으로 실행하는 Service 이고, `bind`는 구성요소와 Service 가 상호작용할 수 있는 Service 이다.

- Service 실행 방법

  1. __start__

      > Service 를 시작하려면 어플리케이션 구성요소(에 : Activity)가 `startService()` 를 호출하여 시작한다. `startService()` 를 호출하면 `onStartCommand()` 가 실행된다.

      - Service 를 `startService()` 로 시작되면 백그라운드에서 무기한으로 실행될 수 있으며, 구성요소가 소멸되어도 무관하다.

      ```java
      Intent intent = new Intent(MainActivity.this, MyService.class);
      // Service start
      startService(intent);
      ```

      - `startService()` 로 시작되는 Service 는 작업을 수행하고 결과를 반환하지 않는다.

      - `startService()` 를 호출하고, 작업을 완료했으면 `stopSelf()` 또는 `stopService()` 를 호출하여 Service 를 중단시켜야 한다.

  2. __bind__

      > Service 를 Bind 하려면 어플리케이션 구성요소가 `bindService()` 를 호출하여 시작한다. `bindService()` 를 호출하면 `onBind()` 가 실행된다.

      - Service 를 `bindService()` 로 시작하면 지속가능한 Service 와 연결을 생성한다.

      - `bindService()` 는 `ServiceConnection` 객체를 통해 Service 의 bind 상태와 결과를 받아올 수 있다.

      ```java
      // System 자체에서 Intent 를 통해 Service 를 호출하기 때문에 MyService 를 접근할 방법이 없다.
      // 그러기 때문에 Connection 객체와 데이터에 접근할 수 있는 Binder 를 제공한다.
      ServiceConnection con = new ServiceConnection() {
          // Service 와 연결되는 순간 호출되는 함수
          @Override
          public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
              // onServiceConnected 가 호출이 되면 IBinder 객체를 넘겨받을 수 있다.
              // Service 내부 객체에서 IBinder Interface 를 구현한 Binder 를 상속받아야한다.
              MyService myservice = ((MyService.CustomBinder) iBinder).getService();
          }

          // Service 가 중단되거나 연결이 도중에 끊겼을 때 발생한다.
          // 예 ) 정삭적으로 stop 이 호출이 되고, onDestroy 가 발생하면 onServiceDisconnected() 가 호출이 되지 않는다.
          @Override
          public void onServiceDisconnected(ComponentName componentName) {

          }
      };

      Intent intent = new Intent(MainActivity.this, MyService.class);
      // Service Bind
      // Context.BIND_AUTO_CREATE 를 추가하면 Service 생성을 체크하여
      // Service 가 있으면 onBind() 를 호출하고,
      // Service 가 없으면 onCreate() 를 호출하고 onBind() 를 호출한다.
      bindService(intent, con, Context.BIND_AUTO_CREATE);
      ```

      - `IBinder` 객체를 사용하기 위해 Service 내부 객체에서 IBinder Interface 를 구현한 Binder 를 상속받아 구현해야 한다.

      ```java
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
          // ... 생략
      }
      ```

- Service 생명 주기

  ![Service 생명주기](https://github.com/Hooooong/DAY22_Service/blob/master/image/Service%20lifecycle.png)

  메소드 | 설명
  :----: | :----:
  onCreate() | Service 가 생성되어 일회성 절차를 수행(onStartCommand() 또는 onBind() 호출하기 전에).<br> Service 가 이미 실행중인 경우, 이 메소드는 호출되지 않는다
  onStartCommand() | startService() 를 호출하면 실행되는 메소드. <br>이 메소드가 실행되면 백그라운드에서 무기한으로 실행될 수 있고, Service 의 작업이 완료되었을 때 해당 Service 를 중단하는 것은 개발자 본인의 책임이며, 이때 stopSelf() 또는 stopService()를 호출하면 된다
  onBind() | bindService() 를 호출하면 실행되는 메소드.<br> onBind() 를 호출하기 위해서는 ServiceConnection 를 제공해야 한다.<br> 이 메소드 또한 백그라운드에서 무기한으로 실행될 수 있으며, unbindService() 를 호출하여 Service 의 bind 를 해제한다
  onUnbind() | unbindService() 를 호출하면 실행되는 메소드. Service 의 bind 를 해제한다
  onDestroy() | 해당 Service 를 더 이상 사용하지 않고 소멸시키는 경우 호출

- 참조 : [Android Service](https://developer.android.com/guide/components/services.html?hl=ko#Foreground)
