package com.faragostaresh.cafeyab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_cafe:
                    Intent intent2 = new Intent(getApplicationContext(), CafeListActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.navigation_event:
                    Intent intent3 = new Intent(getApplicationContext(), EventListActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.navigation_news:
                    Intent intent4 = new Intent(getApplicationContext(), NewsListActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.navigation_video:
                    Intent intent5 = new Intent(getApplicationContext(), VideoListActivity.class);
                    startActivity(intent5);
                    break;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Set bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MenuItem item = navigation.getMenu().findItem(R.id.navigation_home);
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

        // Set main image
        try {
            ImageView imageView = (ImageView) findViewById(R.id.backdrop);
            Glide.with(this).load("https://www.cafeyab.com/upload/app/android/index-cover.jpg").into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
