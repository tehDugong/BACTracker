package org.cs160.bactracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;


public class PhoneActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "DrinksFile";
    DBAdapter myDB;
    String TAG;
    ListView categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Log.d(TAG, "first");
        categoryList = (ListView) findViewById(R.id.listCategories);
        openDB();
        //InitializeDatabase();
        Log.d(TAG, "startPhone");

    }

    public void onResume() {
        Log.d(TAG, "onresume");
        super.onResume();
        openDB();
        populateListView();
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "category list click");
                // get selected drink name
                String name = ((TextView) view).getText().toString();
                // start Drink Info Activity of specified drink
                Log.d(TAG, name.toString());
                startCategoryInfo(name);

            }
        });
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
                Intent dbIntent = new Intent(this, PhoneActivity.class);
                startActivity(dbIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListView() {
        Log.d(TAG, "in populateView");
        Cursor cursor = myDB.getAllRows();
        String[] fromFieldNames = new String[] {DBAdapter.KEY_CATEGORY};
        //Log.d(TAG, fromFieldNames[0]);
        int[] toViewIDs = new int[] {R.id.category_text_view};
        //Log.d(TAG, Integer.valueOf(toViewIDs[0]).toString());
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.category_layout, cursor, fromFieldNames, toViewIDs, 0);
        categoryList.setAdapter(myCursorAdapter);
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public void InitializeDatabase(){
        myDB.deleteAll();
        Log.d(TAG, "after delete");
        myDB.insertRow("Merlot", 122, 14.5, "Red Wine", "Merlot grapes");
        myDB.insertRow("Chardonnay", 123, 14.5, "White Wine", "Chardonnay grapes");
        myDB.insertRow("Guinness", 125, 4.1, "Beer", "Roasted unmalted barley");
        myDB.insertRow("Heineken", 150, 5, "Beer", "Barley malt, hops and the unique Heineken A-yeast");

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


}
