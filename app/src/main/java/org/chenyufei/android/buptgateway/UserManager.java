package org.chenyufei.android.buptgateway;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yfchen on 2018/6/22.
 */

public class UserManager {

    private Context mContext;

    public UserManager(Context context) {
        this.mContext = context;
    }

    public UserInfo readUserInfoFromSharedPreferences() {
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_MULTI_PROCESS);
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        return userInfo;
    }

    public void saveUserInfoToSharedPreferences(UserInfo userInfo) {
        SharedPreferences preferences = mContext.getSharedPreferences("user", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", userInfo.getUsername());
        editor.putString("password", userInfo.getPassword());
        editor.commit();
    }

    public class UserInfo {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
