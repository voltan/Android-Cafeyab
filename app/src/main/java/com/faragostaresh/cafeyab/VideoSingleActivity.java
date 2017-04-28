package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.faragostaresh.adaptor.MyTagHandler;
import com.faragostaresh.adaptor.VideoListAdapter;
import com.faragostaresh.model.ItemList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoSingleActivity extends AppCompatActivity {

    private static final String TAG = VideoSingleActivity.class.getSimpleName();

    public static String videoUrl = "";
    public static String itemId = "";

    public TextView txtSummary;
    public TextView txtDescription;

    public ListView listView;
    public VideoListAdapter adapter;
    public List<ItemList> myVideoList = new ArrayList<ItemList>();

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

        adapter = new VideoListAdapter(this, myVideoList, "related");

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

                            txtSummary = (TextView) findViewById(R.id.summary);
                            if (!json.getString("text_summary").isEmpty()) {
                                txtSummary.setText(json.getString("text_summary"));
                            } else {
                                txtSummary.setVisibility(View.GONE);
                            }

                            txtDescription = (TextView) findViewById(R.id.description);
                            if (!json.getString("text_description").isEmpty()) {
                                txtDescription.setText(Html.fromHtml(json.getString("text_description"), null, new MyTagHandler()));
                            } else {
                                txtDescription.setVisibility(View.GONE);
                            }

                            //qmeryDirect
                            WebView playerWebView = (WebView) findViewById(R.id.playerWebView);
                            playerWebView.clearCache(true);
                            playerWebView.clearHistory();
                            playerWebView.getSettings().setJavaScriptEnabled(true);
                            playerWebView.getSettings().setDomStorageEnabled(true);
                            playerWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            playerWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);;
                            playerWebView.loadUrl(json.getString("qmeryDirect"));


                            Log.d(TAG, "videoRelated array: " + json.getJSONArray("videoRelated"));

                            JSONArray jsonArrayRelated = json.getJSONArray("videoRelated");
                            if (jsonArrayRelated.length() > 0) {
                                for (int i = 0; i < jsonArrayRelated.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArrayRelated.getJSONObject(i);
                                        ItemList video = new ItemList();
                                        video.setTitle(obj.getString("title"));
                                        video.setItemID(obj.getString("id"));
                                        video.setThumbnailUrl(obj.getString("mediumUrl"));
                                        myVideoList.add(video);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Set for list of items
                                listView = (ListView) findViewById(R.id.videoRelatedlist);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new VideoSingleActivity.ListVewiClickListener());
                                setListViewHeightBasedOnChildren(listView);
                            }

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


    private class ListVewiClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ItemList getselected = (ItemList) (listView.getItemAtPosition(position));
            itemId = getselected.getItemId();
            displayView(itemId);
        }

        private void displayView(String itemId) {
            // Start New activity with a request to Show Selected News
            Intent intent = new Intent(getApplicationContext(), VideoSingleActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);

        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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