package org.chenyufei.android.buptgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class LoginActivity extends AppCompatActivity {

    private Switch mWifiSwitch;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWifiSwitch = findViewById(R.id.wifi_switch);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mWifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mWifiManager.setWifiEnabled(true);
                    mWifiSwitch.setText("Wifi is ON");
                } else {
                    mWifiManager.setWifiEnabled(false);
                    mWifiSwitch.setText("Wifi is OFF");
                }
            }
        });

        if (mWifiManager.isWifiEnabled()) {
            mWifiSwitch.setChecked(true);
            mWifiSwitch.setText("Wifi is ON");
        } else {
            mWifiSwitch.setChecked(false);
            mWifiSwitch.setText("Wifi is OFF");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    mWifiSwitch.setChecked(true);
                    mWifiSwitch.setText("Wifi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    mWifiSwitch.setChecked(false);
                    mWifiSwitch.setText("Wifi is OFF");
                    break;
            }
        }
    };
}
