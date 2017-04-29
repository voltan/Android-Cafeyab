package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class LogoutActivity extends AppCompatActivity {

    private static final String TAG = LogoutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String str = "https://www.cafeyab.com/usmartphone/logout";
        Log.d(TAG, str);
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str);
            urlConn = url.openConnection();

            // User info
            SharedPreferences settings = getSharedPreferences("UserInfo", 0);
            String userSessionId = settings.getString("user_sessionid", "").toString();
            urlConn.setRequestProperty("Cookie", "pisess=" + userSessionId);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("user_check", "0");
            editor.putString("user_sessionid", "");
            editor.putString("user_uid", "");
            editor.putString("user_identity", "");
            editor.putString("user_email", "");
            editor.putString("user_name", "");
            editor.putString("user_avatar", "");
            editor.commit();

            startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
            finish();

        } catch (Exception ex) {
            Log.e(TAG, "yourDataTask", ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}