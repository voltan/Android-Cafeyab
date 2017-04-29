package com.faragostaresh.cafeyab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set user
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String userCheck = settings.getString("user_check", "").toString();
        String userSessionId = settings.getString("user_sessionid", "").toString();
        String userUid = settings.getString("user_uid", "").toString();
        String userIdentity = settings.getString("user_identity", "").toString();
        String userEmail = settings.getString("user_email", "").toString();
        String userName = settings.getString("user_name", "").toString();
        String userAvatar = settings.getString("user_avatar", "").toString();
        if (String.valueOf(userCheck).equals("0")) {
            Intent intentUser = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentUser);
        }

        setContentView(R.layout.activity_user);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Set header
        try {
            ImageView imageView = (ImageView) findViewById(R.id.header_cover_image);
            Glide.with(this).load("https://www.cafeyab.com/upload/app/android/index-cover.jpg").skipMemoryCache(true).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

