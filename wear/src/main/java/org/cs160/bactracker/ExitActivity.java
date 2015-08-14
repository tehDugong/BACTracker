package org.cs160.bactracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;



public class ExitActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static GoogleApiClient mGoogleApiClient;
    private final String TAG = "ExitActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        boolean safe = extras.getBoolean("safe");
        if (safe) {
            setContentView(R.layout.activity_exit_safe);
        } else{
            setContentView(R.layout.activity_exit_unsafe);
        }

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
        Log.i(TAG, "ExitActivity.onCreate finished");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void messageEmergency(View view){

        sendMessage("/emergency",null);
    }
    public void messageContact(View view){
        sendMessage("/contact",null);

    }
    public void messageTaxi(View view){
        sendMessage("/taxi", null);
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
