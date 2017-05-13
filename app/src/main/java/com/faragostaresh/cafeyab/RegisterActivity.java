package com.faragostaresh.cafeyab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.faragostaresh.app.Config;
import com.google.firebase.analytics.FirebaseAnalytics;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set tolbar
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        WebView playerWebView = (WebView) findViewById(R.id.registerWebView);
        playerWebView.clearCache(true);
        playerWebView.clearHistory();
        playerWebView.getSettings().setJavaScriptEnabled(true);
        playerWebView.getSettings().setDomStorageEnabled(true);
        playerWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        playerWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);;
        playerWebView.loadUrl(Config.URL_REGISTER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}