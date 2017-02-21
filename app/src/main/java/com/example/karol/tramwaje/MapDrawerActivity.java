package com.example.karol.tramwaje;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private  String SHOW_ALL_LINES;
    private  String SHOW_NO_LINES;
    private final int COMING_TRAMS_LIMIT = 7;

    private GoogleMap mMap;

    private Switch tramsSwitch;
    private Switch stopsSwitch;

    /**map of trams
     * line number is a key
     * list of trams is a value*/
    Map<String, List<Tram>> tramMap;
    List<TramStop> stopList ;

    /**which trams show on map*/
    private ArrayList<String> linesToShow;

    private Menu menu;

    boolean showTrams;
    boolean showStops;

    CountTimeController countTimeController;

    List<Marker> tramsMarkersList;
    List<Marker> stopsMarkersList;

    View markerWindowView;

    ArrayList<String> comingTramsList;

    static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        instance = this;

        SHOW_ALL_LINES = getResources().getString(R.string.allTrams);
        SHOW_NO_LINES = getResources().getString(R.string.noTrams);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);

            mapFragment.getMapAsync(this);

        tramsSwitch = (Switch) findViewById(R.id.switch_trams);
        tramsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showTrams = !showTrams;
                showTrams();
            }

        });

        stopsSwitch = (Switch) findViewById(R.id.switch_stops);
        stopsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showStops = !showStops;
                showStops();
            }
        });

        tramMap = new HashMap<>();
        stopList = new ArrayList<>();

        tramsMarkersList = new ArrayList<>();
        stopsMarkersList = new ArrayList<>();

        showTrams = true;
        showStops = false;

        linesToShow = new ArrayList<>();

        /**start download data service*/
        Intent intent = new Intent(getBaseContext(),DownloadDataService.class);
        startService(intent);

        /**view used to show arrivals*/
        markerWindowView = getLayoutInflater().inflate(R.layout.windowlayout,null);

        comingTramsList = new ArrayList<>();
        countTimeController = new CountTimeController(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.map_drawer, menu);
        this.menu = menu;

        /**start with all lines showed*/
        for (int i = 2; i < menu.size(); i++) {
            MenuItem it = menu.getItem(i);
            it.setChecked(true);
            if (!linesToShow.contains(String.valueOf(it.getTitle()))){
                linesToShow.add(String.valueOf(it.getTitle()));
            }
        }

        Intent i = new Intent(this,GetStopsService.class);
        i.putStringArrayListExtra("lines",linesToShow);
        startService(i);

        showTrams();
        showStops();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String line = item.getTitle().toString();

        if (line.equals(SHOW_ALL_LINES)){

            for (int i = 2; i < menu.size(); i++) {
                MenuItem it = menu.getItem(i);
                it.setChecked(true);
               if (!linesToShow.contains(String.valueOf(it.getTitle()))){
                   linesToShow.add(String.valueOf(it.getTitle()));
               }
            }
        }

        else if (line.equals(SHOW_NO_LINES)){

            linesToShow = new ArrayList<>();

            for (int i = 0; i < menu.size(); i++) {
                MenuItem it = menu.getItem(i);
                it.setChecked(false);
            }
        }

        else{
            if (!linesToShow.contains(line))
                 linesToShow.add(line);
            else
                linesToShow.remove(line);

            item.setChecked(!item.isChecked());
        }

        Intent i = new Intent(this,GetStopsService.class);
        i.putStringArrayListExtra("lines",linesToShow);
        startService(i);

        showTrams();
        return true;
    }

  
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return  true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng center = new LatLng(52.230040, 21.011605);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 13.0f));

        showStops();
        showTrams();

        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                if (stopsMarkersList.contains(marker)){

                    TableLayout tl = (TableLayout) markerWindowView;
                    tl.removeAllViews();
                    TableRow tableRow= new TableRow(getApplicationContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    tableRow.setLayoutParams(layoutParams);

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(marker.getTitle());
                    textView.setTextColor(Color.BLACK);
                    textView.setVisibility(TextView.VISIBLE);
                    tableRow.addView(textView);
                    tl.addView(tableRow,0);

                    int tramLimit = Math.min(COMING_TRAMS_LIMIT,comingTramsList.size());

                    for (int i = 0; i <tramLimit; i++) {

                        TableRow row= new TableRow(getApplicationContext());
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);

                        TextView tv = new TextView(getApplicationContext());
                        tv.setText(comingTramsList.get(i));
                        tv.setTextColor(Color.BLACK);
                        tv.setVisibility(TextView.VISIBLE);
                        row.addView(tv);
                        tl.addView(row,i+1);
                    }
                    return tl;
                }

                else  if (tramsMarkersList.contains(marker)){

                    TableLayout tl = (TableLayout) markerWindowView;
                    tl.removeAllViews();
                    TableRow tableRow= new TableRow(getApplicationContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    tableRow.setLayoutParams(layoutParams);

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(marker.getTitle());
                    textView.setTextColor(Color.BLACK);
                    textView.setVisibility(TextView.VISIBLE);
                    tableRow.addView(textView);
                    tl.addView(tableRow,0);

                    TableRow tableRow2= new TableRow(getApplicationContext());
                    tableRow2.setLayoutParams(layoutParams);
                    TextView textView2 = new TextView(getApplicationContext());

                    Date date = new Date();
                    long delay = (date.getTime() - Long.parseLong(marker.getSnippet())) / 1000;
                    long minutes = delay / 60;
                    long seconds = delay % 60;

                    textView2.setText(getResources().getString(R.string.was_here)+" " + minutes + getResources().getString(R.string.minutes)
                            +" " + seconds + getResources().getString(R.string.ago));

                    textView2.setTextColor(Color.BLACK);
                    textView2.setVisibility(TextView.VISIBLE);
                    tableRow2.addView(textView2);
                    tl.addView(tableRow2,1);

                    return tl;
                }
                return null;
            }
        });

        showTrams();
        showStops();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        /**handle if it is stop marker*/
        if (stopsMarkersList.contains(marker)){
            comingTramsList = new ArrayList<>();
            comingTramsList =  (ArrayList<String>) countTimeController.getComingTrams(tramMap,marker);
        }
        return false;
    }

     void showTrams(Map<String,List<Tram>> tramMap) {

         synchronized (this.tramMap) {
                 this.tramMap = tramMap;
             }
         showTrams();
    }

    private void showTrams(){

        for (int i = 0; i < tramsMarkersList.size(); i++)
            tramsMarkersList.get(i).remove();

        tramsMarkersList.clear();
        List<Tram> tramList;

        for (Map.Entry<String, List<Tram>> entry : tramMap.entrySet()) {

            if (linesToShow.contains(entry.getKey())) {
                tramList = entry.getValue();

                synchronized (tramMap) {
                    for (Tram tram : tramList) {

                        int color;
                        if (tram.getDirectionString().equals(Tram.DIRECTION_UNKNOWN))
                            color = Color.rgb(255, 25, 0);
                        else if (tram.getDirectionString().equals(Tram.NO_DIRECTION))
                            color = Color.rgb(207, 207, 207);
                        else
                            color = Color.rgb(0, 205, 0);

                        Bitmap bitmap = createMarker(String.valueOf(tram.getLine()), color, tram.isLowFloor());
                        String title;

                        switch (tram.getDirection()){
                            case Tram.DIRECTION_UNKNOWN:
                                title =  tram.getLine();
                                break;
                            case Tram.NO_DIRECTION:
                                title = tram.getLine() + " "+ getResources().getString(R.string.on_end_stop);
                                break;
                            default:
                                title = tram.getLine() + " "+getResources().getString(R.string.arrow)+" " + tram.getDirectionString();
                        }

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(tram.getCurrentPosition())
                                .snippet(String.valueOf(tram.getDate().getTime()))
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .title(title));

                        if (showTrams)
                            marker.setVisible(true);
                        else
                            marker.setVisible(false);

                        tramsMarkersList.add(marker);
                    }
                }
            }
        }
    }

    public void showStops(List<TramStop> stopList){
        this.stopList = stopList;
        showStops();
    }

    public void showStops(){

        for (int i = 0; i < stopsMarkersList.size(); i++)
            stopsMarkersList.get(i).remove();

        stopsMarkersList.clear();

            for (TramStop stop : stopList) {

                Bitmap bitmap = createMarker(getResources().getString(R.string.tram_sign), new Color().rgb(72, 118, 255), true);

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(stop.getPosition())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title(stop.getName()));

                if (showStops)
                    marker.setVisible(true);
                else
                    marker.setVisible(false);

                stopsMarkersList.add(marker);
            }
    }

    public Bitmap createMarker(String gText, int color, boolean lowFloor) {

        int size = 30;
        Bitmap myBitmap =  Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(myBitmap);
        canvas.drawColor(color);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        if (lowFloor)
             paint.setColor(Color.WHITE);
        else
            paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize(14 );

        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (myBitmap.getWidth() - bounds.width())/2;
        int y = (myBitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);
        return myBitmap;
    }
}
