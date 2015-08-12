package org.cs160.bactracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

public class CountDrinks extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView drinkName, drinkCount;
    private Button plusButton, minusButton;
    private ImageView drinkImage;
    private String name;
    private int count, categoryIndex, drinkIndex;
    private DrinkItem drink;
    private SQLiteDatabase db;
    public static GoogleApiClient mGoogleApiClient;
    private BroadcastReceiver receiver;
    private final String TAG = "CountDrinks";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_drinks);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                drinkName = (TextView) stub.findViewById(R.id.drinkName);
                drinkCount = (TextView) stub.findViewById(R.id.drinkCount);
                drinkImage = (ImageView) stub.findViewById(R.id.drinkImage);
                Intent i = getIntent();
                drinkIndex = (int) i.getExtras().get("drinkIndex");
                categoryIndex = (int) i.getExtras().get("categoryIndex");
                drink = i.getParcelableExtra("drink");
                name = drink.getName();
                openDB();
                count = selectDB(name);
                drinkName.setText(name);
                drinkImage.setImageBitmap(drink.getImage());
                plusButton = (Button) stub.findViewById(R.id.plusButton);
                minusButton = (Button) stub.findViewById(R.id.minusButton);
                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increment(1);
                    }
                });
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increment(-1);
                    }
                });

                drinkCount.setText(Integer.toString(count));
            }
        });
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
    }
    public void plusButtonClick(View v) {
        increment(1);
    }
    public void minusButtonClick(View v) {
        increment(-1);
    }
    public void increment(int inc) {
        if (inc + count < 0)
            return;
        count += inc;
        drinkCount.setText(Integer.toString(count));
        ArrayList<DrinkItem> categoryItem = DrinkCategories.categoryItems.get(categoryIndex).drinks;
        categoryItem.get(drinkIndex).setCount(count + inc);
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/alcohol");
        float alcohol = drink.getAlcoholContent() * inc;
        putDataMapReq.getDataMap().putFloat("beer", alcohol);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    // Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                }
            }
        });

    }

    public void openDB(){
        db = SQLiteDatabase.openDatabase("/data/data/org.cs160.bactracker/databases/Counts", null, SQLiteDatabase.OPEN_READWRITE);
    }
    public void insertIntoDB (String name, int count, int id) {
        db.execSQL("INSERT INTO counts VALUES('" + name + "','" + Integer.toString(count) + "','" + Integer.toString(id) + "');");
    }
    public void deleteFromDB (String name) {
        db.execSQL("DELETE FROM counts WHERE name='" + name + "'");
    }
    public void updateDB (String name, int count) {
        db.execSQL("UPDATE counts SET count='"+count+"' WHERE name='"+name+"';");
    }
    public int selectDB (String name) {
        Cursor c=db.rawQuery("SELECT * FROM counts WHERE name='"+name+"'", null);
        if(c.moveToFirst()) {
            return Integer.parseInt(c.getString(1).toString());
        } else {
            return 123;
        }
    }
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase("/data/data/org.cs160.bactracker/databases/Counts", null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "does not exist");
        }
        return checkDB != null;
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, Integer.toString(count));
        updateDB(name, count);
        Log.d(TAG, Integer.toString(selectDB(name)));
    }
    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_count_drinks);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                drinkName = (TextView) stub.findViewById(R.id.drinkName);
                drinkCount = (TextView) stub.findViewById(R.id.drinkCount);
                drinkImage = (ImageView) stub.findViewById(R.id.drinkImage);
                Intent i = getIntent();
                drinkIndex = (int) i.getExtras().get("drinkIndex");
                categoryIndex = (int) i.getExtras().get("categoryIndex");
                drink = i.getParcelableExtra("drink");
                name = drink.getName();
                openDB();
                count = selectDB(name);
                drinkName.setText(name);
                drinkImage.setImageBitmap(drink.getImage());
                plusButton = (Button) stub.findViewById(R.id.plusButton);
                minusButton = (Button) stub.findViewById(R.id.minusButton);
                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increment(1);
                    }
                });
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increment(-1);
                    }
                });

                drinkCount.setText(Integer.toString(count));
            }
        });
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
    }


}
