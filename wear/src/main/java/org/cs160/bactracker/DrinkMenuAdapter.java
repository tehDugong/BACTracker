package org.cs160.bactracker;

/**
 * Created by vincent on 8/11/15.
 */
import android.content.Context;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrinkMenuAdapter extends WearableListView.Adapter {
    private ArrayList<String> mNames;
    private final LayoutInflater mInflater;

    public DrinkMenuAdapter(Context context, ArrayList<String> names) {
        mInflater = LayoutInflater.from(context);
        mNames = names;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(mInflater.inflate(R.layout.menu_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        TextView textView = itemViewHolder.mItemTextView;
        textView.setText(String.format(mNames.get(position)));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    private static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView mItemTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItemTextView = (TextView) itemView.findViewById(R.id.name);
        }
    }
}