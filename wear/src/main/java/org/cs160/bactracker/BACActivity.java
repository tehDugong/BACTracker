package org.cs160.bactracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
                    TextView view = (TextView) findViewById(R.id.current_BAC);
                    view.setText(s);
                }

                Float new_limit = intent.getFloatExtra("limit", -1.0f);
                if (new_limit>=0.0f){
                    Log.i(TAG, "Limit updated: "+new_limit);
                    limit = new_limit;
                }
            }
        };
        registerReceiver(receiver, new IntentFilter("wat?"));

        // check existing BAC
        sendMessage("/check_BAC", "");
        // check legal limit
        limit = 0.08f;
        sendMessage("/check_limit", "");

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
        mGoogleApiClient.disconnect();
    }


    public static void increment(float alc){
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

    private void sendMessage(final String path, final String text){
        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Sending message: "+path);
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(
                        mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.i(TAG, "Node: "+node);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
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
                        sendMessage("/reset", "");
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
}