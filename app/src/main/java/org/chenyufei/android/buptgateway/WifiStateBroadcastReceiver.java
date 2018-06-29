package org.chenyufei.android.buptgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;




/**
 * Created by yfchen on 2018/6/21.
 */

public class WifiStateBroadcastReceiver extends BroadcastReceiver {
    
    private static final String TAG = WifiStateBroadcastReceiver.class.getSimpleName();

    private Context mContext;
    private GatewayManager gatewayManager;
    private volatile static boolean isRunning;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        Log.d(TAG, "onReceive: " + intent.getAction());

        synchronized (WifiStateBroadcastReceiver.class) {
            if (isRunning == false) {
                isRunning = true;
            } else {
                return;
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check wifi state
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            gatewayManager = new GatewayManager("", "", mCallback);
            gatewayManager.checkCampusNetwork();
        } else {
            isRunning = false;
        }
    }

    private GatewayManager.Callback mCallback = new GatewayManager.Callback() {
        @Override
        public void onLogin(int msg) {
            Message message = new Message();
            Bundle messageBundle = new Bundle();
            String data;
            if (msg == -1) {
                data = "自动登录失败，请检查网络";
            } else if (msg == 15) {
                data = "自动登录成功";
            } else {
                data = "自动登录失败，请检查账号密码是否正确";
            }
            messageBundle.putString("data", data);
            message.setData(messageBundle);
            mHandler.sendMessage(message);

            isRunning = false;
        }

        @Override
        public void onLogout(boolean success) {

        }

        @Override
        public void onCheckCampusNetwork(boolean isCampusNetwork) {
            if (isCampusNetwork) {
//                Message message = new Message();
//                Bundle messageBundle = new Bundle();
//                String data = "连接到校园网";
//                messageBundle.putString("data", data);
//                message.setData(messageBundle);
//                mHandler.sendMessage(message);
                gatewayManager.checkLogin();
            } else {
                isRunning = false;
            }
        }

        @Override
        public void onCheckLogin(boolean haveLogin) {
            if (!haveLogin) {
                SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_MULTI_PROCESS);
                String username = preferences.getString("username", "");
                String password = preferences.getString("password", "");
                Log.d(TAG, "onReceive: username = " + username + " password = " + password);
                if (username.equals("")) {
                    Message message = new Message();
                    Bundle messageBundle = new Bundle();
                    String data = "自动登录失败，请先保存账号和密码";
                    messageBundle.putString("data", data);
                    message.setData(messageBundle);
                    mHandler.sendMessage(message);
                    isRunning = false;
                } else {
                    gatewayManager.setUsername(username);
                    gatewayManager.setPassword(password);
                    gatewayManager.login();
                }
            } else {
                Message message = new Message();
                Bundle messageBundle = new Bundle();
                String data = "已登录";
                messageBundle.putString("data", data);
                message.setData(messageBundle);
                mHandler.sendMessage(message);

                isRunning = false;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            Toast.makeText(mContext, "自邮门：" + data, Toast.LENGTH_SHORT).show();
        }
    };
}
