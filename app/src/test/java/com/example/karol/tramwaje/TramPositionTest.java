package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

/**
 * Created by Karol on 2016-11-11.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TramPositionTest {




    Date date = new Date();

    @Test
    public void positionTest(){

        // String line, boolean lowFloor, Date date, LatLng currentPosition

        TramPositionDetector tp = new TramPositionDetector(RuntimeEnvironment.application);
        // bukow wilan
        Tram t1 = new Tram(1,"31",true,date,new LatLng(52.185093, 21.024177));

        t1  = tp.findPosition(t1);


        Assert.assertEquals("Metro Wilanowska" ,t1.getPreviousStopOnLine().getName());
        Assert.assertEquals("Bukowińska" ,t1.getNextStopOnLine().getName());




        Tram t2 = new Tram(1,"31",true,date,new LatLng(52.189903, 21.024464));

        t2 =  tp.findPosition(t2);


        Assert.assertEquals("Królikarnia", t2.getCurrentStop().getName());



        Tram t3 = new Tram(1,"31",true,date,new LatLng(52.188908, 21.013119));

        t3 = tp.findPosition(t3);

        Assert.assertEquals("Metro Wierzbno" ,t3.getPreviousStopOnLine().getName());
        Assert.assertEquals("Telewizja Polska" ,t3.getNextStopOnLine().getName());


        Tram t4 = new Tram(1,"31",true,date,new LatLng(52.181989, 21.002206));

        t4 =  tp.findPosition(t4);

        Assert.assertEquals("Domaniewska" ,t4.getPreviousStopOnLine().getName());
        Assert.assertEquals("Rzymowskiego" ,t4.getNextStopOnLine().getName());




        //52.188,21.0021 should be between woronicza konstruktorska

        Tram t5 = new Tram(1,"31",true,date,new LatLng(52.188,21.0021));

        t5 =  tp.findPosition(t5);


        Assert.assertEquals("Woronicza " ,t5.getPreviousStopOnLine().getName());
        Assert.assertEquals("Konstruktorska" ,t5.getNextStopOnLine().getName());

    }
}
