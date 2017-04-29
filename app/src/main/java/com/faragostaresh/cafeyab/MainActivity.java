package com.faragostaresh.cafeyab;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.faragostaresh.adaptor.MainIconAdapter;
import com.faragostaresh.adaptor.MyGridView;
import com.faragostaresh.adaptor.HorizontalRecyclerViewAdapter;
import com.faragostaresh.app.CafeyabApplication;
import com.faragostaresh.model.ItemList;
import com.faragostaresh.model.HorizontalSectionModel;
import com.faragostaresh.model.HorizontalSingleItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView searchBox;
    ArrayList<HorizontalSectionModel> allSampleData;
    public List<ItemList> cafeList = new ArrayList<ItemList>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break;
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Set hide actionBar
        //getSupportActionBar().hide();

        // Set bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem item = navigation.getMenu().findItem(R.id.navigation_home);
        item.setCheckable(true);
        item.setChecked(true);

        // Set floating button
        if (Build.VERSION.SDK_INT > 21) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Click action
                    Intent intent = new Intent(getApplicationContext(), MapListActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Set main image
        if (Build.VERSION.SDK_INT > 21) {
            try {
                ImageView imageView = (ImageView) findViewById(R.id.backdrop);
                Glide.with(this).load("https://www.cafeyab.com/upload/app/android/index-cover.jpg").skipMemoryCache(true).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    myIntent.putExtra("searchTruck", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 3) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchNoSmoking", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 4) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchWifi", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 5) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchThirdWaveCoffee", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 6) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchBreakfast", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 7) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchFood", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 8) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchVegetarianFood", 1);
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

        //
        String urlCafe = "https://www.cafeyab.com/guide/json/search?limit=15&page=1&recommended=1";
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
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        CafeyabApplication.getInstance().addToRequestQueue(reqCafe);

        // Check user
        new checkLogin().execute();
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

    public void createDummyDataCafe() {
        HorizontalSectionModel dm = new HorizontalSectionModel();
        dm.setHeaderTitle("کافه های پیشنهادی");
        ArrayList<HorizontalSingleItemModel> singleItem = new ArrayList<HorizontalSingleItemModel>();
        for (int j = 0; j < cafeList.size(); j++) {
            ItemList cafe = cafeList.get(j);
            singleItem.add(new HorizontalSingleItemModel(cafe.getTitle(), cafe.getThumbnailUrl(), "cafe", cafe.getItemId()));
        }
        dm.setAllItemsInSection(singleItem);
        allSampleData.add(dm);
    }

    protected class checkLogin extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {

            String str = "https://www.cafeyab.com/usmartphone/check";
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
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failure", e);
                }
            }
        }
    }
}