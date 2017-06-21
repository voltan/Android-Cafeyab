package com.faragostaresh.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faragostaresh.cafeyab.R;

import java.util.ArrayList;
import java.util.List;

public final class MainIconAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public MainIconAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        mItems.add(new Item("کافه‌های جدید",             R.drawable.ic_home_location_on));
        mItems.add(new Item("کافه‌های پیشنهادی",         R.drawable.ic_home_assistant_photo));
        mItems.add(new Item("کافه‌های تخفیف‌‌‌ دار",        R.drawable.ic_money_off));

        mItems.add(new Item("کافه‌های سیار",             R.drawable.ic_home_airport_shuttle));
        mItems.add(new Item("کافه‌های بدون سیگار",       R.drawable.ic_home_smoke_free));
        mItems.add(new Item("کافه‌های با اینترنت",       R.drawable.ic_home_wifi));

        mItems.add(new Item("کافه‌های با قهوه موج سوم",  R.drawable.ic_home_local_cafe));
        mItems.add(new Item("کافه‌های با صبحانه",        R.drawable.ic_home_brightness_high));
        mItems.add(new Item("کافه‌های با غذا",           R.drawable.ic_home_restaurant));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item_main, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private static class Item {
        public final String name;
        public final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}