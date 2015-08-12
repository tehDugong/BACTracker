package org.cs160.bactracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by mwaliman on 8/1/15.
 */
public class DrinkInfoActivity extends ActionBarActivity {
    String name;
    int cal;
    double abv;
    String ingredients;
    String picturename;
    String category;
    private static final String TAG = "DrinkInfoActivity"; //used for logging database version changes

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drink_info);
        TextView calTextView = (TextView) findViewById(R.id.calTextView);
        TextView abvTextView = (TextView) findViewById(R.id.abvTextView);
        TextView ingredientsTextView = (TextView) findViewById(R.id.ingredientsTextView);
        ImageView drinkImageView = (ImageView) findViewById(R.id.drinkImageView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            cal = extras.getInt("cal");
            abv = extras.getInt("abv");
            ingredients = extras.getString("ingredients");
            category = extras.getString("category");
            //Log.d(TAG, "drinkinfo "+ category);
            calTextView.setText(Integer.toString(cal) + " Calories");
            abvTextView.setText(Double.toString(abv) + " % Alcohol");
            ingredientsTextView.setText(ingredients);
        }
        getSupportActionBar().setTitle(name);

        //picturename = category.replaceAll("\\s+","").toLowerCase();
        picturename = name.replaceAll("\\s+","").toLowerCase();
        Log.i(TAG, picturename);
        Context context = drinkImageView.getContext();
        int id = context.getResources().getIdentifier(picturename, "drawable", context.getPackageName());
        Log.d(TAG, "drinkInfo"+id);
        if (id == 0) {
            String drinkPictureName = name.replaceAll("\\s+","").toLowerCase();
            Log.d(TAG, "drinkPicName" + drinkPictureName);
            File imgFile = AddDrinkActivity.imagesFolder;

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()+"/"+drinkPictureName+".jpg");
                drinkImageView.setImageBitmap(myBitmap);

            }
        }
        else {
            drinkImageView.setImageResource(id);
        }
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
                //Intent dbIntent = new Intent(this, PhoneActivity.class);
                //startActivity(dbIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void removeFromMenu(View view){
        Log.d(TAG, "removeFromMenu");
        boolean flag = true;
        if (flag == true) {
            //((ImageButton) view).setBackgroundResource(R.drawable.addtomenu);
        }
        else{

        }

    }

}
