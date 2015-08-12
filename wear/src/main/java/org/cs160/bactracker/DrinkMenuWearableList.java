package org.cs160.bactracker;

/**
 * Created by vincent on 8/11/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DrinkMenuWearableList extends Activity {
    private static ArrayList<String> mNames;
    private TextView mHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        String header = "Unknown";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            header = extras.getString("name");
        }

        // names for the list, currently populated with false data...
        mNames = new ArrayList<String>();
        mNames.add("Beer 1");
        mNames.add("Beer 2");
        mNames.add("Beer 3");
        mNames.add("Beer 4");

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
                    Toast.makeText(DrinkMenuWearableList.this,
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1),
                            Toast.LENGTH_SHORT).show();
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