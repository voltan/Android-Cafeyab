package com.faragostaresh.cafeyab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.faragostaresh.adaptor.MainIconAdapter;

public class MainActivity extends AppCompatActivity {

    TextView searchBox;

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
        //MenuItem item = navigation.getMenu().findItem(R.id.navigation_home);
        //item.setCheckable(true);
        //item.setChecked(true);

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
            Glide.with(this).load("https://www.cafeyab.com/upload/app/android/index-cover.jpg").into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set list of main icons
        GridView gridView = (GridView)findViewById(R.id.gridview);
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
                    myIntent.putExtra("searchNoSmoking", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 3) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchBreakfast", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 4) {
                    Intent myIntent = new Intent(view.getContext(), CafeListActivity.class);
                    myIntent.putExtra("searchFood", 1);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 5) {
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
}