package org.cs160.bactracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import java.util.ArrayList;

public class DrinkCategories extends Activity {

    private WearableListView wearableListView;
    private ArrayList<CategoryItem> categoryItems;
    private final int WIDTH_RESIZE = 50;
    private final int HEIGHT_RESIZE = 50;
    private final int dWIDTH_RESIZE = 20;
    private final int dHEIGHT_RESIZE = 20;
    private final String TAG = "DrinkCategories";
    private DrinkItem testItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_categories);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                wearableListView = (WearableListView)stub.findViewById(R.id.drinksListView);
                setTestCategories();
                CategoryAdapterWearable adapter = new CategoryAdapterWearable(getApplicationContext(), categoryItems);
                wearableListView.setAdapter(adapter);
                wearableListView.setClickListener(new WearableListView.ClickListener() {
                    @Override
                    public void onClick(WearableListView.ViewHolder viewHolder) {
                        onClickAction(viewHolder);
                    }

                    @Override
                    public void onTopEmptyRegionClick() {

                    }
                });

            }
        });
    }

    public void setTestCategories() {
        BitmapFactory.Options o=new BitmapFactory.Options();
        o.inSampleSize = 4;
        o.inDither=false;                     //Disable Dithering mode
        //BEER CATEGORY
        Bitmap beercategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.beer_category, o);
        CategoryItem beercategory = new CategoryItem("Beer", beercategoryimage, WIDTH_RESIZE,HEIGHT_RESIZE);

        Bitmap budlightimage = BitmapFactory.decodeResource(getResources(), R.drawable.budlight, o);
        DrinkItem budlight = new DrinkItem(budlightimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Bud Light", 0.4f, 1, "Pint");
        Bitmap heinekenimage = BitmapFactory.decodeResource(getResources(), R.drawable.heineken, o);
        DrinkItem heineken = new DrinkItem(heinekenimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Heineken", 0.4f, 1, "Pint");
        Bitmap guinnessimage = BitmapFactory.decodeResource(getResources(), R.drawable.guinness, o);
        DrinkItem guinness = new DrinkItem(guinnessimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Guinness", 0.5f, 1, "Pint");
        Bitmap amstellightimage = BitmapFactory.decodeResource(getResources(), R.drawable.amstel_light_beer, o);
        DrinkItem amstellight = new DrinkItem(amstellightimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Amstel Light", 0.3f, 1, "Pint");

        testItem = budlight;

        beercategory.drinks.add(budlight);
        beercategory.drinks.add(heineken);
        beercategory.drinks.add(guinness);
        beercategory.drinks.add(amstellight);

        Bitmap winecategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.wine_category, o);
        CategoryItem winecategory = new CategoryItem("Wine", winecategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);
        

        Bitmap liquorcategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.liquor_category, o);
        CategoryItem liquorcategory = new CategoryItem("Liquor", liquorcategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);

        Bitmap cocktailcategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.cocktails_category, o);
        CategoryItem cocktailcategory = new CategoryItem("Cocktail", cocktailcategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);

        categoryItems = new ArrayList<CategoryItem>();
        categoryItems.add(beercategory);
        categoryItems.add(winecategory);
        categoryItems.add(liquorcategory);
        categoryItems.add(cocktailcategory);


    }

    public void onClickAction(WearableListView.ViewHolder viewHolder){
        Intent i = new Intent(DrinkCategories.this, DrinksListActivity.class);
        int currentPosition = viewHolder.getAdapterPosition();
        ArrayList<DrinkItem> drinksArray = categoryItems.get(currentPosition).drinks;
        Log.d(TAG, Integer.toString(currentPosition));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("drinks", drinksArray);
        i.putExtra("drinks",bundle);
        startActivity(i);
    }


}
