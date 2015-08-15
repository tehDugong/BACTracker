package org.cs160.bactracker;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.sql.SQLException;
import java.util.ArrayList;

public class MenuSignalListenerService extends WearableListenerService {
    private final String TAG = "MenuSignalListener";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i(TAG, "onMessageReceived triggered!");
        Log.d(TAG, messageEvent.getPath());
        if (messageEvent.getPath().equals("GET_MENU")) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle connectionHint) {
                            Log.d(TAG, "Connected");
                            final PutDataMapRequest putRequest = PutDataMapRequest.create("/menu");
                            final DataMap map = putRequest.getDataMap();
                            DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
                            try {
                                dbAdapter.openToRead();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            ArrayList<DataMap> dataMaps = new ArrayList<DataMap>();
                            Cursor c = dbAdapter.getAllRows();
                            while (!c.isAfterLast()) {
                                DataMap m = new DataMap();
                                m.putString("name", c.getString(0));
                                m.putString("ingredients", c.getString(1));
                                m.putInt("calories", c.getInt(3));
                                m.putDouble("abv", c.getInt(2));
                                m.putString("category", c.getString(4));
                                dataMaps.add(m);
                                c.moveToNext();
                            }
                            map.putDataMapArrayList("dataMaps", dataMaps);
                            Long time = System.currentTimeMillis();
                            map.putLong("time", time);
                            Log.d(TAG, putRequest.asPutDataRequest().getUri().getPath());
                            Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
                            dbAdapter.close();
                    }
                        @Override
                        public void onConnectionSuspended(int cause) {
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.d(TAG, "Connection Failed");
                            Log.d(TAG, result.toString());
                        }
                    })
                    .addApi(Wearable.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }
}
