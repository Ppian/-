package org.chenyufei.android.buptgateway;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mLogoutButton;

    private UserManager mUserManager;
    private UserManager.UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Read user info from SharedPreferences
        mUserManager = new UserManager(this);
        mUserInfo = mUserManager.readUserInfoFromSharedPreferences();

        mUserEditText = findViewById(R.id.user_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mLoginButton = findViewById(R.id.login_button);
        mLogoutButton = findViewById(R.id.logout_button);

        mUserEditText.setText(mUserInfo.getUsername());
        mPasswordEditText.setText(mUserInfo.getPassword());

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

                // Save user info to SharedPreferences
                mUserInfo.setUsername(username);
                mUserInfo.setPassword(password);
                mUserManager.saveUserInfoToSharedPreferences(mUserInfo);

                new GatewayManager(username, password, gatewayCallback).login();

            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GatewayManager("", "", gatewayCallback).logout();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GatewayManager(mUserInfo.getUsername(), mUserInfo.getPassword(), gatewayCallback).checkLogin();
    }

    private GatewayManager.Callback gatewayCallback = new GatewayManager.Callback() {
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
            handler.sendMessage(message);
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
            handler.sendMessage(message);
        }

        @Override
        public void onCheckCampusNetwork(boolean isCampusNetwork) {

        }

        @Override
        public void onCheckLogin(boolean haveLogin) {
            if (haveLogin) {
                Message message = new Message();
                Bundle messageBundle = new Bundle();
                String data = "已登录";
                messageBundle.putString("data", data);
                message.setData(messageBundle);
                handler.sendMessage(message);
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
        }
    };
}
