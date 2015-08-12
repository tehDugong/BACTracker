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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Icons for the list
        mIcons = new ArrayList<Integer>();
        mIcons.add(R.drawable.beer);
        mIcons.add(R.drawable.cocktail);
        mIcons.add(R.drawable.liquor);
        mIcons.add(R.drawable.wine);

        // names for the list
        mNames = new ArrayList<String>();
        mNames.add("Beer");
        mNames.add("Cocktail");
        mNames.add("Liquor");
        mNames.add("Wine");

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
                    String drink = mNames.get(viewHolder.getLayoutPosition());
                    Intent i = new Intent(getApplicationContext(), DrinkMenuWearableList.class);
                    i.putExtra("name", drink);
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
}