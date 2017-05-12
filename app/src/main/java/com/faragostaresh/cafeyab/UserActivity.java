package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * http://androidbox.me/linkedin-profile-app/
 */
public class UserActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set photo Header
        View photoHeader = findViewById(R.id.photoHeader);
        try {
            userAvatar = userAvatar.replace("s=80", "s=600");
            CircleImageView imageView = (CircleImageView) findViewById(R.id.civProfilePic);
            Glide.with(this).load(userAvatar).skipMemoryCache(true).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoHeader.setTranslationZ(6);
            photoHeader.invalidate();
        }

        // Set texts
        TextView userNameView = (TextView) findViewById(R.id.userName);
        userNameView.setText(userName);

        // Set texts
        TextView userEmailView = (TextView) findViewById(R.id.userEmail);
        userEmailView.setText(userEmail);

        // Set Button
        Button logout = (Button) findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
                startActivity(intent);
            }
        });

        // Set Button
        Button website = (Button) findViewById(R.id.btnWebsite);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                String url = "https://www.cafeyab.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}

