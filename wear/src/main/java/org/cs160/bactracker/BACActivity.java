package org.cs160.bactracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BACActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "BACActivity";
    private GoogleApiClient mGoogleApiClient;
    private final String PATH = "/alcohol";
    private String mobileNodeId;

    private float bac = 0.0f;
    private float alcohol = 0.0f;

    private BroadcastReceiver receiver;
    static final public String BACTRACKER_RESULT = "org.cs160.BACTracker" +
            ".backend.BACService.REQUEST_PROCESSED";
    static final public String BACTRACKER_MESSAGE = "org.cs160.BACTracker" +
            ".backend.BACService.BAC_MSG";

    public static final String PREFS_NAME = "DrinksFile";

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

        // set mobileNodeId
        getRemoteNodeId();

        // broadcast listener
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(BACTRACKER_MESSAGE);
                TextView view = (TextView) findViewById(R.id.current_BAC);
                view.setText(s);
            }
        };
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

    public void increment(View view){
        // send dummy data to the PhoneListenerService
        Log.i(TAG, "Button pressed");

        alcohol += 0.6f;    // equivalent of drinking a beer

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);
        putDataMapReq.getDataMap().putFloat("beer", alcohol);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                }
            }
        });
    }

    private Uri getUriForAlcohol() {
        return new Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME)
                .authority(mobileNodeId).path(PATH).build();
    }

    /*
    private String getLocalNodeId() {
        NodeApi.GetLocalNodeResult nodeResult =
                Wearable.NodeApi.getLocalNode(mGoogleApiClient).await();
        return nodeResult.getNode().getId();
    }
    */


    private void getRemoteNodeId() {
        PendingResult<NodeApi.GetConnectedNodesResult> nodesResult =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);

        nodesResult.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                // this will NOT fail gracefully, but don't have time to fix right now
                mobileNodeId = getConnectedNodesResult.getNodes().get(0).getId();
                Log.i(TAG, "Node(s) succesfully retrieved: " + mobileNodeId);
            }
        });
    }

}
