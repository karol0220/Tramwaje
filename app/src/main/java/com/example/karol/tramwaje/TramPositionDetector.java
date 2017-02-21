package com.example.karol.tramwaje;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karol on 2016-11-07.
 *
 * Class finds position of tram according to line stops
 * and count distances to the first and last stop
 *
 * e.g. detects that tram a is between stop X and stop Y
 */

class TramPositionDetector {

    private DbController dbController;
     /**list of stops of specified line*/
    private List<TramStop> lineList;
    private final int STOP_AREA = 40;
    private List<TramStop> endStopList;
    private Tram tram;
    private TramStop nearestStop;
    private TramStop previousStop;
    private TramStop nextStop;

    public TramPositionDetector(Context context){
        lineList = new ArrayList<>();
        dbController = new DbController(context);
        endStopList = new ArrayList<>();
    }


    public Tram findPosition(Tram t){

        tram = t;
        String line = tram.getLine();

        getLineStops(line);

        /**if there is no line for tram in database*/
        if (lineList.size()==0)
            return tram;

        setEndStops();
        findNearestStop();

        /**in case when there is no data about tram position*/
        if (lineList.indexOf(nearestStop) == -1)
            return tram;

        /**  is tram on stop*/
        if (SphericalUtil.computeDistanceBetween(nearestStop.getPosition(), tram.getCurrentPosition()) < STOP_AREA){
           setInfoAboutCurrentStop();
            return tram;
        }

        /**
         * Tram is between two stops.
         * Determines between which
         */

        findSecondNearestStop();

        return tram;
    }

    private void getLineStops(String line){

        /**if empty, got list of stops for line*/
        if (lineList.size()==0)
            lineList = dbController.getLineStops(line);

        else {
            /**if current list is not for this line get proper*/
            String[] l = lineList.get(0).getLines();
            if ( !l[0].equals(line))
                lineList = dbController.getLineStops(line);
        }
    }

    /**sets the same object of end stop for each tram in the same line like in flyweight pattern*/
    private void setEndStops(){

            TramStop firstStop = null;
            TramStop lastStop = null;

            for (TramStop stop: endStopList) {
                if (stop.equals(lineList.get(0)))
                    firstStop = stop;
                else if (stop.equals(lineList.get(lineList.size()-1)))
                    lastStop = stop;
            }

            if (firstStop!=null)
                tram.setFirstEndStop(firstStop);

            else {
                endStopList.add(lineList.get(0));
                tram.setFirstEndStop(lineList.get(0));
            }

            if (lastStop!=null)
                tram.setLastEndStop(lastStop);

            else {
                endStopList.add(lineList.get(lineList.size()-1));
                tram.setLastEndStop(lineList.get(lineList.size()-1));
            }
    }

    private void findNearestStop(){

        double minDistance = 1000000;
        double tmpDistance;
        nearestStop = TramStopFactory.createStop();

        for (TramStop stop: lineList) {

            tmpDistance = SphericalUtil.computeDistanceBetween(stop.getPosition(), tram.getCurrentPosition());

            if (tmpDistance < minDistance){

                minDistance = tmpDistance;
                nearestStop = stop;
            }
        }
    }

    private void setInfoAboutCurrentStop(){
        /**use new tram object to calculate distance to avoid calculation mistakes*/
        tram.setDistanceToFirstEndStop(countDistanceToFirstEndStop(nearestStop,TramFactory.createTram()));
        tram.setDistanceToLastEndStop(countDistanceToLastEndStop(nearestStop,TramFactory.createTram()));
        tram.setCurrentStop(nearestStop);
    }

    private void findSecondNearestStop(){

        /**if nearest stop is first stop of the line - previous stop does not exist*/
        if (lineList.indexOf(nearestStop)==0){
            nextStop = lineList.get(lineList.indexOf(nearestStop)+1);
            tram.setDistanceToFirstEndStop(countDistanceToFirstEndStop(nearestStop,tram));
            tram.setDistanceToLastEndStop(countDistanceToLastEndStop(nextStop,tram));
            tram.setPreviousStopOnLine(nearestStop);
            tram.setNextStopOnLine(nextStop);
        }

        /**if nearest stop is last stop of the line - next stop does not exist*/
        else if (lineList.indexOf(nearestStop)==lineList.size()-1){
            previousStop = lineList.get(lineList.indexOf(nearestStop)-1);

            tram.setDistanceToFirstEndStop(countDistanceToFirstEndStop(previousStop,tram));
            tram.setDistanceToLastEndStop(countDistanceToLastEndStop(nearestStop,tram));
            tram.setPreviousStopOnLine(previousStop);
            tram.setNextStopOnLine(nearestStop);
        }

        /**
         * Count distances to the end stops for next and previous stop and sum them for each stop
         * Tram is closer to the stop that has lesser sum
         */
        else{
            /** Do not now if tram is before or beyond nearest stop. Get next and previous stop */
            previousStop = lineList.get(lineList.indexOf(nearestStop)-1);
            nextStop = lineList.get(lineList.indexOf(nearestStop)+1);


            double distanceToFirstStopFromPreviousStop = countDistanceToFirstEndStop(previousStop, tram);
            double distanceToLastStopFromPreviousStop = countDistanceToLastEndStop(nearestStop, tram);
            double distanceToFirstStopFromNextStop = countDistanceToFirstEndStop(nearestStop, tram);
            double distanceToLastStopFromNextStop = countDistanceToLastEndStop(nextStop, tram);

            if ((distanceToFirstStopFromNextStop + distanceToLastStopFromNextStop) < (distanceToFirstStopFromPreviousStop + distanceToLastStopFromPreviousStop)) {
                tram.setNextStopOnLine(nextStop);
                tram.setPreviousStopOnLine(nearestStop);
                tram.setDistanceToFirstEndStop(distanceToFirstStopFromNextStop);
                tram.setDistanceToLastEndStop(distanceToLastStopFromNextStop);
            } else {
                tram.setNextStopOnLine(nearestStop);
                tram.setPreviousStopOnLine(previousStop);
                tram.setDistanceToFirstEndStop(distanceToFirstStopFromPreviousStop);
                tram.setDistanceToLastEndStop(distanceToLastStopFromPreviousStop);
            }
        }
    }



    private double countDistanceToFirstEndStop(TramStop stop, Tram tram){

        List<LatLng> path = new ArrayList<>();
        for (int i = 0; i <= lineList.indexOf(stop); i++) {
            path.add(lineList.get(i).getPosition());
        }

        if (tram.getCurrentPosition()!=null)
            path.add(tram.getCurrentPosition());

        return SphericalUtil.computeLength(path);

    }

    private double countDistanceToLastEndStop(TramStop stop, Tram tram){

        List<LatLng> path = new ArrayList<>();

        if (tram.getCurrentPosition()!=null)
              path.add(tram.getCurrentPosition());

        for (int i = lineList.indexOf(stop); i < lineList.size(); i++) {
            path.add(lineList.get(i).getPosition());
        }

        return SphericalUtil.computeLength(path);
    }

}
