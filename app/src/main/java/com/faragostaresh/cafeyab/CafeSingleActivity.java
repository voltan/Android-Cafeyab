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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.faragostaresh.app.CafeyabApplication;
import com.faragostaresh.app.Config;
import com.faragostaresh.model.CafeList;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CafeSingleActivity extends AppCompatActivity {

    private static final String TAG = CafeSingleActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    Context context = this;

    ImageLoader imageLoader = CafeyabApplication.getInstance().getImageLoader();

    public static String cafeUrl = "";
    public static String itemId = "";
    public static String itemTitle = "";
    public static String itemUrl = Config.URL_WEBSITE;
    public static String largeUrl = "";
    public static String mapLatitude = "";
    public static String mapLongitude = "";
    public static String mapZoom = "15";
    public static String mapType = "TERRAIN";

    private TextView txtSummary;
    private TextView txtDescription;

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter1;
    private ArrayAdapter<String> listAdapter2;
    private ArrayAdapter<String> listAdapter3;
    private ArrayAdapter<String> listAdapter4;
    private ArrayAdapter<String> listAdapter5;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_single);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get search information
        itemId = null;
        itemTitle = null;

        Bundle extras = getIntent().getExtras();
        itemId = extras.getString("itemId");
        itemTitle = extras.getString("itemTitle");

        cafeUrl = Config.URL_CAFE_SINGLE + itemId;
        Log.d(TAG, "Single item url : " + cafeUrl);

        // Set title
        setTitle(itemTitle);

        new getJson().execute();

        // Set map button
        Button map = (Button) findViewById(R.id.button_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getApplicationContext(), MapSingleActivity.class);
                intent.putExtra("mapLatitude", mapLatitude);
                intent.putExtra("mapLongitude", mapLongitude);
                intent.putExtra("mapZoom", mapZoom);
                intent.putExtra("mapType", mapType);
                intent.putExtra("itemTitle", itemTitle);
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }
        });

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
            progressDialog = new ProgressDialog(CafeSingleActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("در حال دریافت اطلاعات ...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //TextView viewTitle = (TextView) findViewById(R.id.viewTitle);
            //TextView viewCityArea = (TextView) findViewById(R.id.viewCityArea);
            final NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.photo);

            //Creating a json array request
            JsonArrayRequest volleyRequest = new JsonArrayRequest(cafeUrl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //CafeList cafeSingle = new CafeList();
                            Log.d(TAG, response.toString());
                            JSONObject json = null;
                            try {
                                json = response.getJSONObject(0);

                                // Set title
                                setTitle(json.getString("title"));

                                // Set info for layout
                                thumbnail.setImageUrl(json.getString("itemimageUrl"), imageLoader);
                                //viewTitle.setText(json.getString("title"));
                                //viewCityArea.setText(json.getString("city_area"));

                                // Set item url
                                itemUrl = json.getString("itemUrl");

                                // Set item largeUrl
                                largeUrl = json.getString("largeUrl");

                                String[] attributes1 = new String[]{};
                                ArrayList<String> attributesList1 = new ArrayList<String>();
                                attributesList1.addAll(Arrays.asList(attributes1));
                                listAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_cafe_attributes, attributesList1);
                                if (!json.getString("city_area").isEmpty()) {
                                    listAdapter1.add("منطقه : " + json.getString("city_area"));
                                }
                                if (!json.getString("phone1").isEmpty()) {
                                    listAdapter1.add("تلفن : " + json.getString("phone1"));
                                }
                                if (!json.getString("address1").isEmpty()) {
                                    listAdapter1.add("آدرس : " + json.getString("address1"));
                                    listAdapter1.add(" ");
                                }
                                if (!json.getString("address2").isEmpty()) {
                                    listAdapter1.add("آدرس ۲ : " + json.getString("address2"));
                                    listAdapter1.add(" ");
                                }
                                mainListView = (ListView) findViewById(R.id.listView1);
                                mainListView.setAdapter(listAdapter1);
                                Integer attributeCount1 = listAdapter1.getCount();
                                if (attributeCount1 <= 0) {
                                    TextView txtheader1 = (TextView) findViewById(R.id.listTitle1);
                                    txtheader1.setVisibility(View.GONE);
                                } else {
                                    setListViewHeightBasedOnChildren(mainListView);
                                }

                                String[] attributes2 = new String[]{};
                                ArrayList<String> attributesList2 = new ArrayList<String>();
                                attributesList2.addAll(Arrays.asList(attributes2));
                                listAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_cafe_attributes, attributesList2);
                                if (!json.getString("attribute_workinghour").isEmpty()) {
                                    listAdapter2.add("ساعت کار : " + json.getString("attribute_workinghour"));
                                }
                                mainListView = (ListView) findViewById(R.id.listView2);
                                mainListView.setAdapter(listAdapter2);
                                Integer attributeCount2 = listAdapter2.getCount();
                                if (attributeCount2 <= 0) {
                                    TextView txtheader2 = (TextView) findViewById(R.id.listTitle2);
                                    txtheader2.setVisibility(View.GONE);
                                } else {
                                    setListViewHeightBasedOnChildren(mainListView);
                                }

                                String[] attributes3 = new String[]{};
                                ArrayList<String> attributesList3 = new ArrayList<String>();
                                attributesList3.addAll(Arrays.asList(attributes3));
                                listAdapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_cafe_attributes, attributesList3);
                                if (!json.getString("attribute_pros").isEmpty()) {
                                    listAdapter3.add("بهترین های منو : " + json.getString("attribute_pros"));
                                }
                                if (!json.getString("attribute_food").isEmpty()) {
                                    listAdapter3.add("غذا : " + json.getString("attribute_food"));
                                }
                                if (!json.getString("attribute_vegetarian").isEmpty()) {
                                    listAdapter3.add("غذای گیاهی : " + json.getString("attribute_vegetarian"));
                                }
                                if (!json.getString("attribute_breakfast").isEmpty()) {
                                    listAdapter3.add("صبحانه : " +json.getString("attribute_breakfast"));
                                }
                                if (!json.getString("attribute_thirdwavecoffee").isEmpty()) {
                                    listAdapter3.add("قهوه موج سوم : " + json.getString("attribute_thirdwavecoffee"));
                                }
                                if (!json.getString("attribute_parkingspace").isEmpty()) {
                                    listAdapter3.add("جای پارک : " + json.getString("attribute_parkingspace"));
                                }
                                if (!json.getString("attribute_wifi").isEmpty()) {
                                    listAdapter3.add("اینترنت : " + json.getString("attribute_wifi"));
                                }
                                if (!json.getString("attribute_nosmoke").isEmpty()) {
                                    listAdapter3.add("سیگار کشیدن : " + json.getString("attribute_nosmoke"));
                                }
                                if (!json.getString("attribute_toilet").isEmpty()) {
                                    listAdapter3.add("سرویس بهداشتی : " + json.getString("attribute_toilet"));
                                }
                                if (!json.getString("attribute_vip").isEmpty()) {
                                    listAdapter3.add("فضای ویژه VIP : " + json.getString("attribute_vip"));
                                }
                                mainListView = (ListView) findViewById(R.id.listView3);
                                mainListView.setAdapter(listAdapter3);
                                Integer attributeCount3 = listAdapter3.getCount();
                                if (attributeCount3 <= 0) {
                                    TextView txtheader3 = (TextView) findViewById(R.id.listTitle3);
                                    txtheader3.setVisibility(View.GONE);
                                } else {
                                    setListViewHeightBasedOnChildren(mainListView);
                                }

                                String[] attributes4 = new String[]{};
                                ArrayList<String> attributesList4 = new ArrayList<String>();
                                attributesList4.addAll(Arrays.asList(attributes4));
                                listAdapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_cafe_attributes, attributesList4);
                                if (!json.getString("attribute_routingmetro").isEmpty()) {
                                    listAdapter4.add("مترو : " + json.getString("attribute_routingmetro"));
                                    //listAdapter4.add(" ");
                                }
                                if (!json.getString("attribute_routingbus").isEmpty()) {
                                    listAdapter4.add("اتوبوس : " + json.getString("attribute_routingbus"));
                                    //listAdapter4.add(" ");
                                }
                                if (!json.getString("attribute_routingtaxi").isEmpty()) {
                                    listAdapter4.add("تاکسی : " + json.getString("attribute_routingtaxi"));
                                    listAdapter4.add(" ");
                                }
                                mainListView = (ListView) findViewById(R.id.listView4);
                                mainListView.setAdapter(listAdapter4);
                                Integer attributeCount4 = listAdapter4.getCount();
                                if (attributeCount4 <= 0) {
                                    TextView txtheader4 = (TextView) findViewById(R.id.listTitle4);
                                    txtheader4.setVisibility(View.GONE);
                                } else {
                                    setListViewHeightBasedOnChildren(mainListView);
                                }

                                String[] attributes5 = new String[]{};
                                ArrayList<String> attributesList5 = new ArrayList<String>();
                                attributesList5.addAll(Arrays.asList(attributes5));
                                listAdapter5 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_cafe_attributes, attributesList5);
                                if (!json.getString("attribute_cardreader").isEmpty()) {
                                    listAdapter5.add("دستگاه کارت خوان : " + json.getString("attribute_cardreader"));
                                }
                                if (!json.getString("attribute_servicecosts").isEmpty()) {
                                    listAdapter5.add("حق سرویس : " + json.getString("attribute_servicecosts"));
                                }
                                if (!json.getString("attribute_outdoor").isEmpty()) {
                                    listAdapter5.add("فضای باز : " + json.getString("attribute_outdoor"));
                                }
                                if (!json.getString("attribute_music").isEmpty()) {
                                    listAdapter5.add("موزیک : " + json.getString("attribute_music"));
                                }
                                if (!json.getString("attribute_timelimit").isEmpty()) {
                                    listAdapter5.add("محدودیت زمانی : " + json.getString("attribute_timelimit"));
                                }
                                if (!json.getString("attribute_booking").isEmpty()) {
                                    listAdapter5.add("رزرو تلفنی : " + json.getString("attribute_booking"));
                                }
                                if (!json.getString("attribute_takeaway").isEmpty()) {
                                    listAdapter5.add("بیرون بر : " + json.getString("attribute_takeaway"));
                                }
                                if (!json.getString("attribute_capacity").isEmpty()) {
                                    listAdapter5.add("ظرفیت : " + json.getString("attribute_capacity"));
                                }
                                if (!json.getString("attribute_suitableforcouples").isEmpty()) {
                                    listAdapter5.add("مناسب برای قرار دونفره : " + json.getString("attribute_suitableforcouples"));
                                }
                                if (!json.getString("attribute_suitablegroups").isEmpty()) {
                                    listAdapter5.add("مناسب برای قرار گروهی : " + json.getString("attribute_suitablegroups"));
                                }
                                if (!json.getString("attribute_suitablefamily").isEmpty()) {
                                    listAdapter5.add(" مناسب برای خانواده : " + json.getString("attribute_suitablefamily"));
                                }
                                if (!json.getString("attribute_suitablechildren").isEmpty()) {
                                    listAdapter5.add("مناسب برای کودکان : " + json.getString("attribute_suitablechildren"));
                                }
                                if (!json.getString("attribute_suitabledisabled").isEmpty()) {
                                    listAdapter5.add("مناسب برای معلولین : " + json.getString("attribute_suitabledisabled"));
                                }
                                if (!json.getString("attribute_gallery").isEmpty()) {
                                    listAdapter5.add("گالری : " + json.getString("attribute_gallery"));
                                }
                                if (!json.getString("attribute_tv").isEmpty()) {
                                    listAdapter5.add("تلویزیون : " + json.getString("attribute_tv"));
                                }
                                if (!json.getString("attribute_shop").isEmpty()) {
                                    listAdapter5.add("فروشگاه : " + json.getString("attribute_shop"));
                                }
                                if (!json.getString("attribute_delivery").isEmpty()) {
                                    listAdapter5.add("دلیوری(تحویل در محل) : " + json.getString("attribute_delivery"));
                                }
                                if (!json.getString("attribute_selfservice").isEmpty()) {
                                    listAdapter5.add("سلف سرویس : " + json.getString("attribute_selfservice"));
                                }
                                if (!json.getString("attribute_videoprojector").isEmpty()) {
                                    listAdapter5.add("ویدیو پروژکتور : " + json.getString("attribute_videoprojector"));
                                }
                                if (!json.getString("attribute_charge").isEmpty()) {
                                    listAdapter5.add("پریز برق : " + json.getString("attribute_charge"));
                                }
                                mainListView = (ListView) findViewById(R.id.listView5);
                                mainListView.setAdapter(listAdapter5);
                                Integer attributeCount5 = listAdapter5.getCount();
                                if (attributeCount5 <= 0) {
                                    TextView txtheader5 = (TextView) findViewById(R.id.listTitle5);
                                    txtheader5.setVisibility(View.GONE);
                                } else {
                                    setListViewHeightBasedOnChildren(mainListView);
                                }

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

                                if (!json.getString("map_latitude").isEmpty()) {
                                    mapLatitude = json.getString("map_latitude");
                                }
                                if (!json.getString("map_longitude").isEmpty()) {
                                    mapLongitude = json.getString("map_longitude");
                                }
                                if (!json.getString("map_zoom").isEmpty()) {
                                    mapZoom = json.getString("map_zoom");
                                }
                                if (!json.getString("map_type").isEmpty()) {
                                    mapType = json.getString("map_type");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
            //Creating request queue
            RequestQueue requestQueue = Volley.newRequestQueue(CafeSingleActivity.this);

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