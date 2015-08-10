package org.cs160.bactracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mwaliman on 8/1/15.
 */
public class DrinkInfo extends Activity {
    String name;
    int cal;
    double abv;
    String ingredients;
    String picturename;
    String category;
    private static final String TAG = "DrinkInfo"; //used for logging database version changes

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_info);
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
//        getActionBar().setTitle(name);

        picturename = category.replaceAll("\\s+","").toLowerCase();
        Log.i(TAG, picturename);
        Context context = drinkImageView.getContext();
        int id = context.getResources().getIdentifier(picturename, "drawable", context.getPackageName());
        drinkImageView.setImageResource(id);
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
