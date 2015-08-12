package org.cs160.bactracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import java.util.ArrayList;

public class DrinkCategories extends Activity {

    private WearableListView wearableListView;
    public static ArrayList<CategoryItem> categoryItems;
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
                BitmapFactory.Options o=new BitmapFactory.Options();
                o.inDither=false;                     //Disable Dithering mode
                o.inSampleSize=8;
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
        o.inDither=false;                     //Disable Dithering mode
        o.inSampleSize=8;
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
        Bitmap keystonelightimage = BitmapFactory.decodeResource(getResources(), R.drawable.keystone_light, o);
        DrinkItem keystonelight = new DrinkItem(keystonelightimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Keystone Light", 0.3f, 1, "Pint");
        Bitmap becksimage = BitmapFactory.decodeResource(getResources(), R.drawable.becks, o);
        DrinkItem becks = new DrinkItem(becksimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Becks", 0.3f, 1, "Pint");

        testItem = budlight;

        beercategory.drinks.add(budlight);
        beercategory.drinks.add(heineken);
        beercategory.drinks.add(guinness);
        beercategory.drinks.add(amstellight);
        beercategory.drinks.add(keystonelight);
        beercategory.drinks.add(becks);

        Bitmap winecategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.wine_category, o);
        CategoryItem winecategory = new CategoryItem("Wine", winecategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);
        Bitmap gallopinotgrigioimage = BitmapFactory.decodeResource(getResources(), R.drawable.gallo_family_pinot_grigio);
        DrinkItem gallopinotgrigio = new DrinkItem(gallopinotgrigioimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Gallo Family: Pinot Grigio", 0.6f, 1, "glass");
        Bitmap merlotredwoodcreekimage = BitmapFactory.decodeResource(getResources(), R.drawable.merlot_redwood_creek);
        DrinkItem merlotredwoodcreek = new DrinkItem(merlotredwoodcreekimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Redwood Creek: Merlot", 0.6f, 1, "glass");
        Bitmap suttercabernetimage = BitmapFactory.decodeResource(getResources(), R.drawable.sutter_home_cabernet_suvignon);
        DrinkItem suttercabernet = new DrinkItem(suttercabernetimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Sutter Home: Cabernet Suvignon", 0.6f, 1, "glass");

        winecategory.drinks.add(gallopinotgrigio);
        winecategory.drinks.add(merlotredwoodcreek);
        winecategory.drinks.add(suttercabernet);

        Bitmap liquorcategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.liquor_category, o);
        CategoryItem liquorcategory = new CategoryItem("Liquor", liquorcategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);
        Bitmap ginmareimage = BitmapFactory.decodeResource(getResources(), R.drawable.gin_mare_gin);
        DrinkItem ginmare = new DrinkItem(ginmareimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Gin: Gin Mare", 0.6f, 1, "shot");
        Bitmap absolutevodkaimage = BitmapFactory.decodeResource(getResources(), R.drawable.absolute_vodka);
        DrinkItem absolutevodka = new DrinkItem(absolutevodkaimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Vodka: Absolute", 0.6f, 1, "shot");
        Bitmap jamesonimage = BitmapFactory.decodeResource(getResources(), R.drawable.jameson_whiskey);
        DrinkItem jameson = new DrinkItem(jamesonimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Whiskey: Jameson", 0.6f, 1, "shot");
        Bitmap jackdanielsimage = BitmapFactory.decodeResource(getResources(), R.drawable.jack_daniels_whiskey);
        DrinkItem jackdaniels = new DrinkItem(jackdanielsimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Whiskey: Jack Daniels", 0.6f, 1, "shot");
        liquorcategory.drinks.add(ginmare);
        liquorcategory.drinks.add(absolutevodka);
        liquorcategory.drinks.add(jameson);
        liquorcategory.drinks.add(jackdaniels);

        Bitmap cocktailcategoryimage = BitmapFactory.decodeResource(getResources(), R.drawable.cocktails_category, o);
        CategoryItem cocktailcategory = new CategoryItem("Cocktail", cocktailcategoryimage, WIDTH_RESIZE, HEIGHT_RESIZE);
        Bitmap martiniimage = BitmapFactory.decodeResource(getResources(), R.drawable.martini);
        DrinkItem martini = new DrinkItem(martiniimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Martini", 0.6f, 1, "glass");
        Bitmap margeritaimage = BitmapFactory.decodeResource(getResources(), R.drawable.margerita);
        DrinkItem margerita = new DrinkItem(margeritaimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Margerita", 0.6f, 1, "glass");
        Bitmap ginandtonicimage = BitmapFactory.decodeResource(getResources(), R.drawable.gin_and_tonic);
        DrinkItem ginandtonic = new DrinkItem(ginandtonicimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Gin and Tonic", 0.7f, 1, "glass");
        Bitmap jackandcokeimage = BitmapFactory.decodeResource(getResources(), R.drawable.jack_and_coke);
        DrinkItem jackandcoke = new DrinkItem(jackandcokeimage, dWIDTH_RESIZE, dHEIGHT_RESIZE, "Jack and Coke", 0.7f, 1, "glass");


        cocktailcategory.drinks.add(martini);
        cocktailcategory.drinks.add(margerita);
        cocktailcategory.drinks.add(ginandtonic);
        cocktailcategory.drinks.add(jackandcoke);

        categoryItems = new ArrayList<CategoryItem>();
        categoryItems.add(beercategory);
        categoryItems.add(winecategory);
        categoryItems.add(liquorcategory);
        categoryItems.add(cocktailcategory);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleBitmaps();
    }

    public void recycleBitmaps() {
        for (CategoryItem ci : categoryItems) {
            ci.image.recycle();
            ci.image = null;
            if (ci.drinks != null &&ci.drinks.size() > 0) {
                for (DrinkItem drink : ci.drinks) {
                    drink.recycleImages();
                }
            }
        }
    }

    public void onClickAction(WearableListView.ViewHolder viewHolder){
        Intent i = new Intent(DrinkCategories.this, DrinksListActivity.class);
        int currentPosition = viewHolder.getAdapterPosition();
        ArrayList<DrinkItem> drinksArray = categoryItems.get(currentPosition).drinks;
        CategoryItem categoryItem = categoryItems.get(currentPosition);
        String name = categoryItem.name;
        i.putExtra("position", currentPosition);
        i.putExtra("name", name);
        startActivity(i);
    }


}
