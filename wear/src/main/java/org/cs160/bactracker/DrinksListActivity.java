package org.cs160.bactracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class DrinksListActivity extends Activity {
    private ListView list;
    public ArrayList<DrinkItem> drinks;
    private int selectedPosition;
    private final String TAG = "DrinksListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_drinks_list);
        drinks = ((Bundle)getIntent().getBundleExtra("drinks")).getParcelableArrayList("drinks");
        Log.d(TAG, drinks.toString());
        for (DrinkItem drink : drinks) {
            Log.d(TAG, "name = " + drink.getName());
        }

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

            }
        });
    }
}
