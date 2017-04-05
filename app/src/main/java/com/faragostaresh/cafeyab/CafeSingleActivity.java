package com.faragostaresh.cafeyab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.faragostaresh.app.CafeyabApplication;
import com.faragostaresh.model.CafeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CafeSingleActivity extends AppCompatActivity {

    ImageLoader imageLoader = CafeyabApplication.getInstance().getImageLoader();
    public static String cafeUrl = "https://www.cafeyab.com/guide/json/itemSingle/id/";
    public static String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_single);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Get search information
        Bundle extras = getIntent().getExtras();
        itemId = extras.getString("itemId");
        cafeUrl = cafeUrl + itemId;

        TextView viewTitle = (TextView) findViewById(R.id.viewTitle);
        TextView viewCityArea = (TextView) findViewById(R.id.viewCityArea);
        NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.photo);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(cafeUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        CafeList cafeSingle = new CafeList();
                        JSONObject json = null;
                        try {
                            json = response.getJSONObject(0);

                            // Set title
                            setTitle(json.getString("title"));

                            // Set info for layout

                            thumbnail.setImageUrl(json.getString("itemimageUrl"), imageLoader);
                            viewTitle.setText(json.getString("title"));
                            viewCityArea.setText(json.getString("city_area"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
}