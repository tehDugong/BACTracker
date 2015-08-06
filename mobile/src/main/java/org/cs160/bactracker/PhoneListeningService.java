package org.cs160.bactracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PhoneListeningService extends WearableListenerService {

    private final String TAG = "PhoneListeningService";
    private final String PATH = "/bac";
    public static final String PREFS_NAME = "DrinksFile";

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        Log.i(TAG, "onMessageReceived triggered");

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.i(TAG, "Unable to connect");
            return;
        }

        SharedPreferences drinks = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = drinks.edit();

        if( messageEvent.getPath().equalsIgnoreCase( "/check_BAC" ) ) {
            Log.i(TAG, "Checking BAC");

            float bac = calculateBAC();

            /*
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);
            putDataMapReq.getDataMap().putFloat("bac", bac);
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(googleApiClient, putDataReq);

            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(final DataApi.DataItemResult result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                    }
                }
            });
            */
            updateBAC(PATH, bac);


        } else if (messageEvent.getPath().equalsIgnoreCase("/reset")) {
            editor.putFloat("alcohol", 0.0f);

            // Note that reset is called when "Start drinking" is pressed
            Calendar c= Calendar.getInstance();
            int current_time = c.get(Calendar.SECOND);
            editor.putInt("start_time", current_time);
            editor.commit();

            float bac = calculateBAC(); // This should return 0. If it doesn't, something is wrong

            // update new BAC and send result to watch
            /*
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);

            putDataMapReq.getDataMap().putFloat("bac", bac);
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(googleApiClient, putDataReq);

            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(final DataApi.DataItemResult result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                    }
                }
            });
            */

            updateBAC(PATH, bac);
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        Log.i(TAG, "onDataChanged triggered!");

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.i(TAG, "Unable to connect");
            return;
        }

        SharedPreferences drinks = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = drinks.edit();

        // Loop through the events and send a message
        // to the node that created the data item.
        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            Log.i(TAG, uri.toString());

            if (uri.toString().contains("/alcohol")) {
                DataMapItem item = DataMapItem.fromDataItem(event.getDataItem());
                Log.i(TAG, "Datamap: " + item.getDataMap().toString());
                float beer = item.getDataMap().getFloat("beer");
                Log.i(TAG, "Alcohol added: " + beer);
                editor.putFloat("alcohol", drinks.getFloat("alcohol", 0.0f) + beer);
                editor.commit();

                float bac = calculateBAC();

                // update new BAC and send result to watch
                updateBAC(PATH, bac);

                /*PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH);
                putDataMapReq.getDataMap().putFloat("bac", bac);
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

                PendingResult<DataApi.DataItemResult> pendingResult =
                        Wearable.DataApi.putDataItem(googleApiClient, putDataReq);

                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(final DataApi.DataItemResult result) {
                        if (result.getStatus().isSuccess()) {
                            Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                        }
                    }
                });
                */
            }
        }
    }

    private float calculateBAC(){

        // calculate new BAC
        SharedPreferences drinks = getSharedPreferences(PREFS_NAME, 0);

        boolean gender = drinks.getBoolean("gender", true);
        float ratio = 0.0f;
        if (gender){
            ratio = 0.73f;
        } else{
            ratio = 0.66f;
        }
        int weight = drinks.getInt("weight", 1);
        float alcohol = drinks.getFloat("alcohol", 0.0f);
        int start_time = drinks.getInt("start_time", 0);

        Calendar c= Calendar.getInstance();
        int current_time = c.get(Calendar.SECOND);

        float hours = ((float) current_time - start_time)/3600 ;

        hours = 0.5f;   // hard coded in

        Log.i(TAG, "Ratio: "+ratio);
        Log.i(TAG, "Weight: "+weight);
        Log.i(TAG, "alcohol: "+alcohol);
        Log.i(TAG, "hours: "+hours);

        float bac = (alcohol * (5.14f/weight) * ratio) - 0.015f * hours;

        if (bac < 0.0f){
            Log.i(TAG, "Sober!");
            bac = 0.0f;
        }

        Log.i(TAG, "BAC calculated: " + bac);

        return bac;
    }

    private void updateBAC(final String path, final float bac){
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.i(TAG, "Unable to connect");
            return;
        }

        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Sending message: "+path);
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(
                        googleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.i(TAG, "Node: " + node);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            googleApiClient, node.getId(), path,
                            ByteBuffer.allocate(4).putFloat(bac).array()).await();
                }
            }
        }).start();
    }
}
