package org.chenyufei.android.buptgateway;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TabHost;
import android.widget.Toast;

public class ShortcutActivity extends Activity {

    private static final String TAG = ShortcutActivity.class.getSimpleName();
    private static final String ACTION_LOGIN = "org.chenyufei.android.buptgateway.ACTION_LOGIN";
    private static final String ACTION_LOGOUT = "org.chenyufei.android.buptgateway.ACTION_LOGOUT";

    private GatewayManager mGatewayManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "自邮门：请先连接校园网", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = preferences.getString("username", "");
            String password = preferences.getString("password", "");

            mGatewayManager = new GatewayManager(username, password, mCallback);

            String action = getIntent().getAction();
            if (action.equals(ACTION_LOGIN)) {
                mGatewayManager.checkLogin();
            } else if (action.equals(ACTION_LOGOUT)) {
                mGatewayManager.logout();
            } else {

            }
        }

        finish();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            Toast.makeText(ShortcutActivity.this, "自邮门：" + data, Toast.LENGTH_SHORT).show();
        }
    };

    private GatewayManager.Callback mCallback = new GatewayManager.Callback() {
        @Override
        public void onLogin(int msg) {
            Message message = new Message();
            Bundle messageBundle = new Bundle();
            String data;
            if (msg == -1) {
                data = "登录失败，请检查网络";
            } else if (msg == 15) {
                data = "登录成功";
            } else {
                data = "登录失败，请检查账号密码是否正确";
            }
            messageBundle.putString("data", data);
            message.setData(messageBundle);
            mHandler.sendMessage(message);
        }

        @Override
        public void onLogout(boolean success) {
            Message message = new Message();
            Bundle messageBundle = new Bundle();
            String data;
            if (success) {
                data = "注销成功";
            } else {
                data = "注销失败";
            }
            messageBundle.putString("data", data);
            message.setData(messageBundle);
            mHandler.sendMessage(message);
        }

        @Override
        public void onCheckCampusNetwork(boolean isCampusNet) {

        }

        @Override
        public void onCheckLogin(boolean haveLogin) {
            if (haveLogin) {
                Message message = new Message();
                Bundle messageBundle = new Bundle();
                String data = "已登录";
                messageBundle.putString("data", data);
                message.setData(messageBundle);
                mHandler.sendMessage(message);
            } else {
                mGatewayManager.login();
            }
        }
    };
}
