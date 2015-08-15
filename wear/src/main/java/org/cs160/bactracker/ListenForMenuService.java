package org.cs160.bactracker;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.sql.SQLException;
import java.util.ArrayList;

public class ListenForMenuService extends WearableListenerService {
    public final String TAG = "ListenForMenu";
    private DBAdapterWearable dbAdapter;

    @Override
    public void onDataChanged(DataEventBuffer events) {
        for (DataEvent event : events) {
            final Uri uri = event.getDataItem().getUri();
            final String path = uri != null ? uri.getPath() : null;
            Log.d(TAG, path);
            if ("/menu".equals(path)) {
                Log.d(TAG, "check");
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                ArrayList<DataMap> dataMaps = map.getDataMapArrayList("dataMaps");
                dbAdapter = DBAdapterWearable.getInstance(getApplicationContext());
                try {
                    dbAdapter.openToWrite();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < dataMaps.size(); i++) {
                    DataMap m = dataMaps.get(i);
                    if (! dbAdapter.checkExists(DBAdapterWearable.KEY_NAME, m.getString("name"))) {
                        dbAdapter.insertRow(m.getString("name"),
                                m.getString("ingredients"),
                                m.getInt("calories"),
                                m.getDouble("abv"),
                                0, "null", m.getString("category"));
                    } else {
                        Log.d(TAG, m.getString("name") + " "
                                + m.getString("ingredients") + " "
                                + Integer.toString(m.getInt("calories")) + " "
                                + Double.toString(m.getDouble("abv")) + " "
                                + m.getString("category"));
                    }
                }

                Log.d(TAG, "...About to Send Broadcast");
                Intent intent = new Intent("menuRetrieved");
                Intent intent2 = new Intent("menu");
                sendBroadcast(intent);
                sendBroadcast(intent2);
                dbAdapter.printAllRows(TAG);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
    }
}
