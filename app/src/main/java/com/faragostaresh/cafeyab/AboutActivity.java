package com.faragostaresh.cafeyab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.faragostaresh.app.Config;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    Context context = this;

    String appUrl = Config.URL_APP_URL;
    String appLogoUrl = Config.URL_APP_LOGO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set photo Header
        /* View photoHeader = findViewById(R.id.photoHeader);
        try {
            CircleImageView imageView = (CircleImageView) findViewById(R.id.civProfilePic);
            Glide.with(this).load(R.drawable.logo).skipMemoryCache(true).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        } */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                List<Intent> targetShareIntents = new ArrayList<Intent>();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");

                List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo ri : resInfos) {

                    String packageName = ri.activityInfo.packageName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setPackage(packageName);

                    if (packageName.contains("com.twitter.android")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.facebook.katana")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.instagram")) {

                        intent.setType("image/jpeg");
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.pinterest")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.google.android.gm")) {

                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_TITLE, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.hootsuite.droid.full")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.facebook.pages.app")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("org.telegram.messenger")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        targetShareIntents.add(intent);

                    } else {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_name));
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, appLogoUrl);
                        targetShareIntents.add(intent);

                    }
                }

                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "به اشتراک بگذارید");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            }
        });


        // Faragostaresh
        TextView faragistareshCopyright = (TextView) findViewById(R.id.faragostareshCopyright);
        faragistareshCopyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Config.URL_FARAGOSTARESH));
                startActivity(intent);
            }
        });

        ImageView faragostareshLogo = (ImageView) findViewById(R.id.faragostareshLogo);
        faragostareshLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Config.URL_FARAGOSTARESH));
                startActivity(intent);
            }
        });
    }
}