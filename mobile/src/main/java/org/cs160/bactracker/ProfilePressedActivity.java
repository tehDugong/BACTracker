package org.cs160.bactracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class ProfilePressedActivity extends ActionBarActivity {

    String TAG; //del
    private EditText user_name;
    private EditText user_weight;
    private String name = "";
    private String weightStr = "";
    private int weight;
    private Boolean gender;
    public static final String PREFS_NAME = "DrinksFile";
    SharedPreferences drinks;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "profile onCreate");
        drinks = getSharedPreferences(PREFS_NAME, 0);
        editor = drinks.edit();

        /*
        if (savedInstanceState == null)
            Log.d(TAG, "null");
        else
            Log.d(TAG, "not null"+savedInstanceState.toString());
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pressed);
        /*
        user_name = (EditText)findViewById(R.id.user_name);

        if ((savedInstanceState != null) && (savedInstanceState.getString("name") != null)) {
            name = savedInstanceState.getString("name");
            user_name = (EditText)findViewById(R.id.user_name);
            user_name.setText(name);
        }
        */

    }
    /*
    public void onResume(){
        super.onResume();
        Log.d(TAG, "profile on resume");
        user_name = (EditText) findViewById(R.id.user_name);
        user_weight = (EditText) findViewById(R.id.user_weight);
        user_name.setText(name);
        user_weight.setText(weight);

        //male_button = (RadioButton) findViewById(R.id.male_button);


        //if (gender = true){
          //  set
        //}

    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_pressed, menu);
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
                Intent dbIntent = new Intent(this, PhoneActivity.class);
                startActivity(dbIntent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle state){
        Log.d(TAG, "in saveInstance");
        Log.d(TAG, name);
        state.putString("name", name);
        super.onSaveInstanceState(state);
        Log.d(TAG, state.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        Log.d(TAG, "in RestoreInstance");
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString("name");
        user_name = (EditText)findViewById(R.id.user_name);
        user_name.setText(name);
    }
    */

    //RadioButton = Male/Female
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.male_button:
                if (checked){
                    //Log.d(TAG, "male button checked");
                    gender = true;
                    editor.putBoolean("gender", gender);
                    editor.commit();
                    Log.d(TAG, String.valueOf(drinks.getBoolean("gender", gender)));
                    //RadioButton maleButton = (RadioButton)findViewById(R.id.male_button);
                    //maleButton.setChecked(true);
                }
                break;
            case R.id.female_button:
                if (checked){
                    //Log.d(TAG, "female button checked");
                    gender = false;
                    editor.putBoolean("gender", gender);
                    editor.commit();
                    Log.d(TAG, String.valueOf(drinks.getBoolean("gender", gender)));
                    //RadioButton femaleButton = (RadioButton)findViewById(R.id.female_button);
                    //femaleButton.setChecked(true);
                }
                break;
        }
    }


    public void pressSaveButton(View view) {
        Intent intent = new Intent(this, PhoneActivity.class);


        user_name = (EditText) findViewById(R.id.user_name);
        user_weight = (EditText) findViewById(R.id.user_weight);
        name = user_name.getText().toString();
        weightStr = user_weight.getText().toString();
        weight = Integer.parseInt(weightStr);
        //user_name.setText(name);
        //user_weight.setText(weight);
        //Log.d(TAG, name);
        //Log.d(TAG, weight);
        editor.putString("name", name);
        editor.putInt("weight", weight);
        editor.commit();
        Log.d(TAG, "in here");
        Log.d(TAG, drinks.getString("name", name));
        Log.d(TAG, String.valueOf(drinks.getInt("weight", weight)));



        Toast.makeText(getApplicationContext(), "Profile saved!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }



}