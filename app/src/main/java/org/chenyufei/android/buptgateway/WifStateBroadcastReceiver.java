package org.chenyufei.android.buptgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by yfchen on 2018/6/21.
 */

public class WifStateBroadcastReceiver extends BroadcastReceiver {

    private GatewayManager gatewayManager;
    private volatile static boolean isRunning;

    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");


        synchronized (WifStateBroadcastReceiver.class) {
            if (isRunning == false) {
                isRunning = true;
            } else {
                return;
            }
        }

        System.out.println(intent.getAction());

        gatewayManager = new GatewayManager(username, password, new GatewayManager.Callback() {
            @Override
            public void onLogin(int msg) {
                System.out.println("login msg = " + msg);
                isRunning = false;
            }

            @Override
            public void onLogout(boolean success) {

            }

            @Override
            public void onCheckCampusNetwork(boolean isCampusNetwork) {
                if (isCampusNetwork) {
                    System.out.println("Connet to campus network");
                    gatewayManager.checkLogin();
                } else {
                    System.out.println("Not campus network");
                    isRunning = false;
                }
            }

            @Override
            public void onCheckLogin(boolean haveLogin) {
                if (!haveLogin) {
                    gatewayManager.login();
                } else {
                    System.out.println("已登录");
                    isRunning =  false;
                }
            }
        });

        // check wifi state
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gatewayManager.checkCampusNetwork();
        } else {
            isRunning = false;
        }
    }
}
