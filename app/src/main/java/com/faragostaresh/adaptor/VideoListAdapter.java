package com.faragostaresh.adaptor;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.faragostaresh.app.CafeyabApplication;

import com.faragostaresh.cafeyab.R;
import com.faragostaresh.model.ItemList;

import java.util.List;

public class VideoListAdapter extends BaseAdapter {

    ImageLoader imageLoader = CafeyabApplication.getInstance().getImageLoader();
    private final AppCompatActivity appCompatActivity;
    private LayoutInflater inflater;
    private List<ItemList> ItemList;
    private String type = "list";
    private int lastPosition = -1;

    public VideoListAdapter(AppCompatActivity appCompatActivity, List<ItemList> ItemsList, String type) {
        this.appCompatActivity = appCompatActivity;
        this.ItemList = ItemsList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int location) {
        return ItemList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) appCompatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            if (String.valueOf(type).equals("related")) {
                convertView = inflater.inflate(R.layout.list_row_video_related, null);
            } else {
                convertView = inflater.inflate(R.layout.list_row_video, null);
            }
        }

        if (imageLoader == null) {
            imageLoader = CafeyabApplication.getInstance().getImageLoader();
        }

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        //ImageView image = (ImageView) convertView.findViewById(R.id.imageView);

        // getting movie data for the row
        ItemList m = ItemList.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());
        //image.setImageBitmap(m.getImage());

        lastPosition = position;

        return convertView;
    }
}