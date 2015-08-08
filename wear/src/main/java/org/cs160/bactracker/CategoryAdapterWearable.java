package org.cs160.bactracker;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avifrankl on 8/7/15.
 */
public class CategoryAdapterWearable extends WearableListView.Adapter{
    public ArrayList<CategoryItem> items;
    private final LayoutInflater inflater;
    public CategoryAdapterWearable(Context context,ArrayList<CategoryItem> items) {
        this.items=items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(inflater.inflate(R.layout.category_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        CategoryItem item = (CategoryItem) items.get(position);

        TextView title = (TextView) holder.itemView.findViewById(R.id.categoryName);
        title.setText(item.name);

        TextView numberOfDrinks = (TextView)holder.itemView.findViewById(R.id.numberOfDrinks);
        numberOfDrinks.setText(Integer.toString(items.size()));

        ImageView image = (ImageView)holder.itemView.findViewById(R.id.categoryImage);
        image.setImageBitmap(item.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
