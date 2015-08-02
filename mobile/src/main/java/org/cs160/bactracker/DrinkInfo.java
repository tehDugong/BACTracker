package org.cs160.bactracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by mwaliman on 8/1/15.
 */
public class DrinkInfo extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_info);
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView calTextView = (TextView) findViewById(R.id.calTextView);
        TextView abvTextView = (TextView) findViewById(R.id.abvTextView);
        TextView ingredientsTextView = (TextView) findViewById(R.id.ingredientsTextView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name");
            int cal = extras.getInt("cal");
            double abv = extras.getInt("abv");
            String ingredients = extras.getString("ingredients");
            nameTextView.setText(name);
            calTextView.setText(Integer.toString(cal) + " calories");
            abvTextView.setText(Double.toString(abv) + " %");
            ingredientsTextView.setText(ingredients);
        }
    }



}
