package org.cs160.bactracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class BACActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "BACActivity";
    public static GoogleApiClient mGoogleApiClient;
    public static float alcohol = 0.0f;
    private float limit;
    private float bac;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "BACActivity started!");

        super.onCreate(savedInstanceState);

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
                String s = intent.getStringExtra("bac");
                if (s != null) {
                    Log.i(TAG, "Found BAC: " + s);
                    bac = Float.parseFloat(s);
                    TextView view = (TextView) findViewById(R.id.current_BAC);
                    view.setText(s);
                    Log.i(TAG, color_limit());
                    view.setTextColor(Color.parseColor(color_limit()));
                }

                Float new_limit = intent.getFloatExtra("limit", -1.0f);
                if (new_limit>0.0f){
                    Log.i(TAG, "Limit updated: "+new_limit);
                    limit = new_limit;
                }
            }
        };
        registerReceiver(receiver, new IntentFilter("wat?"));

        // check existing BAC
        bac = 0.00f;
        sendMessage("/check_BAC", null);
        // check legal limit
        limit = 0.08f;
        sendMessage("/check_limit", null);

        setContentView(R.layout.activity_bac);
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
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
        mGoogleApiClient.disconnect();
    }


    public static void increment(float alc) {
        // send dummy data to the PhoneListenerService
        // Log.i(TAG, "Button pressed");

        alcohol += alc;

        // Log.i(TAG, "Sending alcohol content of " + alcohol);

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/alcohol");
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

    private void sendMessage(final String path, final byte[] data){
        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Sending message: "+path);
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(
                        mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.i(TAG, "Node: "+node);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, data ).await();
                }
            }
        }).start();
    }

    public void reset(View v){
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to reset?")
                .setTitle("Reset?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMessage("/reset", null);
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

    public void toDrinksSelection(View v) {
        Intent i = new Intent(this, DrinksListActivity.class);
        startActivity(i);
    }

    private String color_limit(){
        // returns hex color code based on ratio between current bac and legal limit

        int red;
        int green;
        int blue = 0;
        double ratio = bac/limit;
        if (ratio > 1.0){
            ratio = 1.0;
        }

        if (ratio < 0.5f){
            red = (int) Math.floor(ratio*255);
            green = 255;
        } else {
            red = 255;
            green = 255 - ((int) Math.floor(ratio * 255));
        }
        return String.format("#%02x%02x%02x", red, green, blue);
   }
}