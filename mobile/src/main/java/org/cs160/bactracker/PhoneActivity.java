package org.cs160.bactracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;


public class PhoneActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "DrinksFile";
    protected static DBAdapter myDB;
    String TAG;
    ListView categoryList;
    protected static File imagesFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Log.d(TAG, "first");
        //openDB(); //used to reset database
        //InitializeDatabase(); //used to reset the database
        Log.d(TAG, "startPhone");
        imagesFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    }

    public void onResume() {
        Log.d(TAG, "onresume");
        super.onResume();
        openDB();
        /*
        populateListView();
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Log.d(TAG, "category list click");
                //String name = ((TextView) view).getText().toString();
                Cursor c = (Cursor)categoryList.getItemAtPosition(position);
                String name = c.getString(c.getColumnIndex("category"));


                Log.d(TAG, name);
                startCategoryInfo(name);

            }
        });
        */
    }

    @Override
    public void onBackPressed(){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
            //return true;
        //}
        switch (id) {
            case R.id.action_add_drink:
                Intent addDrinkIntent = new Intent(this, AddDrinkActivity.class);
                startActivity(addDrinkIntent);
                break;
            case R.id.action_profile:
                Intent profileIntent = new Intent(this, ProfilePressedActivity.class);
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(profileIntent);
                break;
            case R.id.action_info:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListView() {
        Log.d(TAG, "in populateView");

        /*
        Integer[] imageId = {
                R.drawable.beericon,
                R.drawable.cocktailicon,
                R.drawable.liquoricon,
                R.drawable.wineicon
        };
        */

        Cursor cursor = myDB.getCategories();
        String[] fromFieldNames = new String[] {DBAdapter.KEY_CATEGORY};
        int[] toViewIDs = new int[] {R.id.category_text_view};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.category_layout, cursor, fromFieldNames, toViewIDs, 0);
        categoryList.setAdapter(myCursorAdapter);

        /*
        ImageView imageView = null;
        int position = 0;
        Log.d(TAG, Integer.valueOf(position).toString());
        cursor.moveToFirst();
        while (!cursor.isLast()) {
            position = cursor.getPosition();
            View childView = categoryList.getChildAt(position);
            imageView = (ImageView) childView.findViewById(R.id.wineImage);
            Log.d(TAG, "here" + position);
            imageView.setImageResource(imageId[position]);
            cursor.moveToNext();
        }
        imageView.setImageResource(imageId[position]);
        */

    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public void InitializeDatabase(){
        myDB.deleteAll();
        Log.d(TAG, "after delete");
        myDB.insertRow("Merlot", 122, 14.5, "Wine", "Merlot grapes");
        myDB.insertRow("Chardonnay", 123, 14.5, "Wine", "Chardonnay grapes");
        myDB.insertRow("Guinness", 125, 4.1, "Beer", "Roasted unmalted barley");
        myDB.insertRow("Heineken", 150, 5, "Beer", "Barley malt, hops and the unique Heineken A-yeast");
        myDB.insertRow("Long Island Iced Tea", 780, 22, "Cocktail", "Gin, Tequila, Vodka, Run, Triple sec");
        myDB.insertRow("Margarita", 153, 22, "Cocktail", "Tequila, triple sec, lime, lemon");
        myDB.insertRow("Vodka", 64, 40, "Liquor", "Water, ethanol");
        myDB.insertRow("Whisky", 70, 40, "Liquor", "Fermented grain mash");

//        myDB.insertRow("Red Wine", 1.1 , 1, "Red Wine Ingredients");
//        myDB.insertRow("White Wine", 2.2, 2, "White Wine Ingredients");
    }

    private void startCategoryInfo(String category){
        Log.d(TAG, "start Category Info");
        Cursor c = myDB.getRowByCategory(category);

        /*
        int abvindex = c.getColumnIndex("abv");
        Log.i(TAG, "abv col index : " + Integer.toString(abvindex));
        int abv = c.getInt(c.getColumnIndex("abv"));

        int ingindex = c.getColumnIndex("ingredients");
        Log.i(TAG, "ingredients col index : " + Integer.toString(ingindex));
        String ingredients = c.getString(c.getColumnIndex("ingredients"));

        int calindex = c.getColumnIndex("calories") + 1;
        Log.i(TAG, "calories col index : " + Integer.toString(calindex));

        int cal = c.getInt(calindex);
        */

        // Launching DrinkInfoActivity on selecting single Drink Item
        Intent i = new Intent(getApplicationContext(), DBActivity.class);
        // sending data to new activity
        i.putExtra("category", category);

        /*
        i.putExtra("abv", abv);
        i.putExtra("cal", cal);
        i.putExtra("ingredients", ingredients);
        */
        startActivity(i);
    }

    public void pressBeer(View view){
        startCategoryInfo("Beer");
    }

    public void pressCocktail(View view){
        startCategoryInfo("Cocktail");
    }

    public void pressLiquor(View view){
        startCategoryInfo("Liquor");
    }
    public void pressWine(View view){
        startCategoryInfo("Wine");
    }



}
