package com.faragostaresh.cafeyab;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.faragostaresh.app.Config;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class LogoutActivity extends AppCompatActivity {

    private static final String TAG = LogoutActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("از اپلیکشن خاریج می شود؟");
        builder.setMessage("در صورت خروج از اپلیکشن برای استفاده بعدی مجددا باید با اطلاعات ورود خود به اپلیکشن وارد شوید");
        builder.setCancelable(false);
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String str = Config.URL_LOGOUT;
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

                    editor.putString("user_first_name", "");
                    editor.putString("user_last_name", "");
                    editor.putString("user_id_number", "");
                    editor.putString("user_phone", "");
                    editor.putString("user_mobile", "");
                    editor.putString("user_address1", "");
                    editor.putString("user_zip_code", "");
                    editor.putString("user_company", "");

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



        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        builder.show();

    }

}