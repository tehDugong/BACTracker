package org.cs160.bactracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrinksListActivity extends Activity {
    private GridView drinksGridView;
    private TextView titleView;
    public ArrayList<DrinkItem> drinks;
    private String categoryName;
    private int selectedPosition;
    private final String TAG = "DrinksListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_drinks_list);
        int position = (int)getIntent().getExtras().get("position");
        drinks = DrinkCategories.categoryItems.get(position).drinks;
        for (DrinkItem drink : drinks) {
            Log.d(TAG, "name = " + drink.getName());
        }
        categoryName = (String)getIntent().getExtras().get("name");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                drinksGridView = (GridView)stub.findViewById(R.id.drinksGridView);
                DrinksListAdapter adapter = new DrinksListAdapter(getApplicationContext(), drinks);
                drinksGridView.setAdapter(adapter);
                titleView = (TextView)stub.findViewById(R.id.drinkListTitle);
                titleView.setText(categoryName);
                drinksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onClickAction(position);
                    }
                });
            }
        });
    }

    public void onClickAction(int selectedPosition) {
        DrinkItem item = drinks.get(selectedPosition);
        Intent i = new Intent(DrinksListActivity.this, CountDrinks.class);
        i.putExtra("drink", item);
        startActivity(i);
    }
}
