package com.faragostaresh.cafeyab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.faragostaresh.adaptor.MainIconAdapter;
import com.faragostaresh.adaptor.MyGridView;
import com.faragostaresh.model.ItemList;
import com.faragostaresh.model.HorizontalSectionModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.messaging.FirebaseMessaging;

import com.faragostaresh.app.Config;
import com.faragostaresh.extra.NotificationUtils;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    TextView searchBox;
    ArrayList<HorizontalSectionModel> allSampleData;
    public List<ItemList> cafeList = new ArrayList<ItemList>();

    /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //case R.id.navigation_home:
                //    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                //    startActivity(intent1);
                //    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                //    break;
                case R.id.navigation_cafe:
                    Intent intent2 = new Intent(getApplicationContext(), CafeListActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break;
                case R.id.navigation_video:
                    Intent intent5 = new Intent(getApplicationContext(), VideoListActivity.class);
                    startActivity(intent5);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break;
                case R.id.navigation_event:
                    Intent intent3 = new Intent(getApplicationContext(), EventListActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break;
                case R.id.navigation_news:
                    Intent intent4 = new Intent(getApplicationContext(), NewsListActivity.class);
                    startActivity(intent4);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break;
            }
            return false;
        }
    }; */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Firebase Messaging
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // Firebase Messaging topics
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.FB_TOPIC_GLOBAL);
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.FB_TOPIC_CAFE);
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.FB_TOPIC_VIDEO);
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.FB_TOPIC_EVENT);
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.FB_TOPIC_NEWS);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };

        displayFirebaseRegId();

        // Set bottom navigation
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /* MenuItem item = navigation.getMenu().findItem(R.id.navigation_home);
        item.setCheckable(true);
        item.setChecked(true); */

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, getString(R.string.title_home)))
                .addItem(new BottomNavigationItem(R.drawable.ic_home_local_cafe, getString(R.string.title_cafe)))
                .addItem(new BottomNavigationItem(R.drawable.ic_videocam, getString(R.string.title_video)))
                .addItem(new BottomNavigationItem(R.drawable.ic_event_available, getString(R.string.title_event)))
                .addItem(new BottomNavigationItem(R.drawable.ic_content_paste, getString(R.string.title_news)))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.colorAccent)
                .setInActiveColor(R.color.colorGarry500)
                .setBarBackgroundColor(R.color.colorGarry300)
                /*
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.iron)
                .setInActiveColor(R.color.white)
                .setBarBackgroundColor(R.color.colorPrimary)
                 */
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                        break;
                    case 1:
                        Intent intent2 = new Intent(getApplicationContext(), CafeListActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                        break;
                    case 2:
                        Intent intent5 = new Intent(getApplicationContext(), VideoListActivity.class);
                        startActivity(intent5);
                        overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), EventListActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getApplicationContext(), NewsListActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });

        // Set floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getApplicationContext(), MapListActivity.class);
                startActivity(intent);
            }
        });

        // Set main image
        try {
            ImageView imageView = (ImageView) findViewById(R.id.backdrop);
            Glide.with(this).load(Config.URL_IMAGE_MAIN).skipMemoryCache(true).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Set list of main icons
        MyGridView gridView = (MyGridView)findViewById(R.id.gridview);
        gridView.setAdapter(new MainIconAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 1) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchRecommended", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 2) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchDiscount", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 3) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchTruck", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 4) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchNoSmoking", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 5) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchWifi", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 6) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchThirdWaveCoffee", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 7) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchBreakfast", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 8) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchFood", 1);
                    startActivityForResult(myIntent, 0);
                }

            }
        });

        // Set click on search box
        searchBox = (TextView)findViewById(R.id.search);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CafeListActivity.class);
                intent.putExtra("openSearchBox", 1);
                startActivity(intent);
            }
        });

        /* searchBox = (EditText) findViewById(R.id.search);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(getApplicationContext(), CafeListActivity.class);
                    intent.putExtra("openSearchBox", 1);
                    intent.putExtra("searchTitle", searchBox.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        }); */

        //
        /* String urlCafe = Config.URL_CAFE_RECOMMENDED;
        Log.d(TAG, urlCafe);
        // Volley's json array request object
        JsonObjectRequest reqCafe = new JsonObjectRequest(urlCafe,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            JSONArray jsonArray = response.optJSONArray("items");
                            if (response.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        Log.d(TAG, obj.toString());
                                        ItemList cafe = new ItemList();
                                        cafe.setTitle(obj.getString("title"));
                                        cafe.setItemID(obj.getString("id"));
                                        cafe.setThumbnailUrl(obj.getString("mediumUrl"));
                                        cafeList.add(cafe);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }


                        Log.d(TAG, "Size : " + String.valueOf(cafeList.size()));

                        allSampleData = new ArrayList<HorizontalSectionModel>();
                        createDummyDataCafe();
                        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.cafe_list);
                        my_recycler_view.setHasFixedSize(true);
                        HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter(getApplicationContext(), allSampleData);
                        my_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        my_recycler_view.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error.getMessage().isEmpty()) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        CafeyabApplication.getInstance().addToRequestQueue(reqCafe); */

        // Check user
        new checkLogin().execute();

        // Set User bar
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String userCheck = settings.getString("user_check", "").toString();
        String userSessionId = settings.getString("user_sessionid", "").toString();
        String userUid = settings.getString("user_uid", "").toString();
        String userIdentity = settings.getString("user_identity", "").toString();
        String userEmail = settings.getString("user_email", "").toString();
        String userName = settings.getString("user_name", "").toString();
        String userAvatar = settings.getString("user_avatar", "").toString();

        CircleImageView imageView = (CircleImageView) findViewById(R.id.user_avatar);
        TextView userTitleView = (TextView) findViewById(R.id.user_title);
        //TextView userEmailView = (TextView) findViewById(R.id.user_email);
        if (String.valueOf(userCheck).equals("1")) {
            userTitleView.setText("خوش آمدید " + userName);
            //userEmailView.setText(userEmail);
            try {
                userAvatar = userAvatar.replace("s=80", "s=400");
                Glide.with(this).load(userAvatar).skipMemoryCache(true).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RelativeLayout userBar = (RelativeLayout) findViewById(R.id.user_bar);
        userBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
        });
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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

    /* public void createDummyDataCafe() {
        HorizontalSectionModel dm = new HorizontalSectionModel();
        dm.setHeaderTitle("کافه های پیشنهادی");
        ArrayList<HorizontalSingleItemModel> singleItem = new ArrayList<HorizontalSingleItemModel>();
        for (int j = 0; j < cafeList.size(); j++) {
            ItemList cafe = cafeList.get(j);
            singleItem.add(new HorizontalSingleItemModel(cafe.getTitle(), cafe.getThumbnailUrl(), "cafe", cafe.getItemId()));
        }
        dm.setAllItemsInSection(singleItem);
        allSampleData.add(dm);
    } */

    protected class checkLogin extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {

            String str = Config.URL_PROFILE;
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

                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                Log.e(TAG, stringBuffer.toString());

                return new JSONObject(stringBuffer.toString());
            } catch (Exception ex) {
                Log.e(TAG, "yourDataTask", ex);
                return null;
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

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    String check = response.getString("check");
                    String sessionid = response.getString("sessionid");
                    String uid = response.getString("uid");
                    String identity = response.getString("identity");
                    String email = response.getString("email");
                    String name = response.getString("name");
                    String avatar = response.getString("avatar");

                    // User info
                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("user_check", check);
                    editor.putString("user_sessionid", sessionid);
                    editor.putString("user_uid", uid);
                    editor.putString("user_identity", identity);
                    editor.putString("user_email", email);
                    editor.putString("user_name", name);
                    editor.putString("user_avatar", avatar);

                    if (check.equals(1)) {
                        String first_name = response.getString("first_name");
                        String last_name = response.getString("last_name");
                        String id_number = response.getString("id_number");
                        String phone = response.getString("phone");
                        String mobile = response.getString("mobile");
                        String address1 = response.getString("address1");
                        String zip_code = response.getString("zip_code");
                        String company = response.getString("company");

                        editor.putString("user_first_name", first_name);
                        editor.putString("user_last_name", last_name);
                        editor.putString("user_id_number", id_number);
                        editor.putString("user_phone", phone);
                        editor.putString("user_mobile", mobile);
                        editor.putString("user_address1", address1);
                        editor.putString("user_zip_code", zip_code);
                        editor.putString("user_company", company);
                    }

                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failure", e);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}