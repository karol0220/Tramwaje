package com.example.karol.tramwaje;

import android.content.Context;

import java.util.List;

/**
 * Created by Karol on 2016-12-29.
 */

public class DbController {

    private StopsDAO stopsDAO;

    public DbController(Context context){
        stopsDAO = new StopsDAO(context);
    }


    public List<TramStop> getLineStops(String line){
       return stopsDAO.getLineStops(line);
    }

    public String[] getStopLines(String name){
        return stopsDAO.getStopLines(name);
    }


}
