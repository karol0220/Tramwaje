package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Karol on 2016-12-20.
 */

public class TramFactory {


    public static Tram createTram(){
        return  new Tram();
    }

    public static Tram createTram(int id, String line, boolean lowFloor, Date date, LatLng currentPosition){
        return new Tram(id,line,lowFloor,date,currentPosition);
    }

    public static Tram createTram( String line, boolean lowFloor, Date date, LatLng currentPosition){
        return new Tram(line,lowFloor,date,currentPosition);
    }
}
