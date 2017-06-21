package com.faragostaresh.cafeyab;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.faragostaresh.adaptor.MyTagHandler;
import com.faragostaresh.adaptor.VideoListAdapter;
import com.faragostaresh.app.CafeyabApplication;
import com.faragostaresh.app.Config;
import com.faragostaresh.model.ItemList;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventSingleActivity extends AppCompatActivity {

    private static final String TAG = EventSingleActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    Context context = this;

    ImageLoader imageLoader = CafeyabApplication.getInstance().getImageLoader();

    public static String eventUrl = "";
    public static String itemId = "";
    public static String itemTitle = "";
    public static String itemUrl = Config.URL_WEBSITE;
    public static String largeUrl = "";

    public TextView txtSummary;
    public TextView txtDescription;
    public TextView registerDetails;

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter1;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_single);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get search information
        Bundle extras = getIntent().getExtras();
        itemId = extras.getString("itemId");
        itemTitle = extras.getString("itemTitle");
        eventUrl = Config.URL_EVENT_SINGLE + itemId;

        Log.d(TAG, "Single item url : " + eventUrl);

        // Set title
        setTitle(itemTitle);

        new getJson().execute();

        // Set shear bottom
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.facebook.katana")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.instagram")) {

                        intent.setType("image/jpeg");
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.pinterest")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.google.android.gm")) {

                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_TITLE, itemTitle);
                        intent.putExtra(Intent.EXTRA_SUBJECT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.hootsuite.droid.full")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.facebook.pages.app")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("org.telegram.messenger")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        targetShareIntents.add(intent);

                    } /* else {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } */
                }

                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);

            }
        });
    }


    public class getJson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EventSingleActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("در حال دریافت اطلاعات ...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.photo);

            //Creating a json array request
            JsonArrayRequest volleyRequest = new JsonArrayRequest(eventUrl,
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
                                thumbnail.setImageUrl(json.getString("largeUrl"), imageLoader);

                                // Set info for layout
                                //TextView textViewTitle = (TextView) findViewById(R.id.viewTitle);
                                //textViewTitle.setText(json.getString("title"));

                                //TextView textSubViewTitle = (TextView) findViewById(R.id.viewSubTitle);
                                //textViewTitle.setText(json.getString("subtitle"));

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

                                registerDetails = (TextView) findViewById(R.id.register_details);
                                if (!json.getString("register_details").isEmpty()) {
                                    registerDetails.setText(Html.fromHtml(json.getString("register_details"), null, new MyTagHandler()));
                                } else {
                                    registerDetails.setVisibility(View.GONE);
                                    TextView txtheaderRegister = (TextView) findViewById(R.id.register_details_title);
                                    txtheaderRegister.setVisibility(View.GONE);
                                }

                                // Set item url
                                itemUrl = json.getString("eventUrl");

                                // Set item largeUrl
                                largeUrl = json.getString("largeUrl");


                                String[] attributes1 = new String[]{};
                                ArrayList<String> attributesList1 = new ArrayList<String>();
                                attributesList1.addAll(Arrays.asList(attributes1));
                                listAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_cafe_attributes, attributesList1);
                                if (!json.getString("time_view").isEmpty()) {
                                    listAdapter1.add("زمان برگزاری : " + json.getString("time_view"));
                                }
                                if (!json.getString("organizer_name").isEmpty()) {
                                    listAdapter1.add("سازمان دهنده : " + json.getString("organizer_name"));
                                }
                                if (!json.getString("register_price_view").isEmpty()) {
                                    listAdapter1.add("هزینه ثبت نام  : " + json.getString("register_price_view"));
                                }
                                if (!json.getString("address").isEmpty()) {
                                    listAdapter1.add("آدرس : " + json.getString("address"));
                                    listAdapter1.add(" ");
                                }
                                mainListView = (ListView) findViewById(R.id.listView);
                                mainListView.setAdapter(listAdapter1);
                                Integer attributeCount1 = listAdapter1.getCount();
                                if (attributeCount1 <= 0) {
                                    TextView txtheader1 = (TextView) findViewById(R.id.listEventTitle);
                                    txtheader1.setVisibility(View.GONE);
                                } else {
                                    setListViewHeightBasedOnChildren(mainListView);
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
            RequestQueue requestQueue = Volley.newRequestQueue(EventSingleActivity.this);

            // Add retry policy
            volleyRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //Adding request to the queue
            requestQueue.add(volleyRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 1000);
        }
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
}