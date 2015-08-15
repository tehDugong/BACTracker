package org.cs160.bactracker;

/**
 * Created by vincent on 8/11/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuWearableList extends Activity {
    private static ArrayList<Integer> mIcons;
    private static ArrayList<String> mNames;
    private TextView mHeader;
    private final String TAG = "MenuList";

    public static ArrayList<CategoryItem> categoryItems;
    private final int WIDTH_RESIZE = 50;
    private final int HEIGHT_RESIZE = 50;
    private final int dWIDTH_RESIZE = 20;
    private final int dHEIGHT_RESIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Icons for the list
        mIcons = new ArrayList<Integer>();
        mIcons.add(R.drawable.beer);
        mIcons.add(R.drawable.wine);
        mIcons.add(R.drawable.liquor);

        mIcons.add(R.drawable.cocktail);

        // names for the list
        mNames = new ArrayList<String>();
        mNames.add("Beer");
        mNames.add("Wine");
        mNames.add("Liquor");
        mNames.add("Cocktail");

        // This is our list header
        mHeader = (TextView) findViewById(R.id.header_menu);
        WearableListView wearableListView =
                (WearableListView) findViewById(R.id.wearable_List);
        wearableListView.setAdapter(new MenuAdapter(this, mIcons, mNames));
        wearableListView.setClickListener(mClickListener);
        wearableListView.addOnScrollListener(mOnScrollListener);
    }


    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    int currentPosition = viewHolder.getAdapterPosition();
                    String category = mNames.get(currentPosition);
                    Intent i = new Intent(MenuWearableList.this, DrinkMenuWearableList.class);
                    i.putExtra("category", category);
                    startActivity(i);
                }
                @Override
                public void onTopEmptyRegionClick() {
                    // do nothing
                }
            };

    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list
    private WearableListView.OnScrollListener mOnScrollListener =
            new WearableListView.OnScrollListener() {
                @Override
                public void onAbsoluteScrollChange(int i) {
                    // Only scroll the title up from its original base position
                    // and not down.
                    if (i > 0) {
                        mHeader.setY(-i);
                    }
                }
                @Override
                public void onScroll(int i) {
                    // Placeholder
                }
                @Override
                public void onScrollStateChanged(int i) {
                    // Placeholder
                }
                @Override
                public void onCentralPositionChanged(int i) {
                    // Placeholder
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}