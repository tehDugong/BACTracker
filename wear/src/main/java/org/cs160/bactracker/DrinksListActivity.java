package org.cs160.bactracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DrinksListActivity extends Activity {
    private ListView list;
    public static ArrayList<Item> drinksArray;
    private int selectedPosition;
    private EntryItem itemSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_drinks_list);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                list = (ListView) stub.findViewById(R.id.list);
                drinksArray = new ArrayList<Item>();
                Item beer = new SectionItem("Beers");
                Item cocktails = new SectionItem("Cocktails");
                Item wines = new SectionItem("Wines");
                Item darkBeer = new EntryItem("Dark Beer",0, .7f);
                Item lightBeer = new EntryItem("Light Beer", 0, .5f);
                drinksArray.add(beer);
                drinksArray.add(darkBeer);
                drinksArray.add(lightBeer);
                drinksArray.add(cocktails);
                drinksArray.add(wines);
                EntryAdapter adapter = new EntryAdapter(getApplicationContext(), drinksArray);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Item clicked", Integer.toString(position));
                        Intent i = new Intent(DrinksListActivity.this, CountDrinks.class);
                        EntryItem item = (EntryItem)drinksArray.get(position);
                        i.putExtra("drinkName", item.title);
                        i.putExtra("drinkCount", item.drinkCount);
                        i.putExtra("alc", item.alc);
                        i.putExtra("index", position);
                        startActivity(i);
                    }
                });
            }
        });
    }
}
