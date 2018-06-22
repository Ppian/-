package org.chenyufei.android.buptgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Switch mWifiSwitch;
    private WifiManager mWifiManager;
    private EditText mUserEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Read username and password from SharedPreferences
        readUserFromSharedPreferences();

        mWifiSwitch = findViewById(R.id.wifi_switch);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mUserEditText = findViewById(R.id.user_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mLoginButton = findViewById(R.id.login_button);

        mUserEditText.setText(username);
        mPasswordEditText.setText(password);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if(username == null || username.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password == null || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check wifi state
                if (!mWifiManager.isWifiEnabled()) {
                    //
                }



                // Save username and password to SharedPreferences
                saveUserToSharedPreferences();
            }
        });

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
//        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        registerReceiver(wifiStateReceiver, intentFilter);
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

    private void readUserFromSharedPreferences() {
        SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        password = preferences.getString("password", "");
    }

    private void saveUserToSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }
}
