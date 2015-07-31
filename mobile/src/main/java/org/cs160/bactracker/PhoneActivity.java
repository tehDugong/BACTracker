package org.cs160.bactracker;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;


public class PhoneActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "DrinksFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        // Create a SharedPreferences file listing what drinks are available. For debugging only
        // File will include weight, height, Time passed since drinking, and alcohol
        // I should really be using a SQLite db for this...

        SharedPreferences drinks = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = drinks.edit();
        editor.putFloat("ratio_male", 0.73f);
        editor.putFloat("ratio_female", 0.66f);
        editor.putInt("weight", 140); // weight in lbs
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        editor.putInt("start_time", seconds); // current time, in seconds
        editor.putFloat("alcohol", 0.0f); // the amount of alcohol consumed in oz

        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone, menu);
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
