package org.cs160.bactracker;

/**
 * Created by vincent on 8/11/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DrinkMenuWearableList extends Activity {
    private static ArrayList<String> mNames;
    private TextView mHeader;
    public ArrayList<DrinkItem> drinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        String header = "Unknown";
        int position = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            header = extras.getString("name");
            position = extras.getInt("position");
        }

        mNames = new ArrayList<String>();
        drinks = MenuWearableList.categoryItems.get(position).drinks;
        for (DrinkItem d : drinks){
            mNames.add(d.getName());
        }

        // This is our list header
        mHeader = (TextView) findViewById(R.id.header_drink_menu);
        WearableListView wearableListView =
                (WearableListView) findViewById(R.id.wearable_List);
        wearableListView.setAdapter(new DrinkMenuAdapter(this, mNames));
        wearableListView.setClickListener(mClickListener);
        wearableListView.addOnScrollListener(mOnScrollListener);
        mHeader.setText(header);
    }

    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    DrinkItem item = drinks.get(viewHolder.getLayoutPosition());
                    Intent i = new Intent(getApplicationContext(), CountDrinks.class);
                    i.putExtra("drink", item);
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