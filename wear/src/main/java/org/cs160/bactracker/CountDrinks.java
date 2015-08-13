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
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.nio.ByteBuffer;
>>>>>>> 3a69c26a746793ce3ad81465927b02df18e1c477

public class CountDrinks extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView drinkName, drinkCount;
    private Button plusButton, minusButton;
    private ImageView drinkImage;
    private String name;
<<<<<<< HEAD
    private int count, categoryIndex, drinkIndex;
=======
    private int count;
>>>>>>> 3a69c26a746793ce3ad81465927b02df18e1c477
    private DrinkItem drink;
    private SQLiteDatabase db;
    public static GoogleApiClient mGoogleApiClient;
<<<<<<< HEAD
    private BroadcastReceiver receiver;
=======
>>>>>>> 3a69c26a746793ce3ad81465927b02df18e1c477
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
<<<<<<< HEAD
        ArrayList<DrinkItem> categoryItem = DrinkCategories.categoryItems.get(categoryIndex).drinks;
        categoryItem.get(drinkIndex).setCount(count + inc);
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/alcohol");
=======
>>>>>>> 3a69c26a746793ce3ad81465927b02df18e1c477
        float alcohol = drink.getAlcoholContent() * inc;
        Log.i(TAG, "Alcohol calculated: "+alcohol);
        sendMessage("/alcohol", ByteBuffer.allocate(4).putFloat(alcohol).array());
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
<<<<<<< HEAD
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


=======

    private void sendMessage(final String path, final byte[] data){
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(
                        mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.i(TAG, "Sending message: "+path);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, data ).await();
                }
            }
        }).start();
    }
>>>>>>> 3a69c26a746793ce3ad81465927b02df18e1c477
}
