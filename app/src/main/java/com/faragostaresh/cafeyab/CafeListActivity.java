package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.faragostaresh.adaptor.CafeListAdapter;
import com.faragostaresh.app.CafeyabApplication;
import com.faragostaresh.app.Config;
import com.faragostaresh.model.ItemList;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class CafeListActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = CafeListActivity.class.getSimpleName();

    public static String itemId = "";
    public static String itemTitle = "";
    public int page = 1;
    public String searchLocation = "";
    public String searchTitle = "";
    public int searchRecommended = 0;
    public int searchNoSmoking = 0;
    public int searchBreakfast = 0;
    public int searchFood = 0;
    public int searchVegetarianFood = 0;
    public int searchWifi = 0;
    public int searchTruck = 0;
    public int searchThirdWaveCoffee = 0;
    /* public int searchTakeaWay = 0;
    public int searchGallery = 0;
    public int searchTv = 0;
    public int searchFamily = 0; */
    public int openSearchBox = 0;
    private List<ItemList> myCafeList = new ArrayList<ItemList>();
    private GridView gridView;
    private CafeListAdapter adapter;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private SlidingUpPanelLayout mLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                /* case R.id.navigation_home:
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break; */
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
        setContentView(R.layout.activity_cafe_list);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem item = navigation.getMenu().findItem(R.id.navigation_cafe);
        item.setCheckable(true);
        item.setChecked(true);

        // Set floating button
        /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getApplicationContext(), MapListActivity.class);
                startActivity(intent);
            }
        }); */

        // Set map button
        /* Button map = (Button) findViewById(R.id.search_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getApplicationContext(), MapListActivity.class);
                startActivity(intent);
            }
        }); */

        // Get location list
        Spinner spinner = (Spinner) findViewById(R.id.filter_city);
        ArrayAdapter<CharSequence> filterCityAdapter = ArrayAdapter.createFromResource(this, R.array.city_list_name, android.R.layout.simple_spinner_item);
        filterCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filterCityAdapter);

        // Set search information
        final EditText filterTitle = (EditText) findViewById(R.id.filter_name);
        final Spinner filterCity = (Spinner) findViewById(R.id.filter_city);
        final CheckBox filterRecommended = (CheckBox) findViewById(R.id.filter_recommended);
        final CheckBox filterNoSmoking = (CheckBox) findViewById(R.id.filter_no_smoking);
        final CheckBox filterBreakfast = (CheckBox) findViewById(R.id.filter_breakfast);
        final CheckBox filterFood = (CheckBox) findViewById(R.id.filter_food);
        final CheckBox filterVegetarianFood = (CheckBox) findViewById(R.id.filter_vegetarian_food);
        final CheckBox filterWifi = (CheckBox) findViewById(R.id.filter_wifi);
        /*
        final CheckBox filterTruck = (CheckBox) findViewById(R.id.filter_truck);
        final CheckBox filterThirdWaveCoffee = (CheckBox) findViewById(R.id.filter_third_wave_coffee);
        final CheckBox filterTakeaWay = (CheckBox) findViewById(R.id.filter_takea_way);
        final CheckBox filterGallery = (CheckBox) findViewById(R.id.filter_gallery);
        final CheckBox filterTv = (CheckBox) findViewById(R.id.filter_tv);
        final CheckBox filterFamily = (CheckBox) findViewById(R.id.filter_family);
        */
        final Button search = (Button) findViewById(R.id.filter_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CafeListActivity.class);
                int position = filterCity.getSelectedItemPosition();
                String[] CITYSLUG = getResources().getStringArray(R.array.city_list_slug);
                searchLocation = CITYSLUG[position];
                if (!TextUtils.isEmpty(searchLocation)) {
                    intent.putExtra("searchLocation", searchLocation);
                }
                searchTitle = filterTitle.getText().toString();
                if (!TextUtils.isEmpty(searchTitle)) {
                    intent.putExtra("searchTitle", searchTitle);
                }
                if (filterRecommended.isChecked()) {
                    intent.putExtra("searchRecommended", 1);
                }
                if (filterNoSmoking.isChecked()) {
                    intent.putExtra("searchNoSmoking", 1);
                }
                if (filterBreakfast.isChecked()) {
                    intent.putExtra("searchBreakfast", 1);
                }
                if (filterFood.isChecked()) {
                    intent.putExtra("searchFood", 1);
                }
                if (filterVegetarianFood.isChecked()) {
                    intent.putExtra("searchVegetarianFood", 1);
                }
                if (filterWifi.isChecked()) {
                    intent.putExtra("searchWifi", 1);
                }
                /*
                if (filterThirdWaveCoffee.isChecked()) {
                    intent.putExtra("searchThirdWaveCoffee", 1);
                }
                if (filterTruck.isChecked()) {
                    intent.putExtra("searchTruck", 1);
                }
                if (filterTakeaWay.isChecked()) {
                    intent.putExtra("searchTakeaWay", 1);
                }
                if (filterGallery.isChecked()) {
                    intent.putExtra("searchGallery", 1);
                }
                if (filterTv.isChecked()) {
                    intent.putExtra("searchTv", 1);
                }
                if (filterFamily.isChecked()) {
                    intent.putExtra("searchFamily", 1);
                }
                */
                startActivity(intent);
                overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
            }
        });

        // Get search information
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchLocation = extras.getString("searchLocation");
            searchTitle = extras.getString("searchTitle");
            searchRecommended = extras.getInt("searchRecommended");
            searchNoSmoking = extras.getInt("searchNoSmoking");
            searchBreakfast = extras.getInt("searchBreakfast");
            searchFood = extras.getInt("searchFood");
            searchVegetarianFood = extras.getInt("searchVegetarianFood");
            searchWifi = extras.getInt("searchWifi");
            searchTruck = extras.getInt("searchTruck");
            searchThirdWaveCoffee = extras.getInt("searchThirdWaveCoffee");
            /* searchTakeaWay = extras.getInt("searchTakeaWay");
            searchGallery = extras.getInt("searchGallery");
            searchTv = extras.getInt("searchTv");
            searchFamily = extras.getInt("searchFamily"); */
            openSearchBox = extras.getInt("openSearchBox");

            if (!TextUtils.isEmpty(searchLocation)) {
                String[] CitySlug = getResources().getStringArray(R.array.city_list_slug);
                String[] CityName = getResources().getStringArray(R.array.city_list_name);
                int postion = Arrays.asList(CitySlug).indexOf(searchLocation);
                filterCity.setSelection(postion);
            }
            if (!TextUtils.isEmpty(searchTitle)) {
                filterTitle.setText(searchTitle);
            }
            if (searchRecommended == 1) {
                filterRecommended.setChecked(true);
            }
            if (searchNoSmoking == 1) {
                filterNoSmoking.setChecked(true);
            }
            if (searchBreakfast == 1) {
                filterBreakfast.setChecked(true);
            }
            if (searchFood == 1) {
                filterFood.setChecked(true);
            }
            if (searchVegetarianFood == 1) {
                filterVegetarianFood.setChecked(true);
            }
            if (searchWifi == 1) {
                filterWifi.setChecked(true);
            }
            /*
            if (searchThirdWaveCoffee == 1) {
                filterThirdWaveCoffee.setChecked(true);
            }
            if (searchTruck == 1) {
                filterTruck.setChecked(true);
            }
            if (searchTakeaWay == 1) {
                filterTakeaWay.setChecked(true);
            }
            if (searchGallery == 1) {
                filterGallery.setChecked(true);
            }
            if (searchTv == 1) {
                filterTv.setChecked(true);
            }
            if (searchFamily == 1) {
                filterFamily.setChecked(true);
            }
            */
        }

        // Sliding Up Panel actions
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (openSearchBox == 1) {
            mLayout.setPanelState(EXPANDED);
        }

        // Set for list of items
        gridView = (GridView) findViewById(R.id.cafelist);
        adapter = new CafeListAdapter(this, myCafeList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new CafeListActivity.ListVewiClickListener());
        gridView.setOnScrollListener(onScrollListener());
        myCafeList.clear();
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipyRefreshLayout.setRefreshing(true);
                                         fetchList();
                                     }
                                 }
        );
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

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int first = view.getFirstVisiblePosition();
                int count = view.getChildCount();
                if (scrollState == SCROLL_STATE_IDLE || (first + count > adapter.getCount())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipyRefreshLayout.setRefreshing(true);
                            fetchList();
                        }
                    }, 1500);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        };
    }

    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        fetchList();
    }


    private void fetchList() {
        // showing refresh animation before making http call
        mSwipyRefreshLayout.setRefreshing(true);


        // appending offset to url
        String url = Config.URL_CAFE_LIST + page;

        // Make search to url
        if (!TextUtils.isEmpty(searchTitle)) {
            url = url + "&title=" + Uri.encode(searchTitle);
        }
        if (!TextUtils.isEmpty(searchLocation)) {
            url = url + "&location=" + searchLocation;
        }
        if (searchRecommended == 1) {
            url = url + "&recommended=1";
        }
        if (searchNoSmoking == 1) {
            url = url + "&nosmoke=%D8%A2%D8%B2%D8%A7%D8%AF%20%D9%86%DB%8C%D8%B3%D8%AA";
        }
        if (searchBreakfast == 1) {
            url = url + "&breakfast=دارد";
        }
        if (searchFood == 1) {
            url = url + "&food=دارد";
        }
        if (searchVegetarianFood == 1) {
            url = url + "&vegetarian=دارد";
        }
        if (searchWifi == 1) {
            url = url + "&wifi=دارد";
        }
        if (searchTruck == 1) {
            url = url + "&truck=سیار";
        }
        if (searchThirdWaveCoffee == 1) {
            url = url + "&thirdwavecoffee=دارد";
        }
        /* if (searchTakeaWay == 1) {
            url = url + "&takeaway=دارد";
        }
        if (searchGallery == 1) {
            url = url + "&gallery=دارد";
        }
        if (searchTv == 1) {
            url = url + "&tv=دارد";
        }
        if (searchFamily == 1) {
            url = url + "&suitablefamily=بله";
        } */

        Log.d(TAG, url);

        // Volley's json array request object
        JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            JSONArray jsonArray = response.optJSONArray("items");
                            if (response.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        ItemList cafe = new ItemList();
                                        cafe.setTitle(obj.getString("title"));
                                        cafe.setItemID(obj.getString("id"));
                                        cafe.setThumbnailUrl(obj.getString("mediumUrl"));
                                        cafe.setCityArea(obj.getString("city_area"));
                                        myCafeList.add(cafe);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // Update page
                                page++;
                                adapter.notifyDataSetChanged();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipyRefreshLayout.setRefreshing(false);
                                    }
                                }, 2500);
                            }
                        }

                        // stopping swipe refresh
                        mSwipyRefreshLayout.setRefreshing(false);
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
                // stopping swipe refresh
                mSwipyRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        CafeyabApplication.getInstance().addToRequestQueue(req);
    }

    private class ListVewiClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            itemId = null;
            itemTitle = null;
            ItemList getselected = (ItemList) (gridView.getItemAtPosition(position));
            itemId = getselected.getItemId();
            itemTitle = getselected.getTitle();

            Log.d(TAG, "List item id : " + itemId);
            Log.d(TAG, "List item title : " + itemTitle);

            displayView(itemId, itemTitle);
        }

        private void displayView(String itemId, String itemTitle) {
            // Start New activity with a request to Show Selected News
            Intent intent = new Intent(getApplicationContext(), CafeSingleActivity.class);
            intent.putExtra("itemId", itemId);
            intent.putExtra("itemTitle", itemTitle);
            startActivity(intent);
        }
    }
}