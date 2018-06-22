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

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        // check wifi state
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // if wifi is enabled
        if (wifiManager.isWifiEnabled()) {
            // then check whether the wifi connect to campus network
            boolean isCampusNetwork = true;
            // if campus network
            if (isCampusNetwork) {
                // then login to gateway
                if (username == null || username.equals("")) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }

        }



        Toast.makeText(context, intent.getAction() + username + password, Toast.LENGTH_SHORT).show();
    }
}
