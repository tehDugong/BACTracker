package org.cs160.bactracker;

import android.graphics.Bitmap;

/**
 * Created by avifrankl on 8/7/15.
 */
public class DrinkItem implements Item{
    public Bitmap image;
    public String name;
    public float alcoholContent;
    public int size;
    public String units;

    public boolean isCategory() {
        return false;
    }
}
