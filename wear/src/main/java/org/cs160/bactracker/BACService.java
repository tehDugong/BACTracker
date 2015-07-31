package org.cs160.bactracker;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

public class BACService extends WearableListenerService {
    private LocalBroadcastManager broadcastManager;
    private final String TAG = "BACService";

    static final public String BACTRACKER_RESULT = "org.cs160.BACTracker" +
            ".backend.BACService.REQUEST_PROCESSED";
    static final public String BACTRACKER_MESSAGE = "org.cs160.BACTracker" +
            ".backend.BACService.BAC_MSG";

    public BACService() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        Log.i(TAG, "Listener triggered!");

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.i(TAG, "Unable to connect");
            return;
        }

        // Loop through the events and send a message
        // to the node that created the data item.
        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            Log.i(TAG, uri.toString());

            byte[] bytes = event.getDataItem().getData();
            float bac = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            Log.i(TAG, "Calculated BAC: " + bac);
            updateBAC(String.format("%.2f", bac));
        }
    }

    public void updateBAC(String message) {
        Intent intent = new Intent(BACTRACKER_RESULT);
        intent.putExtra(BACTRACKER_MESSAGE, message);
        broadcastManager.sendBroadcast(intent);
    }
}
