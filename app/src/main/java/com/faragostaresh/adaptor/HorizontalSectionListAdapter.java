package com.faragostaresh.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.faragostaresh.cafeyab.CafeSingleActivity;
import com.faragostaresh.model.HorizontalSingleItemModel;
import com.faragostaresh.cafeyab.R;

import java.util.ArrayList;

public class HorizontalSectionListAdapter extends RecyclerView.Adapter<HorizontalSectionListAdapter.SingleItemRowHolder> {

    private ArrayList<HorizontalSingleItemModel> itemsList;
    private Context mContext;

    public HorizontalSectionListAdapter(Context context, ArrayList<HorizontalSingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_horizontal_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        HorizontalSingleItemModel singleItem = itemsList.get(i);

        holder.itemTitle.setText(singleItem.getItemTitle());

        holder.itemType.setText(singleItem.getItemType());

        holder.itemId.setText(singleItem.getItemId());

        Glide.with(mContext)
                .load(singleItem.getItemImage())
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.centerCrop()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected TextView itemType;

        protected TextView itemId;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.itemType = (TextView) view.findViewById(R.id.itemType);
            this.itemId = (TextView) view.findViewById(R.id.itemId);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (String.valueOf(itemType.getText()).equals("cafe")) {
                        Intent intent = new Intent(v.getContext(), CafeSingleActivity.class);
                        intent.putExtra("itemId", itemId.getText());
                        intent.putExtra("itemTitle", itemTitle.getText());
                        v.getContext().startActivity(intent);
                    } else if (String.valueOf(itemType.getText()).equals("video")) {
                    } else if (String.valueOf(itemType.getText()).equals("event")) {
                    } else if (String.valueOf(itemType.getText()).equals("news")) {
                    }
                }
            });

        }

    }

}