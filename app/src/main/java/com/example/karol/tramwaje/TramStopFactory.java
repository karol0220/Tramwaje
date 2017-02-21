package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Karol on 2016-12-16.
 */

public class TramStopFactory {

    public static TramStop createStop( String name, LatLng position, String[] lines, String direction){
        return new TramStop(name, position, lines,  direction);
    }

    public static TramStop createStop( ){
        return new TramStop();
    }
}
