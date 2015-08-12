package org.cs160.bactracker;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by avifrankl on 8/7/15.
 */
public class CategoryItem implements Item{
    public Bitmap image, originalImage;
    public String name;
    public ArrayList<DrinkItem> drinks;


    public CategoryItem() {
        this.drinks = new ArrayList<DrinkItem>();
    }
    public CategoryItem(String name) {
        this.name = name;
        this.drinks = new ArrayList<DrinkItem>();
    }

    public CategoryItem(String name, Bitmap obtainedImage, int resizeWidth, int resizeHeight) {
        this.drinks = new ArrayList<DrinkItem>();
        this.name = name;
        this.originalImage = obtainedImage;
        this.image = Bitmap.createScaledBitmap(this.originalImage, resizeWidth, resizeHeight, true);
    }

    public boolean isCategory() {
        return true;
    }
}
