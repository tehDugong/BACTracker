package org.cs160.bactracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class DBActivity extends Activity {
    DBAdapter myDB;
    ListView myList;
    final String TAG = "MainActivity";
    String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "in db");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        myList = (ListView) findViewById(R.id. listView);
        openDB();
        InitializeDatabase();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("category");
        }
        populateListView(category);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get selected drink name
                String name = ((TextView) view).getText().toString();
                // start Drink Info Activity of specified drink
                startDrinkInfo(name);

            }
        });
    }



    private void startDrinkInfo(String name){

        Cursor c = myDB.getRowByName(name);

        int abvindex = c.getColumnIndex("abv");
        Log.i(TAG, "abv col index : " + Integer.toString(abvindex));
        int abv = c.getInt(c.getColumnIndex("abv"));

        int ingindex = c.getColumnIndex("ingredients");
        Log.i(TAG, "ingredients col index : " + Integer.toString(ingindex));
        String ingredients = c.getString(c.getColumnIndex("ingredients"));

        int calindex = c.getColumnIndex("calories") + 1;
        Log.i(TAG, "calories col index : " + Integer.toString(calindex));

        int cal = c.getInt(calindex);

        // Launching DrinkInfo on selecting single Drink Item
        Intent i = new Intent(getApplicationContext(), DrinkInfo.class);
        // sending data to new activity
        i.putExtra("name", name);
        i.putExtra("abv", abv);
        i.putExtra("cal", cal);
        i.putExtra("ingredients", ingredients);
        startActivity(i);
    }

    private void populateListView(String category) {

        Cursor cursor = myDB.getRowByCategory(category);
        String[] fromFieldNames = new String[] {DBAdapter.KEY_NAME};
        int[] toViewIDs = new int[] {R.id.name};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.drink_layout, cursor, fromFieldNames, toViewIDs, 0);
        myList.setAdapter(myCursorAdapter);
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public void InitializeDatabase(){
        myDB.deleteAll();
        myDB.insertRow("Red Wine", 1, 1.1, "Wine", "Red Wine Ingredients");
        myDB.insertRow("White Wine", 2 , 2.2, "Wine", "White Wine Ingredients");
//        myDB.insertRow("Red Wine", 1.1 , 1, "Red Wine Ingredients");
//        myDB.insertRow("White Wine", 2.2, 2, "White Wine Ingredients");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_db, menu);
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
    public void queryDatabase(SQLiteDatabase db){

    }

}
