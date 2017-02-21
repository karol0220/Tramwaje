package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;

/**
 * Created by Karol on 2016-11-11.
 */

public class TramTest {
//
//    Date date = new Date();
//    TramPositionDetector tramPositionDetector = new TramPositionDetector();
//
//    @Test
//    public void directionTest(){
//
//        Tram t3 = new Tram(1,"31",true,date,new LatLng(52.188908, 21.013119));
//
//        Tram t = tramPositionDetector.findPosition(t3.getCurrentPosition());
//
//        t3.setNextStopOnLine(t.getNextStopOnLine());
//        t3.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t3.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t3.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        Tram t3_2 = new Tram(1,"31",true,date,new LatLng(52.188869, 21.007433));
//
//        t =  tramPositionDetector.findPosition(t3_2.getCurrentPosition());
//
//
//        t3_2.setNextStopOnLine(t.getNextStopOnLine());
//        t3_2.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t3_2.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t3_2.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        t3.setDistanceToFirstEndStop(t3_2.getDistanceToFirstEndStop());
//        t3.setDistanceToLastEndStop(t3_2.getDistanceToLastEndStop());
//
//        t3.estymateDirection();
//
//        Assert.assertEquals("1", t3.getDirection());
//
//
//
//
//
//        Tram t4 = new Tram(1,"31",true,date,new LatLng(52.185406, 21.002132));
//
//      t =  tramPositionDetector.findPosition(t4.getCurrentPosition());
//
//        t4.setNextStopOnLine(t.getNextStopOnLine());
//        t4.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t4.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t4.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        Tram t4_2 = new Tram(1,"31",true,date,new LatLng(52.188636, 21.002046));
//
//       t =  tramPositionDetector.findPosition(t4_2.getCurrentPosition());
//
//        t4_2.setNextStopOnLine(t.getNextStopOnLine());
//        t4_2.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t4_2.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t4_2.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        t4.setDistanceToFirstEndStop(t4_2.getDistanceToFirstEndStop());
//        t4.setDistanceToLastEndStop(t4_2.getDistanceToLastEndStop());
//
//        t4.estymateDirection();
//
//        Assert.assertEquals("0", t4.getDirection());
//
//
//
//
//        Tram t5 = new Tram(1,"31",true,date,new LatLng(52.182235, 21.002244));
//
//       t  = tramPositionDetector.findPosition(t5.getCurrentPosition());
//
//        t5.setNextStopOnLine(t.getNextStopOnLine());
//        t5.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t5.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t5.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        Tram t5_2 = new Tram(1,"31",true,date,new LatLng(52.182235, 21.002244));
//
//       t =  tramPositionDetector.findPosition(t5_2.getCurrentPosition());
//
//        t5_2.setNextStopOnLine(t.getNextStopOnLine());
//        t5_2.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t5_2.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t5_2.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        t5.setDistanceToFirstEndStop(t5_2.getDistanceToFirstEndStop());
//        t5.setDistanceToLastEndStop(t5_2.getDistanceToLastEndStop());
//
//        t5.estymateDirection();
//
//        Assert.assertEquals(null, t5.getDirection());
//
//
//        /*
//        waiting on end stop then moves
//         */
//
//        Tram t6 = new Tram(1,"31",true,date,new LatLng(52.180789, 21.022317));
//
//      t =   tramPositionDetector.findPosition(t6.getCurrentPosition());
//
//        t6.setNextStopOnLine(t.getNextStopOnLine());
//        t6.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t6.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t6.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        Tram t6_2 = new Tram(1,"31",true,date,new LatLng(52.180789, 21.022317));
//
//     t =    tramPositionDetector.findPosition(t6_2.getCurrentPosition());
//
//        t6_2.setNextStopOnLine(t.getNextStopOnLine());
//        t6_2.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t6_2.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t6_2.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        t6.setDistanceToFirstEndStop(t6_2.getDistanceToFirstEndStop());
//        t6.setDistanceToLastEndStop(t6_2.getDistanceToLastEndStop());
//
//
//        Tram t6_3 = new Tram(1,"31",true,date,new LatLng(52.183085, 21.023454));
//
//       t =  tramPositionDetector.findPosition(t6_3.getCurrentPosition());
//
//
//        t6_3.setNextStopOnLine(t.getNextStopOnLine());
//        t6_3.setPreviousStopOnLine(t.getPreviousStopOnLine());
//        t6_3.setDistanceToFirstEndStop(t.getDistanceToFirstEndStop());
//        t6_3.setDistanceToLastEndStop(t.getDistanceToLastEndStop());
//
//        t6.setDistanceToFirstEndStop(t6_3.getDistanceToFirstEndStop());
//        t6.setDistanceToLastEndStop(t6_3.getDistanceToLastEndStop());
//
//        t6.setCurrentPosition(t6_3.getCurrentPosition());
//        t6.estymateDirection();
//
//
//
//        Assert.assertEquals("1", t6.getDirection());
//    }
//
//



}
