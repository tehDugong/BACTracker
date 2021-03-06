package org.cs160.bactracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;

public class CountDrinks extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView drinkName, drinkCount;
    private Button plusButton, minusButton;
    private ImageView drinkImage;
    private String name;
    private int count;
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
                drink = i.getParcelableExtra("drink");
                name = drink.getName();
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
                count = drink.getCount();
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
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/alcohol");
        float alcohol = drink.getAlcoholContent() * inc;
        Log.i(TAG, "Alcohol calculated: " + alcohol);
        sendMessage("/alcohol", ByteBuffer.allocate(4).putFloat(alcohol).array());
        Log.d(TAG, Boolean.toString(BACActivity.dbAdapterWearable.updateRow(drink.getName(), count)));
        Cursor c = BACActivity.dbAdapterWearable.getAllRows();
        while (!c.isAfterLast()) {
            Log.d(TAG, c.getString(DBAdapterWearable.COL_NAME) + ":"+ c.getInt(DBAdapterWearable.COL_COUNT));
            c.moveToNext();
        }
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
                drink = i.getParcelableExtra("drink");
                name = drink.getName();
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
                count = drink.getCount();
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
}
