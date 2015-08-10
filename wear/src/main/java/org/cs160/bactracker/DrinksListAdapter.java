package org.cs160.bactracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avifrankl on 8/10/15.
 */
public class DrinksListAdapter extends BaseAdapter {
    private ArrayList<DrinkItem> drinks;
    private Context context;

    DrinksListAdapter(Context context, ArrayList<DrinkItem> drinks) {
        super();
        this.context = context;
        this.drinks = drinks;
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = null;
        if (convertView == null) {
            gridView = new View(context);
            DrinkItem drink = drinks.get(position);
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.drinks_grid_cell, null);

            TextView title = (TextView) gridView.findViewById(R.id.drinkTitle);
            title.setText(drink.getName());

            RoundedImageView image = (RoundedImageView) gridView.findViewById(R.id.drinkGridImage);
            image.setImageBitmap(drink.getImage());

        } else {
            gridView = convertView;
        }
        return gridView;
    }
}
