package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoSingleActivity extends AppCompatActivity {

    private static final String TAG = VideoSingleActivity.class.getSimpleName();

    public static String videoUrl = "";
    public static String itemId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_single);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Get search information
        Bundle extras = getIntent().getExtras();
        itemId = extras.getString("itemId");
        videoUrl = "https://www.cafeyab.com/video/json/videoSingle/id/" + itemId;

        Log.d(TAG, "Single item url : " + videoUrl);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(videoUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //VideoList videoSingle = new VideoList();
                        Log.d(TAG, response.toString());
                        JSONObject json = null;
                        try {
                            json = response.getJSONObject(0);

                            // Set title
                            setTitle(json.getString("title"));

                            // Set info for layout
                            TextView textViewTitle = (TextView) findViewById(R.id.viewTitle);
                            textViewTitle.setText(json.getString("title"));

                            //qmeryDirect
                            WebView playerWebView = (WebView) findViewById(R.id.playerWebView);
                            playerWebView.clearCache(true);
                            playerWebView.clearHistory();
                            playerWebView.getSettings().setJavaScriptEnabled(true);
                            playerWebView.getSettings().setDomStorageEnabled(true);
                            playerWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            playerWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);;
                            playerWebView.loadUrl(json.getString("qmeryDirect"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /* case R.id.menu_home:
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                break; */

            case R.id.menu_user:
                Intent intentUser = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intentUser);
                break;

            case R.id.menu_about:
                Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intentAbout);
                break;

            /* case R.id.menu_search:
                Intent intentSearch = new Intent(getApplicationContext(), CafeListActivity.class);
                startActivity(intentSearch);
                break; */
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        WebView playerWebView = (WebView) findViewById(R.id.playerWebView);
        playerWebView.onPause();
        playerWebView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        WebView playerWebView = (WebView) findViewById(R.id.playerWebView);
        playerWebView.resumeTimers();
        playerWebView.onResume();
    }

    @Override
    public void onDestroy() {
        WebView playerWebView = (WebView) findViewById(R.id.playerWebView);
        playerWebView.destroy();
        playerWebView = null;
        super.onDestroy();
    }
}