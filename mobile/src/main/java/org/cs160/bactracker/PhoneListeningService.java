package org.cs160.bactracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.ByteBuffer;
import java.util.Calendar;

public class PhoneListeningService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "PhoneListeningService";
    public static final String PREFS_NAME = "DrinksFile";
    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate(){
        super.onCreate();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        Log.i(TAG, "onMessageReceived triggered");

        SharedPreferences drinks = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = drinks.edit();

        if( messageEvent.getPath().equalsIgnoreCase( "/check_BAC" ) ) {
            Log.i(TAG, "Checking BAC");

            float bac = calculateBAC();
            // if bac is 0, restart the timer
            Calendar c= Calendar.getInstance();
            int current_time = c.get(Calendar.SECOND);
            editor.putInt("start_time", current_time);
            editor.commit();

            updateBAC(bac);

            //also check legal driving limit
            float legal_limit = drinks.getFloat("legal_limit", 0.08f);
            updateLimit(legal_limit);


        } else if (messageEvent.getPath().equalsIgnoreCase("/reset")) {
            editor.putFloat("alcohol", 0.0f);
            editor.commit();
            float bac = calculateBAC(); // This should return 0. If it doesn't, something is wrong
            updateBAC(bac);
        } else if (messageEvent.getPath().equalsIgnoreCase("/alcohol")) {
            byte[] bytes = messageEvent.getData();
            float alcohol = ByteBuffer.wrap(bytes).getFloat();
            Log.i(TAG, "Added alcohol: "+alcohol);
            editor.putFloat("alcohol", drinks.getFloat("alcohol", 0.0f)+alcohol);
            editor.commit();
            Log.i(TAG, "Alcohol sum: "+drinks.getFloat("alcohol", 0.0f));
            float bac = calculateBAC();
            updateBAC(bac);
        }else if (messageEvent.getPath().equalsIgnoreCase("/emergency")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel:911"));
            startActivity(intent);
        }else if (messageEvent.getPath().equalsIgnoreCase("/contact")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (messageEvent.getPath().equalsIgnoreCase("/taxi")){
            //try {
                //PackageManager pm = getPackageManager();
                //pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                String uri = "uber://?action=setPickup&pickup=my_location";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(uri));
                //Log.d(TAG, "in uber?");
                startActivity(intent);
            //} catch (PackageManager.NameNotFoundException e) {
                // No Uber app! Open Mobile Website.
                //Log.d(TAG, "no uber app?");
            //}
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

        // if alcohol is 0.0, reset start_time
        Calendar c= Calendar.getInstance();
        int current_time = c.get(Calendar.SECOND);

        if (alcohol == 0.0f){
            SharedPreferences.Editor editor = drinks.edit();
            editor.putInt("start_time", current_time);
        }

        int start_time = drinks.getInt("start_time", 0);
        float hours = ((float) current_time - start_time)/3600 ;

        hours = 1.0f;   // hard coded in

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

    private void updateBAC(final float bac){
        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Sending message: /bac");
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(
                        googleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.i(TAG, "Node: " + node);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            googleApiClient, node.getId(), "/bac",
                            ByteBuffer.allocate(4).putFloat(bac).array()).await();
                }
            }
        }).start();
    }

    private void updateLimit(final float legal_limit){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/legal_limit");
        putDataMapReq.getDataMap().putFloat("legal_limit", legal_limit);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
        pendingResult.await();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleAPI");
        /*
        DBAdapter dbJson = new DBAdapter(PhoneListeningService.this);
        try {
            JSONObject json = dbJson.getJSON();
            new SendToDataLayerThread("/path", json.toString()).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection to GoogleAPI suspended");
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection to GoogleAPI failed!");
    }
}
