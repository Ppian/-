package org.chenyufei.android.buptgateway;

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

    private UserManager mUserManager;
    private UserManager.UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Read username and password from SharedPreferences
        mUserManager = new UserManager(this);
        mUserInfo = mUserManager.readUserInfoFromSharedPreferences();

        mUserEditText = findViewById(R.id.user_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mLoginButton = findViewById(R.id.login_button);

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

                // Save username and password to SharedPreferences
                mUserInfo.setUsername(username);
                mUserInfo.setPassword(password);
                mUserManager.saveUserInfoToSharedPreferences(mUserInfo);

                new GatewayManager().login(username, password);
            }
        });
    }
}
