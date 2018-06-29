package org.chenyufei.android.buptgateway;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yfchen on 2018/6/26.
 */

public class NetworkStateService extends Service {
    
    private static final String TAG = NetworkStateService.class.getSimpleName();

    private WifiStateBroadcastReceiver mWifiStateBroadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiStateBroadcastReceiver = new WifiStateBroadcastReceiver();
        IntentFilter mFilter = new IntentFilter();
        //mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mWifiStateBroadcastReceiver, mFilter);

        //Toast.makeText(this, "自邮门服务开启", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiStateBroadcastReceiver);
        //Toast.makeText(this, "自邮门服务关闭", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy");
    }

    public class MyBinder extends Binder {

    }
}
