package org.cs160.bactracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avifrankl on 8/7/15.
 */
public class CategoryAdapter extends ArrayAdapter{
    private Context context;
    private ArrayList<CategoryItem> items;
    private LayoutInflater vi;
    public CategoryAdapter(Context context,ArrayList<CategoryItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final CategoryItem i = items.get(position);
        if (i != null) {
            if (i.isCategory()) {
                CategoryItem ci = (CategoryItem) i;
                v = vi.inflate(R.layout.category_item, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView nameView = (TextView) v.findViewById(R.id.categoryName);
                nameView.setText(ci.name);

                final TextView countView = (TextView) v.findViewById(R.id.numberOfDrinks);
                countView.setText(Integer.toString(ci.drinks.size()));

                final ImageView imageView = (ImageView) v.findViewById(R.id.categoryImage);
                Bitmap image = ci.image;
                imageView.setImageBitmap(image);
            }
        }
        return v;
    }
}
