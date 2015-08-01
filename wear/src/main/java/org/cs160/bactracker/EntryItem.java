package org.cs160.bactracker;

/**
 * Created by avifrankl on 8/1/15.
 */
public class EntryItem implements Item{

    public final String title;
    public final String subtitle;

    public EntryItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}
