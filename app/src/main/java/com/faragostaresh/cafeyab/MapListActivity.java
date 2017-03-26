package com.faragostaresh.cafeyab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MapListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

    }
}