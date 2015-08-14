package org.cs160.bactracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.database.Cursor;

import android.graphics.Matrix;
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
import java.util.Arrays;

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
    DBAdapter myDB = PhoneActivity.myDB;

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
            abv = extras.getDouble("abv");
            ingredients = extras.getString("ingredients");
            category = extras.getString("category");
            Log.d(TAG, "check abv "+ Double.toString(abv));
            calTextView.setText(Integer.toString(cal) + " Calories");
            abvTextView.setText(Double.toString(abv) + " % Alcohol");
            ingredientsTextView.setText(ingredients);
        }
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //try to get form drawable
        picturename = name.replaceAll("\\s+","").toLowerCase();
        Log.i(TAG, picturename);
        Context context = drinkImageView.getContext();
        int id = context.getResources().getIdentifier(picturename, "drawable", context.getPackageName());
        Log.d(TAG, "drinkInfo"+id);

        //Found drawable
        if (id != 0) {
            drinkImageView.setImageResource(id);
        }
        else if (id == 0){
            String drinkPictureName = name.replaceAll("\\s+","").toLowerCase();
            File imgFile = PhoneActivity.imagesFolder;

            Log.d(TAG, "drinkPicName " + drinkPictureName);

            //if (imgFile != null) {


                String mCurrentPhotoPath = imgFile.getAbsolutePath() + "/" + drinkPictureName + ".jpg";
                Bitmap checkValidPath = BitmapFactory.decodeFile(mCurrentPhotoPath);

                if (checkValidPath != null) {
                    // Get the dimensions of the bitmap
                    Log.d(TAG, "photo path is not null");
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = 12;

                    Bitmap myBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);

                    drinkImageView.setImageBitmap(myBitmap);
                } else {
                    //User didn't take a photo, use category one
                    Log.d(TAG, "photo path is null");
                    picturename = category.replaceAll("\\s+", "").toLowerCase() + "icon";
                    context = drinkImageView.getContext();
                    id = context.getResources().getIdentifier(picturename, "drawable", context.getPackageName());
                    drinkImageView.setImageResource(id);
                }
            }
        /*
            else {
                //User didn't take a photo, use category one
                Log.d(TAG, "image file is null");
                picturename = category.replaceAll("\\s+", "").toLowerCase() + "icon";
                context = drinkImageView.getContext();
                id = context.getResources().getIdentifier(picturename, "drawable", context.getPackageName());
                drinkImageView.setImageResource(id);
            }
            */
       // }
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
            case android.R.id.home:
                this.finish();
                return true;
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

    public void removeFromMenu(View view){
        Log.d(TAG, "removeFromMenu");
        boolean flag = true;
        if (flag == true) {
            //((ImageButton) view).setBackgroundResource(R.drawable.addtomenu);
        }
        else{

        }
    }

    public void deleteFromDB(View view) {
        if (Arrays.asList(DBAdapter.genericDrinks).contains(name)) {
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setMessage("Common Drinks cannot be deleted")
                    .setTitle("Alert")
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            ad.show();

        }
        else{
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this drink?")
                    .setTitle("Delete?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteOperation();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .create();
            ad.show();
        }

    }

    private void deleteOperation(){
        //DBAdapter myDB = new DBAdapter(this);
        myDB.open();
        Cursor c = myDB.getRowByName(name);
        String id = "'"+c.getString(0)+"'";
        Log.i(TAG, id);
        myDB.deleteRow(id);
        Intent PhoneIntent = new Intent(this, PhoneActivity.class);
        startActivity(PhoneIntent);
        myDB.close();
        this.finish();
    }
}
