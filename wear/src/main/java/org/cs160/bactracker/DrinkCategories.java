package org.cs160.bactracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class DrinkCategories extends Activity {
    private ListView wearableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_categories);
        wearableListView = (ListView)findViewById(R.id.drinksListView);
        CategoryItem beer = new CategoryItem();
        beer.drinks=new ArrayList<DrinkItem>();
        beer.name = "Beer";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beer);
        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        beer.image = resizedbitmap;
        ArrayList<CategoryItem> categoryList = new ArrayList<CategoryItem>();
        categoryList.add(beer);
        CategoryAdapter adapter = new CategoryAdapter(getApplicationContext(), categoryList);
        wearableListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drink_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
