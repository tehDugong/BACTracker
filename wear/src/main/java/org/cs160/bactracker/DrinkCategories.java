package org.cs160.bactracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class DrinkCategories extends Activity {
    private WearableListView wearableListView;
    private final int WIDTH_RESIZE = 50;
    private final int HEIGHT_RESIZE = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_categories);
        wearableListView = (WearableListView)findViewById(R.id.drinksListView);

        Bitmap beercategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.beer_category);
        CategoryItem beercategory = new CategoryItem("Beer", beercategoryimage, WIDTH_RESIZE,HEIGHT_RESIZE);

        Bitmap winecategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.wine_category);
        CategoryItem winecategory = new CategoryItem("Wine", winecategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);

        Bitmap liquorcategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.liquor_category);
        CategoryItem liquorcategory = new CategoryItem("Liquor", liquorcategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);

        Bitmap cocktailcategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.cocktails_category);
        CategoryItem cocktailcategory = new CategoryItem("Cocktail", cocktailcategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);

        ArrayList<CategoryItem> categoryList = new ArrayList<CategoryItem>();
        categoryList.add(beercategory);
        categoryList.add(winecategory);
        categoryList.add(liquorcategory);
        categoryList.add(cocktailcategory);

        CategoryAdapterWearable adapter = new CategoryAdapterWearable(getApplicationContext(), categoryList);
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
