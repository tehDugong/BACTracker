package org.cs160.bactracker;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

public class PhoneListeningService extends WearableListenerService {

    private final String TAG = "PhoneListeningService";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

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

            // Get the node id from the host value of the URI
            // String nodeId = uri.getHost();
            // Set the data of the message to be the bytes of the URI
            // byte[] payload = uri.toString().getBytes();

            // Send the RPC
            //Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
            //        DATA_ITEM_RECEIVED_PATH, payload);
        }
    }
}
