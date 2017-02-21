package com.example.karol.tramwaje;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Karol on 2017-01-07.
 */


public class TramStopTest {

    final String NAME = "Piaski";
    final LatLng POSITION = new LatLng(52.271695, 20.945203);

    @Test
    public void stopTest(){
        TramStop stop = new TramStop(NAME, POSITION);

        Assert.assertEquals(stop.getName(),NAME);
    }
}
