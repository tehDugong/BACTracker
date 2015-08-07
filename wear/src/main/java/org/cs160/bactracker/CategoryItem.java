package org.cs160.bactracker;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by avifrankl on 8/7/15.
 */
public class CategoryItem implements Item{
    public Bitmap image;
    public String name;
    public ArrayList<DrinkItem> drinks;

    public boolean isCategory() {
        return true;
    }
}
