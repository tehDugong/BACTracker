package org.cs160.bactracker;

/**
 * Created by vincent on 8/11/15.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class DrinkMenuWearableList extends Activity {
    private static ArrayList<String> mNames;
    private static ArrayList<Integer> mIcons;
    private TextView mHeader;
    private String category;
    public ArrayList<DrinkItem> drinks;
    private BroadcastReceiver retrieveMenu;
    private final String TAG = "DrinkMenuList";
    private int res;

    public void configure() {
        int position = 0;
        Bundle extras = getIntent().getExtras();
        category = extras.getString("category");
        drinks = getDrinks(category);

        mNames = new ArrayList<String>();
        mIcons = new ArrayList<Integer>();
        res = 0;
        if (category.equals("Beer")) {
            res = R.drawable.beer;
        } else if (category.equals("Wine")) {
            res = R.drawable.wine;
        } else if (category.equals("Liquor")) {
            res = R.drawable.liquor;
        } else if (category.equals("Cocktail")) {
            res = R.drawable.cocktail;
        }
        for (DrinkItem d : drinks) {
            mIcons.add(res);
            mNames.add(d.getName());
        }

            // This is our list header
            mHeader = (TextView) findViewById(R.id.header_drink_menu);
            WearableListView wearableListView =
                    (WearableListView) findViewById(R.id.wearable_List);
            wearableListView.setAdapter(new MenuAdapter(this, mIcons, mNames));
            wearableListView.setClickListener(mClickListener);
            wearableListView.addOnScrollListener(mOnScrollListener);
            mHeader.setText(category);
    }


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drink_menu);
            Intent i = new Intent(this, SignalForMenu.class);
            startService(i);
            retrieveMenu = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(TAG, "menuRetrieved");
                    configure();
                }
            };
            registerReceiver(retrieveMenu, new IntentFilter("menu"));
            Log.d(TAG, "Received");
            configure();
        }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_drink_menu);
        configure();
    }

    public ArrayList<DrinkItem> getDrinks(String category) {
        ArrayList<DrinkItem> drinks = new ArrayList<DrinkItem>();

        Cursor c = BACActivity.dbAdapterWearable.getRowByCategory(category);
        Log.d(TAG, "Got Drinks");
        Log.d(TAG, Integer.toString(c.getCount()));
        while (!c.isAfterLast()) {
            DrinkItem drink = new DrinkItem();
            drink.setName(c.getString(DBAdapterWearable.COL_NAME));
            drink.setCount(c.getInt(DBAdapterWearable.COL_COUNT));
            drink.setIngredients(c.getString(DBAdapterWearable.COL_INGREDIENTS));
            drink.setCalories(c.getInt(DBAdapterWearable.COL_CAL));
            drink.setCategory(category);
            double abv = c.getDouble(DBAdapterWearable.COL_ABV);
            drink.setAbv(c.getDouble(DBAdapterWearable.COL_ABV));
            float abvConv = ((float)abv) * .01f;
            Log.d(TAG, "ABV CONV" + Float.toString(abvConv));
            float size = 0.0f;
            if (category.equals("Beer")) {
                size = 12.0f;
            } else if (category.equals("Wine")) {
                size = 8.0f;
            } else if (category.equals("Liquor")) {
                size = 2.0f;
            } else if (category.equals("Cocktail")) {
                size = 8.0f;
            }
            drink.setAlcoholContent(size*abvConv);
            drinks.add(drink);
            c.moveToNext();
        }
        return drinks;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(retrieveMenu);
        } catch (Exception e) {

        }
    }
}