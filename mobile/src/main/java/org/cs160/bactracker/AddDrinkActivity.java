package org.cs160.bactracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;


public class AddDrinkActivity extends ActionBarActivity {

    private EditText drink_name;
    private EditText drink_alcohol;
    private EditText drink_calories;
    private EditText drink_ingredients;

    public String TAG = "addDrink";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_drink, menu);
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

    public void addDrinkPressed(View view){
        drink_name = (EditText) findViewById(R.id.drink_name);
        drink_alcohol = (EditText) findViewById(R.id.drink_alcohol);
        drink_calories = (EditText) findViewById(R.id.drink_calories);
        drink_ingredients = (EditText) findViewById(R.id.drink_ingredients);
        Spinner drink_category = (Spinner) findViewById(R.id.drink_category);
        String spinVal = String.valueOf(drink_category.getSelectedItem());

        Log.d("TAG", drink_name.getText().toString());
        Log.d("TAG", drink_alcohol.getText().toString());
        Log.d("TAG", drink_calories.getText().toString());
        Log.d("TAG", drink_ingredients.getText().toString());
        Log.d("TAG", spinVal);

        Intent intent = new Intent(this, DBActivity.class);
        startActivity(intent);

    }
}
