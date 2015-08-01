package org.cs160.bactracker;

import android.content.Intent;
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
    private EditText user_name;  //= (EditText) findViewById(R.id.user_name);
    private EditText user_weight; // = (EditText) findViewById(R.id.user_weight);
    private String name; // = user_name.getText().toString();
    private String weight; // = user_weight.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState == null)
            Log.d(TAG, "null");
        else
            Log.d(TAG, savedInstanceState.toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pressed);



        user_name = (EditText)findViewById(R.id.user_name);

        if ((savedInstanceState != null) && (savedInstanceState.getString("name") != null)) {
            name = savedInstanceState.getString("name");
            user_name = (EditText)findViewById(R.id.user_name);
            user_name.setText(name);
        }




    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle state){
        Log.d(TAG, "in saveInstance");
        Log.d(TAG, name);
        state.putString("name", name);
        super.onSaveInstanceState(state);
        Log.d(TAG, state.toString());
    }


    /*
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
                    //save in database
                    Log.d(TAG, "male button checked");
                    RadioButton maleButton = (RadioButton)findViewById(R.id.male_button);
                    maleButton.setChecked(true);
                }
                break;
            case R.id.female_button:
                if (checked){
                    //save in database
                    Log.d(TAG, "female button checked");
                    RadioButton femaleButton = (RadioButton)findViewById(R.id.female_button);
                    femaleButton.setChecked(true);
                }
                break;
        }
    }


    public void pressSaveButton(View view) {
        Intent intent = new Intent(this, PhoneActivity.class);
        user_name = (EditText) findViewById(R.id.user_name);
        user_weight = (EditText) findViewById(R.id.user_weight);
        name = user_name.getText().toString();
        weight = user_weight.getText().toString();
        //TextView trial;
        //trial = (TextView)findViewById(R.id.name_text);
        //trial.setText(name);
        user_weight.setText(weight);
        Log.d(TAG, name);
        Log.d(TAG, weight);
        Toast.makeText(getApplicationContext(), "Profile saved!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}