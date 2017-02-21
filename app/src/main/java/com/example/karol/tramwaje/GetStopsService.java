package com.example.karol.tramwaje;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Karol on 2016-12-15.
 */

/**
 * Used to get stops in background to show on map
 */
public class GetStopsService extends IntentService{

    List<String> linesToShow;
    List<TramStop> stopList;
    Set<TramStop> stopSet;

    DbController dbController;

    public GetStopsService(){
        super("GetStopsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        stopList = new ArrayList<>();
        stopSet = new HashSet<>();
        dbController = new DbController(this);
        linesToShow  = intent.getStringArrayListExtra("lines");

            for (String s:linesToShow)
                stopSet.addAll(dbController.getLineStops(s));

        stopList.addAll(stopSet);
            showStops();

    }

    private void showStops(){

        final MapDrawerActivity activity = (MapDrawerActivity) MapDrawerActivity.instance;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.showStops(stopList);
            }
        });
    }
}
