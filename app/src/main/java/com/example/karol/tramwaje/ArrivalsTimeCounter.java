package com.example.karol.tramwaje;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Karol on 2016-11-22.
 *
 *Estimates arrival time for each tram coming
 */

public class ArrivalsTimeCounter {

    private String name;

    /**table of lines available on stop*/
    private  String[] lines;

    /**map of all trams*/
    private Map<String, List<Tram>> tramMap;

    private List<ComingTram> comingTramsList;

     /**map of lines for stop*/
    private Map<String, List<TramStop>> stopMap;

    private DbController dbController;


    public ArrivalsTimeCounter(Context context){

        dbController = new DbController(context);

        stopMap = new HashMap<>();

        comingTramsList = new ArrayList<>();
    }

    /**represents tram with estimated arrival time. Comparable by time*/
    private class ComingTram implements Comparable<ComingTram>{

        Tram tram;
        Integer time;


        public Integer getTime() {
            return time;
        }

        public ComingTram(Tram tram, int time){
            this.tram = tram;
            this.time = time;
        }


        @Override
        public int compareTo(@NonNull ComingTram o) {
           return this.time.compareTo(o.getTime());
        }

        @Override
        public String toString() {
            String info = tram.getLine() + " ➡ "+ tram.getDirectionString()+ " za ok. " + time + " min";

            if (tram.isLowFloor())
                info += " niskopodłogowy";

            return info ;
        }
    }


    public List<String> getComingTrams( Map<String, List<Tram>> tramMap, String name){


        this.name = name;

        this.tramMap = tramMap;

        getLines();

        findComingTrams();

        Collections.sort(comingTramsList);

        List<String> results = new ArrayList<>();

        for (ComingTram ct: comingTramsList) {
            results.add(ct.toString());
        }

        stopMap.clear();

        comingTramsList.clear();

        return results;

    }

    /**get lines available for this stop*/
    private void getLines(){

        lines = dbController.getStopLines(name);

        for (int i = 0; i < lines.length; i++) {
            stopMap.put(lines[i],dbController.getLineStops(lines[i]));
        }
    }


    private void  findComingTrams(){

        for (int i = 0; i < lines.length; i++) {

            String lineNumber = lines[i];

            List<TramStop> line = stopMap.get(lineNumber);

            TramStop destinationStop = findDestinationStop(line);

            if (destinationStop!=null) {

                double distanceToFirstStopFromDestinationStop =  computeDistanceToFirstStopFromDestinationStop(line, destinationStop);

                /**find coming trams
                 * if there are trams on specified line*/
                if(tramMap.containsKey(lineNumber)){

                    for (Tram t : tramMap.get(lineNumber)) {

                        if (t.getDirection().equals(Tram.DIRECTION_UNKNOWN) || t.getDirection().equals(Tram.NO_DIRECTION))
                            continue;

                        int arrivalTime;

                        /**if tram didn't passed by current stop yet*/
                        if ((arrivalTime = estimateTime(t,line, destinationStop)) > 0) {

                            /**is tram going to the last stop and stop is on this direction*/
                            if (t.getDistanceToFirstEndStop() < distanceToFirstStopFromDestinationStop && t.getDirection().equals(Tram.DIRECTION_LAST_STOP)
                                    && !destinationStop.getDirection().equals(TramStop.DIRECTION_FIRST_STOP))
                                comingTramsList.add(new ComingTram(t, arrivalTime));

                            /**is tram going to the first stop and stop is on this direction*/
                            else if (t.getDistanceToFirstEndStop() > distanceToFirstStopFromDestinationStop && t.getDirection().equals(Tram.DIRECTION_FIRST_STOP)
                                    && !destinationStop.getDirection().equals(TramStop.DIRECTION_LAST_STOP))
                                    comingTramsList.add(new ComingTram(t, arrivalTime));

                        }
                    }
                }
            }
        }
    }

    private double computeDistanceToFirstStopFromDestinationStop(List<TramStop> line, TramStop destinationStop){

        List<LatLng> pathToFirstStop = new ArrayList<>();

        for (int j = 0; j < line.size(); j++) {

            pathToFirstStop.add(line.get(j).getPosition());

            if ((line.get(j).getName().equals(destinationStop.getName())))
                break;

        }

        return SphericalUtil.computeLength(pathToFirstStop);
    }

    private TramStop findDestinationStop(List<TramStop> line){

        TramStop destinationStop = null;

        for (int j = 0; j < line.size(); j++) {

            if ((line.get(j).getName().equals(name))) {
                destinationStop = line.get(j);
                break;
            }
        }

        return destinationStop;
    }

    /**returns time in minutes
     * returns negative value if tram already passed by destination stop or
     * estimation was unsuccessful*/
    private int estimateTime(Tram t, List<TramStop> line, TramStop destinationStop){

        int time = 0;

        TramStop startStop;

        if ((startStop = getStartStop(t))==null)
           return -1;

        if (t.getNextStopOnLine()!=null && t.getDirection().equals(Tram.DIRECTION_LAST_STOP))
           addTime(t.getCurrentPosition(),t.getNextStopOnLine().getPosition());
        else if(t.getPreviousStopOnLine()!=null && t.getDirection().equals(Tram.DIRECTION_FIRST_STOP))
            addTime(t.getCurrentPosition(),t.getPreviousStopOnLine().getPosition());


        boolean count = false;

        if (t.getDirection().equals(Tram.DIRECTION_LAST_STOP)){

            /**if tram already passed by destination stop, return*/
            if (line.indexOf(destinationStop) < line.indexOf(startStop))
                return -1;

            for (int i = 0; i < line.size()-1; i++) {

                if (line.get(i).equals(startStop))
                    count = true;

                if (line.get(i).equals(destinationStop))
                    break;

                if(count)
                    time += addTime(line.get(i).getPosition(), line.get(i + 1).getPosition());

            }
        }
        else if (t.getDirection().equals(Tram.DIRECTION_FIRST_STOP)){

            /**if tram already passed by destination stop, return*/
            if (line.indexOf(destinationStop) > line.indexOf(startStop))
                return -1;

            for (int i = line.size()-1; i > 0; i--) {

                if (line.get(i).equals(startStop))
                    count = true;

                if (line.get(i).equals(destinationStop))
                    break;

                if(count)
                    time += addTime(line.get(i).getPosition(), line.get(i - 1).getPosition());
            }
        }

        /**time left in milliseconds*/
        long timeLeft = (long) time*1000*60;

        /**time when tram was on this position*/
        long tramTime = t.getDate().getTime();
        long currentTime = new Date().getTime();
        long delayTime = currentTime - tramTime;
        long realTimeLeft = (timeLeft - delayTime);

        return Integer.parseInt(String.valueOf(realTimeLeft/60/1000));
    }

    private TramStop getStartStop(Tram tram){

        TramStop startStop = null;
         if (tram.getCurrentStop()!=null)
            startStop = tram.getCurrentStop();
         else if (tram.getNextStopOnLine()!=null && tram.getDirection().equals(Tram.DIRECTION_LAST_STOP))
            startStop = tram.getNextStopOnLine();
         else if (tram.getPreviousStopOnLine()!=null && tram.getDirection().equals(Tram.DIRECTION_FIRST_STOP))
             startStop = tram.getPreviousStopOnLine();

        return startStop;
    }

    private int addTime(LatLng position1, LatLng position2){

        int time = 0;

        double distance = SphericalUtil.computeDistanceBetween(position1, position2);

        if (distance < 400)
            time += 1;
        else if (distance < 1200)
            time += 2;
        else if (distance < 2000)
            time += 3;
        else
            time += 4;


        return time;
    }

}