package com.example.karol.tramwaje;

import android.content.Context;
import com.google.android.gms.maps.model.Marker;
import java.util.List;
import java.util.Map;

/**
 * Created by Karol on 2016-11-29.
 */

public class CountTimeController {

    private ArrivalsTimeCounter countArrivalsTime;

    public CountTimeController(Context context){
        countArrivalsTime = new ArrivalsTimeCounter(context);
    }

    public List<String> getComingTrams( Map<String, List<Tram>> tramMap, Marker marker) {

        return countArrivalsTime.getComingTrams(tramMap,marker.getTitle());
   }
}
