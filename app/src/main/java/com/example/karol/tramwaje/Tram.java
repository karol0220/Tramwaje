package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Karol on 2016-10-26.
 */

public class Tram {

   public final static String DIRECTION_FIRST_STOP = "0";
   public final static String DIRECTION_LAST_STOP = "1";
   public final static String DIRECTION_UNKNOWN = "nieznany";
   public final static String NO_DIRECTION = "stoi";
   public final int END_STOP_AREA = 100;

    private int id;
    private String line;
    private boolean lowFloor;
    private Date date;
    private LatLng currentPosition;
    private String direction;

    /**firstEndStop is first stop on the line list
     * lastEndStop is last
     */
    private TramStop firstEndStop;
    private TramStop lastEndStop;

    private double distanceToFirstEndStop;
    private double distanceToLastEndStop;

    /**if tam is on stop it is set*/
    private TramStop currentStop;

    /**if tram is between two stops they are set
     * It not determines direction!*/
    private TramStop previousStopOnLine;
    private TramStop nextStopOnLine;

    private List<Double> distanceToFirstStopHistory;
    private List<Double> distanceToLastStopHistory;

    /**times that tram has not been detected*/
    private int notDetectedCounter;

    public Tram(){
        distanceToFirstEndStop = -1;
        distanceToLastEndStop = -1;
        distanceToFirstStopHistory = new ArrayList<>();
        distanceToLastStopHistory = new ArrayList<>();

        currentStop = null;
        previousStopOnLine = null;
        nextStopOnLine = null;

        direction = DIRECTION_UNKNOWN;

        notDetectedCounter = 0;
    }

    public Tram( String line, boolean lowFloor, Date date, LatLng currentPosition ){

        this.line = line;
        this.lowFloor = lowFloor;
        this.date = date;
        this.currentPosition = currentPosition;

        distanceToFirstEndStop = -1;
        distanceToLastEndStop = -1;
        distanceToFirstStopHistory = new ArrayList<>();
        distanceToLastStopHistory = new ArrayList<>();

        currentStop = null;
        previousStopOnLine = null;
        nextStopOnLine = null;

        direction = DIRECTION_UNKNOWN;

        notDetectedCounter = 0;
    }

    public Tram(int id, String line, boolean lowFloor, Date date, LatLng currentPosition ){

        this.line = line;
        this.lowFloor = lowFloor;
        this.date = date;
        this.id = id;
        this.currentPosition = currentPosition;

        distanceToFirstEndStop = -1;
        distanceToLastEndStop = -1;
        distanceToFirstStopHistory = new ArrayList<>();
        distanceToLastStopHistory = new ArrayList<>();

        currentStop = null;
        previousStopOnLine = null;
        nextStopOnLine = null;

        direction = DIRECTION_UNKNOWN;

        notDetectedCounter = 0;
    }



    public void estimateDirection(){

        if (firstEndStop == null)
            direction = DIRECTION_UNKNOWN;

        else if (isNotTramOnLine())
                direction = DIRECTION_UNKNOWN;

         else if (isTramOnEndStop()){

            /**erase positions history if tram reached end stop*/
            if (!direction.equals(NO_DIRECTION))
                eraseDistancesHistory();

            direction = NO_DIRECTION;
        }

         /**no enough data  */
        else if (distanceToFirstStopHistory.size()<=1)
            direction = DIRECTION_UNKNOWN;

        else {
            if (direction.equals(DIRECTION_UNKNOWN) && isTramStanding())
                    direction = DIRECTION_UNKNOWN;

            else if (isTramGoingToFirstEndStop())
                direction = DIRECTION_FIRST_STOP;

            else if (isTramGoingToLastStop())
                    direction = DIRECTION_LAST_STOP;

                /**unknown direction*/
            else {

                direction = DIRECTION_UNKNOWN;
                eraseDistancesHistory();
            }
        }
    }

    private boolean isNotTramOnLine(){

        boolean notOnLine = false;
        if (nextStopOnLine!=null)
            notOnLine =  SphericalUtil.computeDistanceBetween(previousStopOnLine.getPosition(), nextStopOnLine.getPosition()) + 400
                    < (SphericalUtil.computeDistanceBetween(currentPosition, previousStopOnLine.getPosition())
                    + SphericalUtil.computeDistanceBetween(currentPosition, nextStopOnLine.getPosition()));

        return notOnLine;
    }

    private boolean isTramOnEndStop(){
        return SphericalUtil.computeDistanceBetween(currentPosition, firstEndStop.getPosition()) < END_STOP_AREA
                || SphericalUtil.computeDistanceBetween(currentPosition, lastEndStop.getPosition()) < END_STOP_AREA;
    }

    private boolean isTramStanding(){
        boolean standing = true;

        for (int i = 0; i < distanceToFirstStopHistory.size()-1; i++) {

            if (!distanceToFirstStopHistory.get(i).equals(distanceToFirstStopHistory.get(i+1)) ){
                standing = false;
                break;
            }

        }
        return standing;
    }

    private boolean isTramGoingToFirstEndStop(){
        boolean hasDirection = true;
        for (int i = 0; i < distanceToFirstStopHistory.size()-1; i++) {
            if (distanceToFirstStopHistory.get(i) < distanceToFirstStopHistory.get(i+1)){
                hasDirection = false;
                break;
            }

        }
        return hasDirection;
    }
    private boolean isTramGoingToLastStop(){
        boolean hasDirection = true;
        for (int i = 0; i < distanceToFirstStopHistory.size()-1; i++) {
            if (distanceToFirstStopHistory.get(i) > distanceToFirstStopHistory.get(i+1)){
                hasDirection = false;
                break;
            }

        }
        return hasDirection;
    }

    private void eraseDistancesHistory(){

        distanceToFirstStopHistory = new ArrayList<>();
        distanceToLastStopHistory = new ArrayList<>();
    }

    public String getDirectionString(){

        switch (direction){
            case DIRECTION_FIRST_STOP:
                return firstEndStop.getName();
            case DIRECTION_LAST_STOP:
                return lastEndStop.getName();
            case NO_DIRECTION:
                return NO_DIRECTION;
            default:
                return  DIRECTION_UNKNOWN;
        }
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isLowFloor() {
        return lowFloor;
    }

    public void setLowFloor(boolean lowFloor) {
        this.lowFloor = lowFloor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(LatLng currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastEndStop(TramStop lastEndStop) {
        this.lastEndStop = lastEndStop;
    }

    public void setFirstEndStop(TramStop firstEndStop) {
        this.firstEndStop = firstEndStop;
    }

    public double getDistanceToLastEndStop() {
        return distanceToLastEndStop;
    }

    public void setDistanceToLastEndStop(double distanceToLastEndStop) {
        this.distanceToLastEndStop = distanceToLastEndStop;
        distanceToLastStopHistory.add(distanceToLastEndStop);
    }

    public double getDistanceToFirstEndStop() {
        return distanceToFirstEndStop;
    }

    public void setDistanceToFirstEndStop(double distanceToFirstEndStop) {
        this.distanceToFirstEndStop = distanceToFirstEndStop;
        distanceToFirstStopHistory.add(distanceToFirstEndStop);
    }
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public TramStop getNextStopOnLine() {
        return nextStopOnLine;
    }

    public void setNextStopOnLine(TramStop nextStopOnLine) {
        this.nextStopOnLine = nextStopOnLine;
    }

    public TramStop getPreviousStopOnLine() {
        return previousStopOnLine;
    }

    public void setPreviousStopOnLine(TramStop previousStopOnLine) {
        this.previousStopOnLine = previousStopOnLine;
    }

    public TramStop getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(TramStop currentStop) {
        this.currentStop = currentStop;
    }

    public void removeLastDistancesFromList(){
        distanceToFirstStopHistory.remove(distanceToFirstStopHistory.size()-1);
        distanceToLastStopHistory.remove(distanceToLastStopHistory.size()-1);
    }
    public int getNotDetectedCounter() {
        return notDetectedCounter;
    }

    public void setNotDetectedCounter(int notDetectedCounter) {
        this.notDetectedCounter = notDetectedCounter;
    }
}
