package org.chenyufei.android.buptgateway;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


/**
 * Created by yfchen on 2018/6/22.
 */

public class GatewayManager {

    private static final String LOGIN_URL = "http://10.3.8.211/";

    public void login(final String username, final String password) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Connection.Response response = Jsoup.connect(LOGIN_URL)
                            .data("DDDDD", username)
                            .data("upass", password)
                            .data("0MKKey", "")
                            .method(Connection.Method.POST)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

    }

    public void logout() {

    }

    private void parseLoginRespose(Connection.Response response) {
        int statusCode = response.statusCode();
        try {
            Document document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
