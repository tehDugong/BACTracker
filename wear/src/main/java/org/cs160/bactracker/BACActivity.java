package org.cs160.bactracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class BACActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "BACActivity";
    public static GoogleApiClient mGoogleApiClient;
    private static final String PATH = "/alcohol";
    public static float alcohol = 0.0f;

    private BroadcastReceiver receiver;
    static final public String BACTRACKER_RESULT = "org.cs160.BACTracker" +
            ".backend.BACService.REQUEST_PROCESSED";
    static final public String BACTRACKER_MESSAGE = "org.cs160.BACTracker" +
            ".backend.BACService.BAC_MSG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "BACActivity started!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bac);

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
        Log.i(TAG, "BACActivity.onCreate finished");

        // broadcast listener
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Broadcast received!");
                String s = intent.getStringExtra(BACTRACKER_MESSAGE);
                Log.i(TAG, "Found BAC: "+s);
                TextView view = (TextView) findViewById(R.id.current_BAC);
                view.setText(s);
            }
        };

        registerReceiver(receiver, new IntentFilter("wat?"));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleAPI");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection to GoogleAPI suspended");
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection to GoogleAPI failed!");
    }

    @Override
    public void onResume(){
        super.onResume();
        // Log.i(TAG, "Receiver up");
        // registerReceiver(receiver, new IntentFilter("wat?"));
    }

    @Override
    public void onPause(){
        super.onPause();
        // Log.i(TAG, "Receiver down");
        // unregisterReceiver(receiver);
    }


    public static void increment(float alc){
        // send dummy data to the PhoneListenerService
        //Log.i(TAG, "Button pressed");

        alcohol += alc;

        //Log.i(TAG, "Sending alcohol content of " + alcohol);

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);
        putDataMapReq.getDataMap().putFloat("beer", alcohol);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    //            Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                }
            }
        });
    }

    public void toDrinksSelection(View v) {
        Intent i = new Intent(this, DrinksListActivity.class);
        BACActivity.increment(1.0f);
        startActivity(i);
    }
}
