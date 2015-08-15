package org.cs160.bactracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class AddDrinkActivity extends ActionBarActivity{

    private EditText drink_name;
    private EditText drink_alcohol;
    private EditText drink_calories;
    private EditText drink_ingredients;
    DBAdapter myDB;
    GoogleApiClient mGoogleApiClient;


    public String TAG;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri uriSavedImage;
    ImageView imageView;
    protected static File imagesFolder;
    protected static File image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void addDrinkPressed(View view) {
        drink_name = (EditText) findViewById(R.id.drink_name);
        drink_alcohol = (EditText) findViewById(R.id.drink_alcohol);
        drink_calories = (EditText) findViewById(R.id.drink_calories);
        drink_ingredients = (EditText) findViewById(R.id.drink_ingredients);
        Spinner drink_category = (Spinner) findViewById(R.id.drink_category);
        final String spinVal = String.valueOf(drink_category.getSelectedItem());

        final String name = drink_name.getText().toString();
        final Double alcohol = Double.parseDouble(drink_alcohol.getText().toString());
        final Integer calories = Integer.parseInt(drink_calories.getText().toString());
        final String ingredients = drink_ingredients.getText().toString();
        Log.d("TAG", spinVal);

        Log.d("TAG", drink_name.getText().toString());
        Log.d("TAG", drink_alcohol.getText().toString());
        Log.d("TAG", drink_calories.getText().toString());
        Log.d("TAG", drink_ingredients.getText().toString());
        Log.d("TAG", spinVal);
        myDB = new DBAdapter(this);
        try {
            myDB.openToWrite();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        myDB.insertRow(name, calories.intValue(), alcohol.doubleValue(), spinVal, ingredients);
        myDB.close();
        try {
            myDB.openToRead();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor c = myDB.getAllRows();
        while (!c.isAfterLast()) {
            Log.d(TAG, c.getString(DBAdapter.COL_NAME));
            c.moveToNext();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "Connected");
                        final PutDataMapRequest putRequest = PutDataMapRequest.create("/menu");
                        final DataMap map = putRequest.getDataMap();
                        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
                        try {
                            dbAdapter.openToRead();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        ArrayList<DataMap> dataMaps = new ArrayList<DataMap>();
                        Cursor c = dbAdapter.getAllRows();
                        while (!c.isAfterLast()) {
                            DataMap m = new DataMap();
                            m.putString("name", c.getString(0));
                            m.putString("ingredients", c.getString(1));
                            m.putInt("calories", c.getInt(3));
                            m.putDouble("abv", c.getInt(2));
                            m.putString("category", c.getString(4));
                            dataMaps.add(m);
                            c.moveToNext();
                        }
                        map.putDataMapArrayList("dataMaps", dataMaps);
                        Long time = System.currentTimeMillis();
                        map.putLong("time", time);
                        Log.d(TAG, putRequest.asPutDataRequest().getUri().getPath());
                        Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
                        dbAdapter.close();
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "Connection Failed");
                        Log.d(TAG, result.toString());
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        myDB.close();
        name.replaceAll("\\s+","").toLowerCase();
        Log.d(TAG, "insideOnActivity" + name);
        try {
            File newFile = new File(imagesFolder, name + ".jpg");
            image.renameTo(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, PhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    public void takePhoto(View view) {
        String name = "newdrink";

        imageView = (ImageView)findViewById(R.id.takenPhoto);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdir();
        image = new File(imagesFolder, name+".jpg");
        uriSavedImage = Uri.fromFile(image);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriSavedImage);

                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
