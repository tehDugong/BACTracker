package org.cs160.bactracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AddDrinkActivity extends ActionBarActivity {

    private EditText drink_name;
    private EditText drink_alcohol;
    private EditText drink_calories;
    private EditText drink_ingredients;
    DBAdapter myDB = PhoneActivity.myDB;


    public String TAG;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri uriSavedImage;
    ImageView imageView;
    protected static File imagesFolder = PhoneActivity.imagesFolder;
    protected static File image;
    protected static String mCurrentPhotoPath;
    protected static Switch photoSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        openDB();

        photoSwitch = (Switch) findViewById(R.id.photoSwitch);
        photoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    takePhoto();
                }
            }
        });
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

    private void openDB() {
        //myDB = new DBAdapter(this);
        myDB.open();
    }

    public void addDrinkPressed(View view) {
        drink_name = (EditText) findViewById(R.id.drink_name);
        drink_alcohol = (EditText) findViewById(R.id.drink_alcohol);
        drink_calories = (EditText) findViewById(R.id.drink_calories);
        drink_ingredients = (EditText) findViewById(R.id.drink_ingredients);
        Spinner drink_category = (Spinner) findViewById(R.id.drink_category);
        String spinVal = String.valueOf(drink_category.getSelectedItem());

        String name = drink_name.getText().toString();
        Double alcohol = Double.parseDouble(drink_alcohol.getText().toString());
        Integer calories = Integer.parseInt(drink_calories.getText().toString());
        String ingredients = drink_ingredients.getText().toString();
        Log.d("TAG", spinVal);

        Log.d("TAG", drink_name.getText().toString());
        Log.d("TAG", drink_alcohol.getText().toString());
        Log.d("TAG", drink_calories.getText().toString());
        Log.d("TAG", drink_ingredients.getText().toString());
        Log.d("TAG", spinVal);

        myDB.insertRow(name, calories, alcohol, spinVal, ingredients);
        name.replaceAll("\\s+","").toLowerCase();
        Log.d(TAG, "insideOnActivity" + name);

        String reName = name.replaceAll("\\s+","").toLowerCase();
        File newFile = new File(imagesFolder, reName+".jpg");

        if (photoSwitch.isChecked()) {
            Log.d(TAG, "photo renamed as "+ reName);
            image.renameTo(newFile);
            mCurrentPhotoPath = image.getAbsolutePath();
        }

        Intent intent = new Intent(this, PhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    public void takePhoto(){

        imageView = (ImageView)findViewById(R.id.takenPhoto);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            //imagesFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            image = new File(imagesFolder, "newdrink.jpg");
        }

        mCurrentPhotoPath = image.getAbsolutePath();
        uriSavedImage = Uri.fromFile(image);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "taken");
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                try {
                    Log.d(TAG, "before");
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriSavedImage);
                    Log.d(TAG, "after");

                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = 12;

                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
