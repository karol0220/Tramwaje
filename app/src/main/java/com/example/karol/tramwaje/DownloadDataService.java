package com.example.karol.tramwaje;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Karol on 2016-10-24.
 */


public class DownloadDataService extends IntentService {

    private final String PROTOCOL = "https";
    private final String HOST = "api.um.warszawa.pl";
    private final String FILE = "/api/action/wsstore_get/?id=c7238cfe-8b1f-4c38-bb4a-de386db7e776&apikey=";
    private final String API_KEY = "7e264cb7-0a9c-473d-80af-75a7b26e9f43";

    String tramsData;
    String oldTramsData;

    public DownloadDataService() {
        super("DownloadDataService");
        oldTramsData = "";
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        BufferedReader stream ;
        boolean serverException = false;
        while (true) {

            while (!isNetworkAvailable()){

                final  MapDrawerActivity activity = (MapDrawerActivity) MapDrawerActivity.instance;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"Połącz się z internetem",Toast.LENGTH_SHORT).show();
                    }
                });

                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }


            try {

                serverException = false;

                URL url = new URL(PROTOCOL, HOST, 443, FILE+API_KEY);
                URLConnection connection = url.openConnection();
                connection.setDoInput(true);
                connection.connect();


                stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                tramsData = stream.readLine();
                stream.close();

                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
                serverException = true;
            }

            /**send data if got new */
            if (!tramsData.equals(oldTramsData) && !tramsData.equals("{\"result\":[]} ") && !serverException)
                sendData();

             oldTramsData = tramsData;
             tramsData = "";
        }
    }


    private void sendData(){

        if(tramsData!=null){

            Intent intent = new Intent(this,DataReceiverService.class);
            intent.putExtra("data",tramsData);
            startService(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
