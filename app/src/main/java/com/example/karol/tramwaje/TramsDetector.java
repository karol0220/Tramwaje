package com.example.karol.tramwaje;

import android.content.Context;

import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Karol on 2016-11-05.
 */

/**
 *Class determines where trams moved
 */
public class TramsDetector {

    public Map<String, List<Tram>> getTramMap() {
        return tramMap;
    }

    /**
     * tram line is a key
     * list of trams is a value
     */
    Map<String,List<Tram>> tramMap;

    /** current list of trams of specified line*/
    List<Tram> tramList;

    /**new list of trams with updated localization*/
    List<Tram> newTramList;

    TramPositionDetector tramPositionDetector;

    public static TramsDetector instance;

    /**used to set trams id*/
    int idCounter;


    public TramsDetector(Map<String,List<Tram>> tramMap, Context context){

        instance  = this;
        idCounter = 0;
        this.tramList = new ArrayList<>();
        this.tramMap = new HashMap<>();
        newTramList = new ArrayList<>();
        tramPositionDetector = new TramPositionDetector(context);

        initialize(tramMap);
    }

    /**
     * fills tramMap
     */
   private void initialize(Map<String,List<Tram>> tramMap){

       for (Map.Entry<String, List<Tram>> entry : tramMap.entrySet()) {

            /**create pair line - empty arrayList*/
           this.tramMap.put(entry.getKey(),new ArrayList<Tram>());

           Tram tr;

           /**fill tramList*/
           for (Tram t:entry.getValue()) {

               tr = TramFactory.createTram(idCounter,t.getLine(),t.isLowFloor(),t.getDate(),t.getCurrentPosition());
               tr = findPosition(tr);
               tr.estimateDirection();
               this.tramMap.get(entry.getKey()).add(tr);
               idCounter++;
           }
       }
       showTrams();
   }

    public void detect(Map<String,List<Tram>> tmpTramMap ) {

        List<Tram> tmpTramList;

        for (Map.Entry<String, List<Tram>> entry : tmpTramMap.entrySet()) {

            tramList.clear();

            /**get list of trams of specified line if they are present in tramMap*/
            if(tramMap.containsKey(entry.getKey()))
                 tramList.addAll(tramMap.get(entry.getKey()));

            /**add new line to the tramMap*/
            else {
                for (Tram t :entry.getValue()) {
                    t.setId(idCounter);
                    tramList.add(t);
                    idCounter++;
                }
                tramMap.put(entry.getKey(),tramList);
            }

            tmpTramList = entry.getValue();
            newTramList = new ArrayList<>();

        /** if get no data do not loop */
        if (tmpTramList.size() != 0) {

            for (Tram tram : tmpTramList)
                tram = findPosition(tram);

            /**remember trams from tramList that already detected new position and remove it after loop*/
            List<Integer> tramsToRemove = new ArrayList<>();

            /**
             * STEP 1
             * Detect trams that not moved
             */

                for (Tram tram : tramList) {

                    try {
                        Tram nearestTram = null;
                        for (Tram tmpTram : tmpTramList) {

                            /**In case of not accurate data. Tram seems to be going, but it is standing actually*/
                            if (SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(), tmpTram.getCurrentPosition()) < 25
                                    && tmpTram.isLowFloor() == tram.isLowFloor())
                                nearestTram = tmpTram;

                        }

                        /** if not found tram continue*/
                        if (nearestTram == null)
                            continue;

                        /**to avoid calculation mistakes sets info from previous position
                         * tram and nearestTram distances can be a bit different*/
                        setInfoAboutTrams(tram, tram);
                        tmpTramList.remove(nearestTram);
                        tramsToRemove.add(tram.getId());
                    }

                    catch (NullPointerException e){
                        e.printStackTrace();
                        tramsToRemove.add(tram.getId());
                    }
            }

            removeTrams(tramsToRemove);

            /**set detecting area to ensure closest trams will be detected first*/
            double detectingDistance = 0;

            /**
             * STEP 2
             * Find trams with set direction
             */

            while (detectingDistance <= 1000 && tmpTramList.size() != 0 && tramList.size() != 0) {

                for (Tram tram : tramList) {

                        /**continue if direction is not known*/
                        if (!tram.getDirection().equals(Tram.DIRECTION_FIRST_STOP) && !tram.getDirection().equals(Tram.DIRECTION_LAST_STOP)) {
                            continue;
                        }

                        double minDistance = 100000;
                        double tmpDistance;
                        Tram nearestTram = null;

                        for (Tram tmpTram : tmpTramList) {

                            if ((tmpDistance = SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(),
                                    tmpTram.getCurrentPosition())) < minDistance
                                    && SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(), tmpTram.getCurrentPosition()) <= detectingDistance
                                    && tmpTram.isLowFloor() == tram.isLowFloor()
                                    && tram.getDirection().equals(Tram.DIRECTION_FIRST_STOP)
                                    && tmpTram.getDistanceToFirstEndStop() <= tram.getDistanceToFirstEndStop()
                                    && tmpTram.getDistanceToLastEndStop() >= tram.getDistanceToLastEndStop()) {

                                minDistance = tmpDistance;
                                nearestTram = tmpTram;

                            }
                            /** find nearest tram that moves to the last stop*/
                            else if ((tmpDistance = SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(),
                                    tmpTram.getCurrentPosition())) < minDistance
                                    && SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(), tmpTram.getCurrentPosition()) <= detectingDistance
                                    && tmpTram.isLowFloor() == tram.isLowFloor()
                                    && tram.getDirection().equals(Tram.DIRECTION_LAST_STOP)
                                    && tmpTram.getDistanceToFirstEndStop() >= tram.getDistanceToFirstEndStop()
                                    && tmpTram.getDistanceToLastEndStop() <= tram.getDistanceToLastEndStop()) {

                                minDistance = tmpDistance;
                                nearestTram = tmpTram;
                            }
                        }

                        /** if not found tram continue*/
                        if (nearestTram == null)
                            continue;

                        setInfoAboutTrams(tram, nearestTram);
                        tmpTramList.remove(nearestTram);
                        tramsToRemove.add(tram.getId());
                }
                removeTrams(tramsToRemove);
                detectingDistance += 50;
            }

            /**
             * STEP 3
             * fix detecting
             * Problem is when two trams go by, detecting can go wrong.
             * One tram can not find new position and one new position is not detected
             *
             */
            if (tramList.size() != 0 && tmpTramList.size() != 0) {

                Tram badDetectedTram = TramFactory.createTram();
                Tram notFoundTram = TramFactory.createTram();

                double minDistance;
                double tmpDistance;

                for (Tram t : tramList) {

                    if  (!t.getDirection().equals(Tram.DIRECTION_FIRST_STOP) && !t.getDirection().equals(Tram.DIRECTION_LAST_STOP))
                        continue;

                    minDistance = 10000;

                    /** find nearest not found tram */
                    for (Tram tr : tmpTramList) {
                        if ((tmpDistance = SphericalUtil.computeDistanceBetween(t.getCurrentPosition(), tr.getCurrentPosition())) < minDistance) {

                            minDistance = tmpDistance;
                            notFoundTram = tr;
                        }
                    }

                    minDistance = 10000;

                    /**get nearest found tram*/
                    if (!t.getDirection().equals(Tram.DIRECTION_UNKNOWN)) {
                        for (Tram tr : newTramList) {
                            if ((tmpDistance = SphericalUtil.computeDistanceBetween(t.getCurrentPosition(), tr.getCurrentPosition())) < minDistance) {

                                minDistance = tmpDistance;
                                badDetectedTram = tr;

                            }
                        }
                    }

                    /**change trams if possible */
                    if (t.getDirection().equals(Tram.DIRECTION_FIRST_STOP) && badDetectedTram.getDirection().equals(Tram.DIRECTION_LAST_STOP) && t.getDistanceToFirstEndStop() > badDetectedTram.getDistanceToFirstEndStop()
                            && t.getDistanceToFirstEndStop() < notFoundTram.getDistanceToFirstEndStop()) {

                        changeTrams(t, badDetectedTram, notFoundTram);
                        tmpTramList.remove(notFoundTram);
                        tramsToRemove.add(t.getId());
                        newTramList.add(t);

                     /**change trams if possible */
                    } else if (t.getDirection().equals(Tram.DIRECTION_LAST_STOP)  && badDetectedTram.getDirection().equals(Tram.DIRECTION_FIRST_STOP) && t.getDistanceToFirstEndStop() < badDetectedTram.getDistanceToFirstEndStop()
                            && t.getDistanceToFirstEndStop() > notFoundTram.getDistanceToFirstEndStop()) {

                        changeTrams(t, badDetectedTram, notFoundTram);
                        tmpTramList.remove(notFoundTram);
                        tramsToRemove.add(t.getId());
                        newTramList.add(t);
                    }
                }
            }

            removeTrams(tramsToRemove);

            /**
             *STEP 4
             * Identify rest of trams with not set direction
             */
            detectingDistance = 0;

            while (detectingDistance <= 1000 && tmpTramList.size() != 0 && tramList.size() != 0) {

                for (Tram tram : tramList) {

                    if (tram.getDirection().equals(Tram.DIRECTION_UNKNOWN) || tram.getDirection().equals(Tram.NO_DIRECTION)){

                    double minDistance = 100000;
                    double tmpDistance;
                    Tram nearestTram = null;

                    for (Tram tmpTram : tmpTramList) {

                        /**
                         * find nearest tram when direction is unknown
                         */
                        if ((tmpDistance = SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(), tmpTram.getCurrentPosition())) < minDistance
                                && SphericalUtil.computeDistanceBetween(tram.getCurrentPosition(), tmpTram.getCurrentPosition()) <= detectingDistance
                                && tmpTram.isLowFloor() == tram.isLowFloor()) {

                            minDistance = tmpDistance;
                            nearestTram = tmpTram;
                        }
                    }

                    /**
                     * if not found tram in set area continue
                     */
                    if (nearestTram == null)
                        continue;

                    setInfoAboutTrams(tram, nearestTram);
                    tmpTramList.remove(nearestTram);
                    tramsToRemove.add(tram.getId());

                    }
                }

                removeTrams(tramsToRemove);
                detectingDistance += 50;
            }

        }

        /**
         * add left trams from tramList to the newTramList if they have estimated direction
         */
        for (Tram t:tramList) {
            if (t.getNotDetectedCounter()<2 &&(t.getDirection().equals(Tram.DIRECTION_FIRST_STOP) ||t.getDirection().equals(Tram.DIRECTION_LAST_STOP) )){
                newTramList.add(t);
                t.setNotDetectedCounter(t.getNotDetectedCounter()+1);
            }
        }

        /**
         * add new trams that have been not recognized to the newTramlist
         */

        if (tmpTramList.size() != 0) {

            for (Tram t : tmpTramList) {
                t.setId(idCounter);
                idCounter++;
                t.estimateDirection();
                newTramList.add(t);
            }
        }

        tramMap.get(entry.getKey()).clear();
        tramMap.get(entry.getKey()).addAll( newTramList);
        }

         showTrams();
    }

    /**sets new information about tram */
    private void setInfoAboutTrams(Tram destination, Tram source){

        destination.setDistanceToFirstEndStop(source.getDistanceToFirstEndStop());
        destination.setDistanceToLastEndStop(source.getDistanceToLastEndStop());
        destination.setCurrentPosition(source.getCurrentPosition());
        destination.setDate(source.getDate());

        if (source.getCurrentStop()!=null){
            destination.setCurrentStop(source.getCurrentStop());
            destination.setPreviousStopOnLine(null);
            destination.setNextStopOnLine(null);
        }
        else if (source.getNextStopOnLine()!=null){
            destination.setCurrentStop(null);
            destination.setPreviousStopOnLine(source.getPreviousStopOnLine());
            destination.setNextStopOnLine(source.getNextStopOnLine());
        }

        destination.estimateDirection();
        destination.setNotDetectedCounter(0);
        newTramList.add(destination);
    }

    /**
     * remove detected trams from tramList to not search new positions again
     */
    private void removeTrams(List<Integer> tramsToRemove){

        for (Integer i: tramsToRemove ) {
            for (int j = 0; j < tramList.size(); j++) {
                if (tramList.get(j).getId()==i)
                    tramList.remove(tramList.get(j));
            }
        }
        tramsToRemove.clear();
    }

    /**
     * change trams if detecting gone wrong
     * @param t  tram to change
     * @param badDetectedTram tram to change
     */
    private void changeTrams(Tram t, Tram badDetectedTram,Tram notFoundTram ){

        setInfoAboutTrams(t,badDetectedTram);
        badDetectedTram.removeLastDistancesFromList();
        setInfoAboutTrams(badDetectedTram,notFoundTram);

    }

    private Tram findPosition(Tram tram){
        tram = tramPositionDetector.findPosition(tram);
        return tram;
    }

    private void showTrams(){

      final  MapDrawerActivity activity = (MapDrawerActivity) MapDrawerActivity.instance;

        /**try block needed for unit tests only*/
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.showTrams(tramMap);
                }
            });
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
