package org.cs160.bactracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CountDrinks extends Activity {
    private TextView drinkName, drinkCount;
    private ImageView drinkImage;
    private String name;
    private int count, index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_drinks);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                drinkName = (TextView)stub.findViewById(R.id.drinkName);
                drinkCount = (TextView)stub.findViewById(R.id.drinkCount);
                drinkImage = (ImageView)stub.findViewById(R.id.drinkImage);
                Intent i = getIntent();
                Bundle bundle = (Bundle)i.getExtras();
                name = (String)bundle.get("drinkName");
                count = (int) bundle.get("drinkCount");
                index = (int) bundle.get("index");
                drinkName.setText(name);
                drinkCount.setText(Integer.toString(count));
            }
        });
    }
    public void plusButtonClick(View v) {
        increment(1);
    }
    public void minusButtonClick(View v) {
        increment(-1);
    }
    public void increment(int inc) {
        if(count+inc < 0)
            return;
        this.count += inc;
        this.drinkCount.setText(Integer.toString(this.count));

    }
}
