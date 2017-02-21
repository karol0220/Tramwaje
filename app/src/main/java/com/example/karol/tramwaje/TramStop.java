package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Karol on 2016-11-07.
 */

/**
 * Class represent tram stop
 */
public class TramStop {

    public final static String DIRECTION_FIRST_STOP = "first";
    public final static String DIRECTION_LAST_STOP = "last";
    public final static String DIRECTION_BOTH = "both";

    private String name;
    private LatLng position;
    private String[] lines;
    private String direction;

    public TramStop(){}

    public TramStop(String name,LatLng position){
       this.name = name;
        this.position = position;

    }
    public TramStop(String name,LatLng position, String[] lines, String direction){
        this.name = name;
        this.position = position;
        this.lines = lines;
        this.direction = direction;
    }

    public String[] getLines() {
        return lines;
    }
    public LatLng getPosition() {
        return position;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        return(((TramStop)obj).getName().equals(name));
    }

    public int hashCode() {
        return name.hashCode();
    }
}
