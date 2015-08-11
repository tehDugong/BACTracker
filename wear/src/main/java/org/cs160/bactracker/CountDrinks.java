package org.cs160.bactracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class CountDrinks extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView drinkName, drinkCount;
    private Button plusButton, minusButton;
    private ImageView drinkImage;
    private String name;
    private int count, index;
    private DrinkItem drink;
    public static GoogleApiClient mGoogleApiClient;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_drinks);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                drinkName = (TextView) stub.findViewById(R.id.drinkName);
                drinkCount = (TextView) stub.findViewById(R.id.drinkCount);
                drinkImage = (ImageView) stub.findViewById(R.id.drinkImage);
                Intent i = getIntent();
                drink = i.getParcelableExtra("drink");
                name = drink.getName();
                count = drink.getCount();
                drinkName.setText(name);
                drinkCount.setText(Integer.toString(count));
                drinkImage.setImageBitmap(drink.getImage());
                plusButton = (Button) stub.findViewById(R.id.plusButton);
                minusButton = (Button) stub.findViewById(R.id.minusButton);
                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increment(1);
                    }
                });
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increment(-1);
                    }
                });
            }
        });
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
    }
    public void plusButtonClick(View v) {
        increment(1);
    }
    public void minusButtonClick(View v) {
        increment(-1);
    }
    public void increment(int inc) {
        if (inc + count < 0)
            return;
        count += inc;
        drinkCount.setText(Integer.toString(count));
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/alcohol");
        float alcohol = drink.getAlcoholContent() * inc;
        putDataMapReq.getDataMap().putFloat("beer", alcohol);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    // Log.i(TAG, "Data item set: " + result.getDataItem().getUri());
                }
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
