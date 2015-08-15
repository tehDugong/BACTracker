package org.cs160.bactracker;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.ByteBuffer;

public class BACService extends WearableListenerService {
    private final String TAG = "BACService";

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        Log.i(TAG, "onMessageReceived triggered!");

        if( messageEvent.getPath().equalsIgnoreCase( "/bac" ) ) {
            byte[] bytes = messageEvent.getData();
            float bac = ByteBuffer.wrap(bytes).getFloat();
            Log.i(TAG, "Calculated BAC: " + bac);
            updateBAC(String.format("%.2f", bac));
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        Log.i(TAG, "onDataChanged triggered!");

        // Loop through the events and send a message
        // to the node that created the data item.
        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            String path = uri!=null ? uri.getPath() : null;
            Log.d(TAG, path);

            if (uri.toString().contains("/legal_limit")){
                DataItem item = event.getDataItem();
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                float legal_limit = dataMap.getFloat("legal_limit");
                updateLimit(legal_limit);
            }
        }
    }

    private void updateBAC(String message) {
        Log.i(TAG, "Sending broadcast for bac");
        getApplicationContext().sendBroadcast(new Intent("wat?")
                .putExtra("bac", message));
    }

    private void updateLimit(Float message){
        Log.i(TAG, "Sending broadcast for legal_limit");
        getApplicationContext().sendBroadcast(new Intent("wat?")
                .putExtra("limit", message));
    }

}
