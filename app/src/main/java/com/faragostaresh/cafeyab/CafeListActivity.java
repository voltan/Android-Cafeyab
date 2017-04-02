package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.faragostaresh.adaptor.CafeListAdapter;
import com.faragostaresh.app.CafeyabApplication;
import com.faragostaresh.model.ItemList;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CafeListActivity extends AppCompatActivity {

    private static final String cafeUrl = "https://www.cafeyab.com/guide/json/search?limit=10&page=";
    public static String itemId;
    public int page = 1;
    public String location = "";
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
                /* case R.id.navigation_news:
                    Intent intent4 = new Intent(getApplicationContext(), NewsListActivity.class);
                    startActivity(intent4);
                    overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
                    break; */
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

        // Set bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem item = navigation.getMenu().findItem(R.id.navigation_cafe);
        item.setCheckable(true);
        item.setChecked(true);

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

        // Get search information
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location = extras.getString("location");
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

        // Sliding Up Panel actions
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        // Get location list
        Spinner spinner = (Spinner) findViewById(R.id.filter_city);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_list_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        /* String[] CITYTITLE = getResources().getStringArray(R.array.city_list_name);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, CITYTITLE);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)findViewById(R.id.city_list);
        materialDesignSpinner.setAdapter(arrayAdapter);
        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String[] CITYSLUG = getResources().getStringArray(R.array.city_list_slug);
                String cityTitle = adapterView.getItemAtPosition(position).toString();
                String citySlug = CITYSLUG[position];
                setTitle(cityTitle + " - " + citySlug);

                Intent intent = new Intent(getApplicationContext(), CafeListActivity.class);
                intent.putExtra("location", citySlug);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
            }
        }); */
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
        String url = cafeUrl + page;
        if (!TextUtils.isEmpty(location)) {
            url = url + "&location=" + location;
        }

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
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
            ItemList getselected = (ItemList) (gridView.getItemAtPosition(position));
            itemId = getselected.getItemId();
            displayView(itemId);
        }

        private void displayView(String itemId) {
            // Start New activity with a request to Show Selected News
            Intent intent = new Intent(getApplicationContext(), CafeSingleActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);

        }
    }
}