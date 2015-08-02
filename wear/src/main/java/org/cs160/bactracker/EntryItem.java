package org.cs160.bactracker;

import android.graphics.Bitmap;

/**
 * Created by avifrankl on 8/1/15.
 */
public class EntryItem implements Item{

    public final String title;
    public int drinkCount;
    public float alc;
    public Bitmap drinkImage;


    public EntryItem(String title, int drinkCount, float alc) {
        this.title = title;
        this.drinkCount = drinkCount;
        this.alc = alc;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}
