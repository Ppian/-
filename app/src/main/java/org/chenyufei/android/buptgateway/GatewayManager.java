package org.chenyufei.android.buptgateway;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by yfchen on 2018/6/22.
 */

public class GatewayManager {

    private static final String LOGIN_URL = "http://10.3.8.211/";
    private static final String LOGOUT_URL = "http://10.3.8.211/F.htm";
    public static final String CHECK_LOGIN_URL = "http://www.baidu.com";

    private String username;
    private String password;
    private Callback mCallback;

    public GatewayManager(@NonNull String username, @NonNull String password, @NonNull Callback callback) {
        this.username = username;
        this.password = password;
        this.mCallback = callback;
    }

    public void login() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Connection.Response response = Jsoup.connect(LOGIN_URL)
                            .data("DDDDD", username)
                            .data("upass", password)
                            .data("0MKKey", "")
                            .method(Connection.Method.POST)
                            .execute();
                    parseLoginRespose(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.onLogin(-1);
                }
            }
        }.start();

    }

    public void logout() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Connection.Response response = Jsoup.connect(LOGOUT_URL)
                            .method(Connection.Method.GET)
                            .execute();
                    parseLogoutRespose(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.onLogout(false);
                }
            }
        }.start();

    }

    public void checkCampusNetwork() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Connection.Response response = Jsoup.connect(LOGIN_URL)
                            .method(Connection.Method.GET)
                            .execute();
                    mCallback.onCheckCampusNetwork(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.onCheckCampusNetwork(false);
                }
            }
        }.start();
    }

    public void checkLogin() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Connection.Response response = Jsoup.connect(CHECK_LOGIN_URL)
                            .method(Connection.Method.GET)
                            .execute();
                    if (response.body().contains("上网注销窗")) {
                        mCallback.onCheckLogin(true);
                    } else if (response.body().contains("欢迎登录北邮校园网络")) {
                        mCallback.onCheckLogin(false);
                    } else {
                        mCallback.onCheckLogin(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.onCheckLogin(false);
                }
            }
        }.start();
    }

    private void parseLoginRespose(Connection.Response response) {
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            mCallback.onLogin(-1);
            return;
        }

        String responseContent = response.body();
        if (responseContent.contains("登录成功窗")) {
            mCallback.onLogin(15);
        } else  {
            Pattern pattern = Pattern.compile("Msg=(\\d+)");
            Matcher matcher = pattern.matcher(responseContent);

            if (matcher.find()) {
                int msg = Integer.parseInt(matcher.group(1));
                mCallback.onLogin(msg);
            } else {
                mCallback.onLogin(-1);
            }
        }

    }

    private void parseLogoutRespose(Connection.Response response) {
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            mCallback.onLogout(false);
        } else {
            mCallback.onLogout(true);
        }
    }

    public static interface Callback {
        void onLogin(int msg);
        void onLogout(boolean success);
        void onCheckCampusNetwork(boolean isCampusNet);
        void onCheckLogin(boolean haveLogin);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
