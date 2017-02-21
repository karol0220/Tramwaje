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
 * Created by Karol on 2016-11-10.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DetectTramTest {


    Date date = new   Date();

    Tram t1 = new Tram(0,"31",false, date,new LatLng(52.188792, 21.005839));
    Tram t2 = new Tram(1,"31",false, date,new LatLng(52.185768, 21.002202));
    Tram t3 = new Tram(2,"31",false, date,new LatLng(52.178653, 21.001274));
    Tram t4 = new Tram(3,"31",false, date,new LatLng(52.178684, 21.002180));
    Tram t5 = new Tram(4,"31",false, date,new LatLng(52.180843, 21.022365));

    List<Tram> tramList ;


    Tram t6 = new Tram(0,"31",false, date,new LatLng(52.188831, 21.004283));
    Tram t7 = new Tram(0,"31",false, date,new LatLng(52.183367, 21.002256));
    Tram t8 = new Tram(0,"31",false, date,new LatLng(52.179455, 21.002296));
    Tram t9 = new Tram(0,"31",false, date,new LatLng(52.178684, 21.002180));
    Tram t10 = new Tram(0,"31",false, date,new LatLng(52.185841, 21.024497));

    List<Tram> tmpTramList ;

    List<Tram> checkList;

    /**
     * creates list of trams then symulates getting new data and checks if found proper trams
     */
    @Test
    public void nearestTramTest(){



        Map<String,List<Tram>> tramMap = new HashMap<>();
        tramList = new ArrayList<>();

        tramList.add(t1);
        tramList.add(t2);
        tramList.add(t3);
        tramList.add(t4);
        tramList.add(t5);

        tramMap.put("31",tramList);
        TramsDetector dt = new TramsDetector(tramMap, RuntimeEnvironment.application);

        Map<String,List<Tram>> tmpTramMap = new HashMap<>();
        tmpTramList = new ArrayList<>();

        tmpTramList.add(t6);
        tmpTramList.add(t7);
        tmpTramList.add(t8);
        tmpTramList.add(t9);
        tmpTramList.add(t10);




        tmpTramMap.put("31",tmpTramList);

         dt.detect(tmpTramMap);

        checkList = new ArrayList<>();

        Map<String,List<Tram>> checkMap = dt.getTramMap();


        for (Map.Entry<String,List<Tram>> entry :  checkMap.entrySet()) {



        for (Tram tr: entry.getValue() ) {

            switch (tr.getId()){
                case 0:
                    Assert.assertEquals(new LatLng(52.188831, 21.004283),tr.getCurrentPosition());
                    break;
                case 1:
                    Assert.assertEquals(new LatLng(52.183367, 21.002256),tr.getCurrentPosition());
                    break;
                case 2:
                    Assert.assertEquals(new LatLng(52.179455, 21.002296),tr.getCurrentPosition());
                    break;
                case 3:
                    Assert.assertEquals(new LatLng(52.178684, 21.002180),tr.getCurrentPosition());
                    break;
                case 4:
                    Assert.assertEquals(new LatLng(52.185841, 21.024497),tr.getCurrentPosition());
                    break;
            }

        }
        }

    }


    /**
     * creates listo of trams, sets directions, simulates getting new data and check if detected proper trams
     */
    @Test
    public void directionTest(){
        tramList = new ArrayList<>();

        Map<String,List<Tram>> tramMap = new HashMap<>();

        //wilanowska
        Tram t1 = new Tram(0,"31",false, date,new LatLng(52.180537, 21.022016));
        //wilanowska bukowińska
        Tram t2 = new Tram(1,"31",false, date,new LatLng(52.186004, 21.024679));
        //królikarnia wierzbno
        Tram t3 = new Tram(2,"31",false, date,new LatLng(52.189520, 21.020879));
        //rzymowskiego
        Tram t4 = new Tram(3,"31",false, date,new LatLng(52.178586, 21.001637));
        //domaniewska rzymowskiego
        Tram t5 = new Tram(4,"31",false, date,new LatLng(52.181471, 21.002233));
        //wilanowska
        Tram t6 = new Tram(5,"31",false, date,new LatLng(52.180698, 21.022353));
        //samochodowa
        Tram t7 = new Tram(6,"31",false, date,new LatLng(52.188855, 21.008181));

     t1.setDirection("1");
     t2.setDirection("0");
     t3.setDirection("0");
     t4.setDirection("0");
     t5.setDirection("1");
     t6.setDirection(Tram.NO_DIRECTION);
     t7.setDirection("1");

        //t1
        Tram t11 = new Tram(-1,"31",false, date,new LatLng(52.182261, 21.023089));
        Tram t12 = new Tram(-1,"31",false, date,new LatLng(52.184307, 21.023874));
        Tram t8 = new Tram(-1,"31",false, date,new LatLng(52.189776, 21.024216));
        Tram t9 = new Tram(-1,"31",false, date,new LatLng(52.179935, 21.002249));
        Tram t10 = new Tram(-1,"31",false, date,new LatLng(52.179270, 21.002278));
        Tram t13 = new Tram(-1,"31",false, date,new LatLng(52.180698, 21.022353));
        Tram t14 = new Tram(-1,"31",false, date,new LatLng(52.187872, 21.002075));

        tramList.add(t1);
        tramList.add(t2);
        tramList.add(t3);
        tramList.add(t4);
        tramList.add(t5);
        tramList.add(t6);
        tramList.add(t7);

         tramMap.put("31",tramList);

        TramsDetector dt = new TramsDetector(tramMap,RuntimeEnvironment.application);

        for (Tram t : tramList) {
            System.out.println("T: " + t.getId()+ "POSITION "+ t.getCurrentPosition());
        }

        System.out.println("---------------------- " );
        tmpTramList = new ArrayList<>();
        tmpTramList.add(t11);
        tmpTramList.add(t12);
        tmpTramList.add(t8);
        tmpTramList.add(t9);
        tmpTramList.add(t10);
        tmpTramList.add(t13);
        tmpTramList.add(t14);

        Map<String,List<Tram>> tmpTramMap = new HashMap<>();

        tmpTramMap.put("31",tmpTramList);

       dt.detect(tmpTramMap);

        Map<String,List<Tram>>checkMap =  dt.getTramMap();



        for (Map.Entry<String,List<Tram>> entry :  checkMap.entrySet()) {

            for (Tram tram : entry.getValue()) {
                if (tram.getCurrentPosition().equals(new LatLng(52.182261, 21.023089))) {
                    System.out.println(tram.getId());
                    //  expected t1
                    Assert.assertEquals(t1.getId(), tram.getId());
                }


                if (tram.getCurrentPosition().equals(new LatLng(52.184307, 21.023874))) {
                    System.out.println(tram.getId());
                    //expected t2
                    Assert.assertEquals(t2.getId(), tram.getId());
                }


                if (tram.getCurrentPosition().equals(new LatLng(52.189776, 21.024216))) {
                      System.out.println(tram.getId());
                    //expected t3
                    Assert.assertEquals(t3.getId(), tram.getId());
                }


                if (tram.getCurrentPosition().equals(new LatLng(52.179935, 21.002249))) {
                    System.out.println(tram.getId());
                    //expected t5
                    Assert.assertEquals(t5.getId(), tram.getId());
                }


                if (tram.getCurrentPosition().equals(new LatLng(52.179270, 21.002278))) {
                     System.out.println(tram.getId());
                    //expected 4t
                    Assert.assertEquals(t4.getId(), tram.getId());
                }


                if (tram.getCurrentPosition().equals(new LatLng(52.180698, 21.022353))) {
                         System.out.println(tram.getId());
                    //expected t6
                    Assert.assertEquals(t6.getId(), tram.getId());
                }


                if (tram.getCurrentPosition().equals(new LatLng(52.187872, 21.002075))) {
                       System.out.println(tram.getId());
                    //expected t7
                    Assert.assertEquals(t7.getId(), tram.getId());
                }

            }
        }
    }

    /**
     * trams go by
     */
    @Test
    public void tramsGoByTest() {

        Map<String, List<Tram>> tramMap = new HashMap<>();

        //tramwaj znajduje się na przystanku Metro Wierzbno
        Tram t1 = new Tram(0, "31", false, date, new LatLng(52.189179, 21.017510));

        t1.setDirection("1");

       //tramwaj znajduje się na przystanku Samochodowa
        Tram t2 = new Tram(0, "31", false, date, new LatLng(52.188922, 21.007564));
        t2.setDirection("0");

        List<Tram> tramList = new ArrayList<>();
        tramList.add(t1);
        tramList.add(t2);

        tramMap.put("31", tramList);

        //tramwaj znajduje się na przystanku Telewizja Polska
        Tram t3 = new Tram(0, "31", false, date, new LatLng(52.189034, 21.012156));

        //tramwaj znajduje się na przystanku Królikarnia
        Tram t4 = new Tram(0, "31", false, date, new LatLng(52.189889, 21.023378));


        List<Tram> tmpTramList = new ArrayList<>();
        tmpTramList.add(t3);
        tmpTramList.add(t4);

        TramsDetector dt = new TramsDetector(tramMap, RuntimeEnvironment.application);

        Map<String, List<Tram>> tmpTramMap = new HashMap<>();

        tmpTramMap.put("31", tmpTramList);
        System.out.println( " DETECTING: ");
        dt.detect(tmpTramMap);

        Map<String, List<Tram>> checkMap = dt.getTramMap();

        for (Map.Entry<String,List<Tram>> entry :  checkMap.entrySet()) {

        for (Tram t : entry.getValue()) {

            System.out.println(t.getDirection());

            if (t.getCurrentPosition().equals(new LatLng(52.189889, 21.023378))) {
                Assert.assertEquals("0", t.getDirection());
            }
            if (t.getCurrentPosition().equals(new LatLng(52.188922, 21.007564))) {
                Assert.assertEquals("1", t.getDirection());
            }
        }
    }
    }



}
