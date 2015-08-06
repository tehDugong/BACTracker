package org.cs160.bactracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class StartDrinkingActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "StartDrinkingActivity";
    private GoogleApiClient mGoogleApiClient;
    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_drinking);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
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

    public void goToBACStart(View v) {
        Intent i = new Intent(this, BACActivity.class);
        i.putExtra("startedDrinking", true);
        this.startActivity(i);

        sendMessage("/reset", "");

        Button start_button = (Button) findViewById(R.id.start_button);
        Button continue_button = (Button) findViewById(R.id.continue_button);
        Button reset_button = (Button) findViewById(R.id.reset_button);

        start_button.setVisibility(View.GONE);
        continue_button.setVisibility(View.VISIBLE);
        reset_button.setVisibility(View.VISIBLE);
    }

    public void goToBACContinue(View v){
        Intent i = new Intent(this, BACActivity.class);
        i.putExtra("startedDrinking", true);
        this.startActivity(i);
    }

    public void goToBACReset(View v){
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to reset?")
                .setTitle("Reset?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMessage("/reset", "");

                        Button start_button = (Button) findViewById(R.id.start_button);
                        Button continue_button = (Button) findViewById(R.id.continue_button);
                        Button reset_button = (Button) findViewById(R.id.reset_button);

                        start_button.setVisibility(View.VISIBLE);
                        continue_button.setVisibility(View.GONE);
                        reset_button.setVisibility(View.GONE);
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

    private void sendMessage(final String path, final String text){
        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Sending message: "+text);
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
}
