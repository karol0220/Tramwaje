package com.example.karol.tramwaje;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Karol on 2016-11-17.
 */

/**
 * creates Trams objects from received data
 */
public class DataReceiverService extends IntentService {

    MapDrawerActivity activity;
    TramsDetector tramsDetector;

    /**
     * tram line is a key
     * list of trams is a value
     */
    Map<String, List<Tram>> tramMap;

    private final String DATA = "data";
    private final String JSON_DATA = "result";
    private final String LINE = "FirstLine";
    private final String LATITUDE = "Lat";
    private final String LONGITUDE = "Lon";
    private final String LOW_FLOOR = "LowFloor";
    private final String TIME = "Time";

    /**used to detect if got enough data*/
    private int TRAMS_QUANTITY_LIMIT = 10;

    private int previousTramCount;
    public DataReceiverService() {
        super("DataReceiverService");
        tramMap = new HashMap<>();
        previousTramCount = -1;
    }

    public DataReceiverService(MapDrawerActivity activity) {
        super("DataReceiverService");

        this.activity  = activity;
        tramMap = new HashMap<>();
        previousTramCount = -1;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String response = intent.getStringExtra(DATA);
        boolean gotNewData = false;

        String line;
        double lat;
        double lon;
        boolean lowFloor;
        SimpleDateFormat dt;
        Date date;

        try {
            JSONObject JSONRootObj = new JSONObject(response);
            JSONArray jsonArray = JSONRootObj.optJSONArray(JSON_DATA);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject tram = jsonArray.optJSONObject(i);

                line = tram.optString(LINE).replaceAll("\\s+", "");

                /**round because of not accurate data*/
                lat = Double.parseDouble(String.valueOf((Math.round(tram.optDouble(LATITUDE) * 10000)))) / 10000;
                lon = Double.parseDouble(String.valueOf((Math.round(tram.optDouble(LONGITUDE) * 10000)))) / 10000;
                lowFloor = tram.optBoolean(LOW_FLOOR);
                dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                date = dt.parse(tram.optString(TIME));

                if (tramMap.containsKey(line)) {
                    tramMap.get(line).add(TramFactory.createTram(line, lowFloor, date, new LatLng(lat, lon)));
                } else {
                    List<Tram> list = new ArrayList<>();
                    list.add(TramFactory.createTram(line, lowFloor, date, new LatLng(lat, lon)));
                    tramMap.put(line, list);
                }
            }

            /**sometimes response gives information about only half trams
             *do not process then */
            int tramCount = 0;

            for (Map.Entry<String, List<Tram>> entry : tramMap.entrySet()) {

                if (!entry.getValue().isEmpty())
                    tramCount += entry.getValue().size();
            }

            if (tramCount > 0)
                gotNewData = true;

            boolean gotEnoughData = true;

            if (previousTramCount == -1)
                previousTramCount = tramCount;
            else if (previousTramCount - tramCount > TRAMS_QUANTITY_LIMIT)
                gotEnoughData = false;


            if (gotNewData && gotEnoughData) {
                tramsDetector = TramsDetector.instance;

                if (tramsDetector == null)
                    tramsDetector = new TramsDetector(tramMap, this);

                else
                    tramsDetector.detect(tramMap);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        tramMap = new HashMap<>();
    }
}
