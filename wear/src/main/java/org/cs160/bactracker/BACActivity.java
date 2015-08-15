package org.cs160.bactracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.sql.SQLException;
import java.util.ArrayList;

public class BACActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "BACActivity";
    public static GoogleApiClient mGoogleApiClient;
    public static float alcohol = 0.0f;
    private float limit;
    private float bac;
    private BroadcastReceiver receiver, menuReceiver;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "BACActivity started!");


        super.onCreate(savedInstanceState);
        DBAdapterWearable dbAdapter = new DBAdapterWearable(this);
        try {
            dbAdapter.openToWrite();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbAdapter.deleteAll();
        dbAdapter.close();
        Intent i = new Intent(this, SignalForMenu.class);
        startService(i);

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
        menuReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Menu Recieved");
            }
        };
        registerReceiver(receiver, new IntentFilter("wat?"));
        registerReceiver(menuReceiver, new IntentFilter("menuRetrieved"));

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
        unregisterReceiver(menuReceiver);
        mGoogleApiClient.disconnect();

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
        DBAdapterWearable dbAdapterWearable = new DBAdapterWearable(this);
        try {
            dbAdapterWearable.openToRead();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor c = dbAdapterWearable.getAllRows();
        ArrayList<String> names = new ArrayList<String>();
        while (!c.isAfterLast()) {
            String name = c.getString(DBAdapterWearable.COL_NAME);
            names.add(name);
        }
        dbAdapterWearable.close();
        try {
            dbAdapterWearable.openToWrite();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String name : names) {
            dbAdapterWearable.updateRow(name, 0);
        }
        dbAdapterWearable.close();
    }
    public void toDrinksSelection(View v) {

        Intent i = new Intent(this, MenuWearableList.class);
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
    public void toMenu(View v) {
        startActivity(new Intent(this, MenuWearableList.class));
    }
}