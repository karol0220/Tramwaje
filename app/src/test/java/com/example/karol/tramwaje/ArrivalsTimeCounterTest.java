package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Karol on 2017-01-09.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ArrivalsTimeCounterTest {

    @Test
    public void countTimeTest(){

        ArrivalsTimeCounter timeCounter = new ArrivalsTimeCounter(RuntimeEnvironment.application);

        TramPositionDetector tramPositionDetector = new TramPositionDetector(RuntimeEnvironment.application);

        String name = "Królikarnia";

        long currentTime = new Date().getTime();

        Date date1 = new Date(currentTime-1000*60);
        Date date2 = new Date(currentTime-2000*60);
        Date date3 = new Date(currentTime-1500*60);
        Date date4 = new Date(currentTime-500*60);

        Map<String,List<Tram>> tramMap = new HashMap<>();

        List<Tram> line4Trams = new ArrayList<>();
        List<Tram> line14Trams = new ArrayList<>();
        List<Tram> line18Trams = new ArrayList<>();

        //tramwaj na przystanku Park Dreszera
        Tram tram1 = TramFactory.createTram(1,"4",true,date1,new LatLng(52.196930, 21.024147));
        tram1.setDirection(Tram.DIRECTION_LAST_STOP);
        tram1 = tramPositionDetector.findPosition(tram1);

        //tramwaj pomiędzy przystankami Niedźwiedzia, Aleja Lotników
        Tram tram2 = TramFactory.createTram(2,"4",true,date2,new LatLng(52.170593, 21.018711));
        tram2.setDirection(Tram.DIRECTION_FIRST_STOP);
        tram2 = tramPositionDetector.findPosition(tram2);

        //tramwaj na przystanku Dworkowa
        Tram tram3 = TramFactory.createTram(3,"14",true,date3,new LatLng(52.204949, 21.022937));
        tram3.setDirection(Tram.DIRECTION_FIRST_STOP);
        tram3 = tramPositionDetector.findPosition(tram3);

        //tramwaj na przystanku Królikarnia
        Tram tram4 = TramFactory.createTram(4,"18",true,date4,new LatLng(52.189784, 21.023960));
        tram4.setDirection(Tram.DIRECTION_FIRST_STOP);
        tram4 = tramPositionDetector.findPosition(tram4);

        line4Trams.add(tram1);
        line4Trams.add(tram2);

        line14Trams.add(tram3);

        line18Trams.add(tram4);

        tramMap.put("4",line4Trams);
        tramMap.put("14",line14Trams);
        tramMap.put("18",line18Trams);

        List<String> results = timeCounter.getComingTrams(tramMap,name);



        Assert.assertEquals(3,results.size());
        Assert.assertEquals("4 ➡ Wyścigi za ok. 2 min   ID:1", results.get(0));
        Assert.assertEquals("14 ➡ Metro Wilanowska za ok. 4 min   ID:3", results.get(1));
        Assert.assertEquals("4 ➡ Żerań Wschodni za ok. 6 min   ID:2", results.get(2));

    }
}
