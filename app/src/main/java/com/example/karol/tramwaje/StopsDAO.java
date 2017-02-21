package com.example.karol.tramwaje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karol on 2016-11-18.
 */

 /**
 * Class works as DAO
 */

public class StopsDAO {

    /**Stops table entries*/
    private static class FeedEntryStops{
        public static final String TABLE_NAME = "Stops";
        public static final String ID = "ID";
        public static final String NAME = "Name";
        public static final String LATITUDE = "Latitude";
        public static final String LONGITUDE = "Longitude";
    }

    /**Lines table entries*/
    private static class FeedEntryLines{
        public  static final String TABLE_NAME = "Lines";
        public  static final String LINE = "Line";
        public  static final String STOP_NUMBER = "StopNumber";
        public  static final String STOP_NAME = "StopName";
        public  static final String DIRECTION = "Direction";
    }

    private final  Integer VERSION = 9;
    private final  String DATABASE_NAME = "Trams";

    private final String CREATE_TABLE_STOPS =
            "CREATE TABLE IF NOT EXISTS " + FeedEntryStops.TABLE_NAME+" ("+
                    FeedEntryStops.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    FeedEntryStops.NAME + " TEXT NOT NULL, "+
                    FeedEntryStops.LATITUDE + " TEXT NOT NULL, "+
                    FeedEntryStops.LONGITUDE + " TEXT NOT NULL "+")";

    private final String CREATE_TABLE_LINES =
            "CREATE TABLE IF NOT EXISTS " + FeedEntryLines.TABLE_NAME+"  ("+
                    FeedEntryLines.LINE + " TEXT NOT NULL, "+
                    FeedEntryLines.STOP_NUMBER + " INTEGER NOT NULL, "+
                    FeedEntryLines.STOP_NAME + " TEXT NOT NULL, "+
                    FeedEntryLines.DIRECTION + " TEXT NOT NULL"+")";


    private  final String DROP_TABLE_STOPS =
            "DROP  TABLE IF EXISTS " + FeedEntryStops.TABLE_NAME;

    private  final String DROP_TABLE_LINES =
            "DROP  TABLE IF EXISTS " + FeedEntryLines.TABLE_NAME;

    private SQLiteDatabase db;
    private Context context;
    private DbHelper dbHelper;

    private static DbHelper dBHelperInstance;

     private DbHelper getHelper(){
         if(dBHelperInstance==null)
             dBHelperInstance = new DbHelper(context);

         return dBHelperInstance;
     }

    public StopsDAO(Context context){

        this.context = context;
        dbHelper = getHelper();

        /**if there are no stops insert them*/
        if (getAllStops().size()==0)
            initialize();
    }

    private class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STOPS);
            db.execSQL(CREATE_TABLE_LINES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE_STOPS);
            db.execSQL(DROP_TABLE_LINES);

            onCreate(db);

        }
    }

    private void deleteAll(){
        synchronized (dbHelper) {
            db = dbHelper.getWritableDatabase();
            db.delete(FeedEntryLines.TABLE_NAME, null, null);
            db.delete(FeedEntryStops.TABLE_NAME, null, null);
            db.close();
        }
    }

    /**fills tables*/
    private void initialize(){

        synchronized (dbHelper) {

        db = dbHelper.getWritableDatabase();

            insertStop("Metro Wilanowska", 52.180945, 21.022404);
            insertStop("Bukowińska", 52.186304, 21.024679);
            insertStop("Królikarnia", 52.189940, 21.024466);
            insertStop("Metro Wierzbno", 52.188969, 21.017183);
            insertStop("Telewizja Polska", 52.188885, 21.011621);
            insertStop("Samochodowa", 52.188833, 21.007443);
            //regular stop
            insertStop("Woronicza ", 52.188794, 21.002014);
            //end stop
            insertStop("Woronicza", 52.187821, 21.000252);
            insertStop("Konstruktorska", 52.186037, 21.002090);
            insertStop("Domaniewska", 52.182753, 21.002172);
            insertStop("Rzymowskiego", 52.178750, 21.000621);
            insertStop("Cmentarz Wolski", 52.224696, 20.931119);
            insertStop("Sowińskiego", 52.225521, 20.936898);
            insertStop("Reduta Wolska", 52.226579, 20.941934);
            insertStop("Cmentarz Prawosławny", 52.227391, 20.945818);
            insertStop("Elekcyjna", 52.229631, 20.951517);
            insertStop("Osiedle Wolska", 52.230772, 20.955119);
            insertStop("Sokołowska", 52.232425, 20.960753);
            insertStop("Płocka", 52.233638, 20.966727);
            insertStop("Zajezdnia Wola", 52.235181, 20.973867);
            insertStop("Okopowa", 52.236989, 20.980152);
            insertStop("Żytnia", 52.239558, 20.977710);
            insertStop("Dzielna", 52.241839, 20.977889);
            insertStop("Cmentarz Żydowski", 52.244643, 20.977231);
            insertStop("Niska", 52.248723, 20.979162);
            insertStop("Powązkowska", 52.253268, 20.981294);
            insertStop("Rondo Radosława", 52.254758, 20.982474);
            insertStop("Księdza Popiełuszki", 52.266418, 20.977105);
            insertStop("Metro Marymont", 52.272325, 20.972004);
            insertStop("Wyścigi", 52.168070, 21.016137);
            insertStop("Aleja Lotników", 52.172797, 21.019564);
            insertStop("Niedźwiedzia", 52.176392, 21.020868);
            insertStop("Malczewskiego", 52.194265, 21.024307);
            insertStop("Park Dreszera", 52.196931, 21.024136);
            insertStop("Morskie Oko", 52.202399, 21.023414);
            insertStop("Dworkowa", 52.204994, 21.022937);
            insertStop("Rakowiecka", 52.209297, 21.021365);
            insertStop("Plac Unii Lubelskiej", 52.214356, 21.021122);
            insertStop("Trasa Łazienkowska", 52.217654, 21.019167);
            insertStop("Plac Zbawiciela", 52.219824, 21.017933);
            insertStop("Metro Politechnika", 52.219928, 21.015577);
            insertStop("Plac Politechniki", 52.220046, 21.011058);
            insertStop("Nowowiejska", 52.220204, 21.004917);
            insertStop("Koszykowa", 52.223016, 21.004731);
            insertStop("Dworzec Centralny", 52.228386, 21.001515);
            insertStop("Rondo ONZ", 52.233128, 20.998237);
            insertStop("Norblin", 52.232076, 20.993198);
            insertStop("Rondo Daszyńskiego", 52.230325, 20.984427);
            insertStop("Karolkowa", 52.229427, 20.979106);
            insertStop("Szpital Wolski", 52.229183, 20.969610);
            insertStop("Rogalińska", 52.231297, 20.968990);
            insertStop("Fort Wola", 52.223452, 20.929167);
            insertStop("Połczyńska P+R", 52.221938, 20.922770);
            insertStop("Ciepłownia Wola", 52.220828, 20.918069);
            insertStop("Synów Pułku", 52.225553, 20.916297);
            insertStop("Hala Wola", 52.229061, 20.915316);
            insertStop("Plac Kasztelański", 52.231465, 20.914934);
            insertStop("Czumy", 52.235354, 20.914376);
            insertStop("Bemowo Ratusz", 52.239220, 20.913190);
            insertStop("Klemensiewicza", 52.239021, 20.904962);
            insertStop("Osiedle Górczewska", 52.238723, 20.900698);
            insertStop("Nocznickiego", 52.284794, 20.922920);
            insertStop("Popiela", 52.282337, 20.920175);
            insertStop("Bogusławskiego", 52.281254, 20.924370);
            insertStop("Cmentarz Wawrzyszewski", 52.280206, 20.928237);
            insertStop("Aspekt", 52.278384, 20.933413);
            insertStop("Aleja Reymonta", 52.275275, 20.938354);
            //regualr stop
            insertStop("Piaski ", 52.271659, 20.945123);
            //end stop
            insertStop("Piaski",52.271225, 20.943814);
            insertStop("Duracza", 52.269443, 20.953126);
            insertStop("Park Olszyna", 52.268077, 20.958118);
            insertStop("Włościańska", 52.266748, 20.962910);
            insertStop("Sady Żoliborskie", 52.264275, 20.971787);
            insertStop("Plac Grunwaldzki", 52.262521, 20.979324);
            insertStop("Stawki", 52.251026, 20.985676);
            insertStop("Anielewicza", 52.247380, 20.988811);
            insertStop("Nowolipki", 52.244157, 20.991607);
            insertStop("Kino Femina", 52.241879, 20.993583);
            insertStop("Hala Mirowska", 52.239286, 20.995719);
            insertStop("GUS", 52.216411, 21.005271);
            insertStop("Biblioteka Narodowa", 52.213027, 21.006299);
            insertStop("Metro Pole Mokotowskie", 52.208836, 21.007676);
            insertStop("Kielecka", 52.206788, 21.003529);
            insertStop("Dworzec Wschodni", 52.252890, 21.050400);
            insertStop("Kijowska", 52.249562, 21.042990);
            insertStop("Ząbkowska", 52.251819, 21.037946);
            insertStop("Dworzec Wileński", 52.254706, 21.034799);
            insertStop("Ratuszowa-ZOO", 52.256723, 21.028418);
            insertStop("Plac Hallera", 52.259112, 21.025370);
            insertStop("Rondo Starzyńskiego", 52.263735, 21.021483);
            insertStop("Wybrzeże Helskie", 52.261961, 21.013587);
            insertStop("Most Gdański", 52.260056, 21.007233);
            insertStop("Park Traugutta", 52.258750, 21.002570);
            insertStop("Dworzec Gdański", 52.258172, 20.995473);
            insertStop("Baseny Inflancka", 52.256581, 20.988014);
            insertStop("Conrada", 52.271251, 20.934023);
            insertStop("Ogrody Działkowe Bemowo", 52.269019, 20.932816);
            insertStop("Piastów Śląskich", 52.263532, 20.927700);
            //end stop
            insertStop("Nowe Bemowo", 52.260249, 20.924928);
            insertStop("Nowe Bemowo ", 52.259621, 20.923001);
            insertStop("Wrocławska", 52.257166, 20.919948);
            insertStop("Radiowa", 52.252169, 20.913578);
            insertStop("Dywizjonu 303", 52.246995, 20.911442);
            insertStop("Kazubów", 52.242788, 20.912416);
            insertStop("Plac Zawiszy", 52.224851, 20.989086);
            insertStop("Plac Starynkiewicza", 52.226414, 20.995778);
            insertStop("Centrum", 52.229821, 21.011705);
            insertStop("Krucza", 52.230820, 21.016329);
            insertStop("Muzeum Narodowe", 52.231779, 21.020830);
            insertStop("Most Poniatowskiego", 52.235071, 21.036946);
            insertStop("Rondo Waszyngtona", 52.238162, 21.051716);
            insertStop("Aleja Zieleniecka", 52.247698, 21.047622);
            insertStop("Lubelska", 52.247300, 21.052676);
            insertStop("Bliska", 52.246887, 21.058531);
            insertStop("Gocławska", 52.246526, 21.063985);
            insertStop("Praga Południe - Ratusz", 52.246187, 21.069523);
            insertStop("Międzyborska", 52.245881, 21.074730);
            insertStop("Kickiego", 52.245611, 21.079096);
            //regular stop
            insertStop("Wiatraczna ", 52.245379, 21.082926);
            //end stop
            insertStop("Wiatraczna", 52.244624, 21.084580);
            insertStop("Plac Konstytucji", 52.223211, 21.015688);
            insertStop("Hoża", 52.226611, 21.013651);
            insertStop("Metro Świętokrzyska", 52.235223, 21.008506);
            insertStop("Królewska", 52.238782, 21.006097);
            insertStop("Plac Bankowy", 52.243293, 21.002617);
            insertStop("Metro Ratusz Arsenał", 52.244387, 21.001280);
            insertStop("Muranów", 52.249286, 20.998853);
            insertStop("Muranowska", 52.253561, 20.997796);
            insertStop("Dzika", 52.252788, 20.990437);
            insertStop("Park Praski", 52.252600, 21.029771);
            insertStop("Stare Miasto", 52.246756, 21.014978);
            insertStop("Wola Ratusz", 52.239768, 20.987124);
            insertStop("Banacha", 52.209810, 20.981696);
            insertStop("Bitwy Warszawskiej 1920", 52.211079, 20.976598);
            insertStop("Och Teatr", 52.214436, 20.979487);
            insertStop("Wawelska", 52.216299, 20.981108);
            //regular stop
            insertStop("Plac Narutowicza ", 52.218787, 20.983344);
            //end stop
            insertStop("Plac Narutowicza", 52.218848, 20.984519);
            insertStop("Ochota Ratusz", 52.222657, 20.986729);
            insertStop("Inżynierska", 52.257890, 21.034525);
            insertStop("Bródnowska", 52.263786, 21.038279);
            insertStop("Rondo Żaba", 52.269184, 21.036795);
            insertStop("Staniewicka", 52.273727, 21.031807);
            insertStop("Pożarowa", 52.278270, 21.027646);
            insertStop("Budowlana", 52.280993, 21.026632);
            insertStop("Julianowska", 52.285360, 21.032651);
            insertStop("Rembielińska", 52.288314, 21.030632);
            insertStop("Poborzańska", 52.290112, 21.029397);
            insertStop("Kondratowicza", 52.293537, 21.027112);
            insertStop("Toruńska", 52.296655, 21.024694);
            insertStop("Annopol", 52.298559, 21.021747);
            insertStop("Orlich Gniazd", 52.248809, 20.920769);
            insertStop("Marynin", 52.247248, 20.925203);
            insertStop("Koło", 52.246698, 20.938914);
            insertStop("Dalibora", 52.246826, 20.942899);
            insertStop("Deotymy", 52.247033, 20.947532);
            insertStop("Magistracka", 52.247305, 20.953242);
            insertStop("Majakowskiego", 52.247361, 20.956547);
            insertStop("Wawrzyszewska", 52.246379, 20.962242);
            insertStop("Młynów", 52.244537, 20.965730);
            insertStop("Długosza", 52.240795, 20.970550);
            insertStop("Młynarska", 52.237066, 20.972017);
            insertStop("Berezyńska", 52.238946, 21.055706);
            insertStop("Park Skaryszewski", 52.240451, 21.063054);
            insertStop("Kinowa", 52.242184, 21.071558);
            insertStop("Grenadierów", 52.243668, 21.078853);
            insertStop("Czapelska", 52.244729, 21.091611);
            insertStop("Wspólna Droga", 52.243950, 21.095302);
            insertStop("Plac Szembeka", 52.242473, 21.102231);
            insertStop("Żółkiewskiego", 52.241333, 21.107632);
            insertStop("Kwatery Głównej", 52.239927, 21.114218);
            insertStop("Gocławek", 52.238158, 21.119038);
            insertStop("Czynszowa", 52.260009, 21.043026);
            insertStop("Środkowa", 52.259196, 21.041463);
            insertStop("Konopacka", 52.259506, 21.039489);
            insertStop("Żerań FSO", 52.290444, 21.002204);
            insertStop("Dyrekcja FSO", 52.286961, 21.005899);
            insertStop("Budzińskiej-Tylickiej", 52.281985, 21.009413);
            insertStop("Śliwice", 52.278195, 21.011716);
            insertStop("Batalionu Platerówek", 52.273741, 21.014444);
            insertStop("Pimot", 52.270263, 21.017027);
            insertStop("Golędzinów", 52.266730, 21.020008);
            insertStop("Siodlarska", 52.247429, 20.916758);
            insertStop("Osiedle Zielony Staw", 52.246759, 20.906024);
            insertStop("Fort Blizne", 52.246731, 20.899897);
            insertStop("Kocjana", 52.249660, 20.898536);
            insertStop("WAT", 52.252420, 20.898187);
            insertStop("Archimedesa", 52.255984, 20.897786);
            insertStop("Stare Bemowo", 52.259932, 20.897822);
            insertStop("Boernerowo", 52.263456, 20.899832);
            insertStop("KS Polonia", 52.255733, 20.998640);
            insertStop("Tarchomin Kościelny", 52.324079, 20.942996);
            insertStop("Mehoffera", 52.322648, 20.944304);
            insertStop("Tarchomin", 52.318655, 20.952453);
            insertStop("Myśliborska", 52.316858, 20.960725);
            insertStop("Stare Świdry", 52.314586, 20.967822);
            insertStop("Świderska", 52.310758, 20.959487);
            //regular stop
            insertStop("Zajezdnia Żoliborz ", 52.298323, 20.934074);
            insertStop("Metro Młociny", 52.291909, 20.929586);
            insertStop("Żerań Wschodni", 52.311048, 21.012508);
            insertStop("Faradaya", 52.308754, 21.015560);
            insertStop("Odlewnicza", 52.305537, 21.017984);
            insertStop("Inowłodzka", 52.301181, 21.021257);
            insertStop("Cmentarz Włoski", 52.297895, 20.942888);
            insertStop("Przy Agorze", 52.295675, 20.944981);
            insertStop("UKSW", 52.290247, 20.950090);
            insertStop("Szpital Bielański", 52.287742, 20.952539);
            insertStop("AWF", 52.283437, 20.956364);
            insertStop("Podleśna IMiGW", 52.280559, 20.959090);
            insertStop("Żeromskiego", 52.275572, 20.964009);
            insertStop("Park Kaskada", 52.273507, 20.967271);
            insertStop("Teatr Komedia", 52.270181, 20.980610);
            insertStop("Plac Wilsona", 52.268939, 20.986341);
            insertStop("Plac Inwalidów", 52.265119, 20.989932);
            insertStop("Generała Zajączka", 52.262262, 20.992685);
            insertStop("P+R Aleja Krakowska", 52.176135, 20.944261);
            insertStop("Instytut Lotnictwa", 52.179333, 20.947283);
            insertStop("Lipowczana", 52.183375, 20.951670);
            insertStop("Krakowiaków", 52.186285, 20.954684);
            insertStop("Hynka", 52.190195, 20.958256);
            insertStop("Włochy Ratusz", 52.193173, 20.960792);
            insertStop("PKP Rakowiec", 52.197232, 20.964442);
            insertStop("Korotyńskiego", 52.202620, 20.969175);
            insertStop("Dickensa", 52.205759, 20.971961);
            insertStop("Hale Banacha", 52.208716, 20.974561);
            insertStop("Zajezdnia Praga", 52.256341, 21.052856);
            insertStop("Wojnicka", 52.257155, 21.055518);
            insertStop("Kawęczyńska Bazylika", 52.258141, 21.059538);
            insertStop("NIK", 52.218954, 20.998879);
            insertStop("Raszyńska", 52.219174, 20.989723);
            insertStop("Bohomolca", 52.272317, 20.983535);
            insertStop("Marymont Potok", 52.275172, 20.980434);
            insertStop("Wołoska", 52.195384, 21.002142);
            insertStop("Szpital MSW", 52.198907, 21.001201);
            insertStop("Kulskiego", 52.202113, 21.000309);
            insertStop("Rakowiecka Sanktuarium", 52.205372, 20.999371);
            insertStop("Namysłowska", 52.266953, 21.031093);
            insertStop("Muzeum Powstania Warszawskiego", 52.233300, 20.982136);

            /**LINE 1*/
            insertLineStop("1", 0, "Banacha", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 1, "Bitwy Warszawskiej 1920", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 2, "Och Teatr", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 3, "Wawelska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 4, "Plac Narutowicza ", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 5, "Ochota Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 6, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 7, "Rondo Daszyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 8, "Muzeum Powstania Warszawskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 9, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 10, "Żytnia", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 11, "Dzielna", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("1", 12, "Cmentarz Żydowski", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 13, "Niska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 14, "Powązkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 15, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 16, "Baseny Inflancka", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 17, "Dworzec Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 18, "Park Traugutta", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 19, "Most Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 20, "Wybrzeże Helskie", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 21, "Rondo Starzyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 22, "Namysłowska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 23, "Rondo Żaba", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 24, "Staniewicka", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 25, "Pożarowa", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 26, "Budowlana", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 27, "Julianowska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 28, "Rembielińska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 29, "Poborzańska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 30, "Kondratowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 31, "Toruńska", TramStop.DIRECTION_BOTH);
            insertLineStop("1", 32, "Annopol", TramStop.DIRECTION_BOTH);

            /**LINE 2*/
            insertLineStop("2", 0, "Tarchomin Kościelny", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 1, "Mehoffera", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 2, "Tarchomin", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 3, "Myśliborska", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 4, "Stare Świdry", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 5, "Świderska", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 6, "Zajezdnia Żoliborz ", TramStop.DIRECTION_BOTH);
            insertLineStop("2", 7, "Metro Młociny", TramStop.DIRECTION_BOTH);

            /**LINE 3*/
            insertLineStop("3", 0, "Gocławek", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 1, "Kwatery Głównej", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 2, "Żółkiewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 3, "Plac Szembeka", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 4, "Wspólna Droga", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 5, "Czapelska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 6, "Wiatraczna ", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 7, "Kickiego", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 8, "Międzyborska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 9, "Praga Południe - Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 10, "Gocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 11, "Bliska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 12, "Lubelska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 13, "Aleja Zieleniecka", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 14, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 15, "Ząbkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 16, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 17, "Inżynierska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 18, "Bródnowska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 19, "Rondo Żaba", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 20, "Staniewicka", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 21, "Pożarowa", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 22, "Budowlana", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 23, "Julianowska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 24, "Rembielińska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 25, "Poborzańska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 26, "Kondratowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 27, "Toruńska", TramStop.DIRECTION_BOTH);
            insertLineStop("3", 28, "Annopol", TramStop.DIRECTION_BOTH);

            /**LINE 4*/
            insertLineStop("4", 0, "Żerań Wschodni", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 1, "Faradaya", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 2, "Odlewnicza", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 3, "Inowłodzka", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 4, "Toruńska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 5, "Kondratowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 6, "Poborzańska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 7, "Rembielińska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 8, "Julianowska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 9, "Budowlana", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 10, "Pożarowa", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 11, "Staniewicka", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 12, "Rondo Żaba", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 13, "Bródnowska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 14, "Inżynierska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 15, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 16, "Park Praski", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 17, "Stare Miasto", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 18, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 19, "Plac Bankowy", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 20, "Królewska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 21, "Metro Świętokrzyska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 22, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 23, "Hoża", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 24, "Plac Konstytucji", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 25, "Plac Zbawiciela", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 26, "Trasa Łazienkowska", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("4", 27, "Plac Unii Lubelskiej", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 28, "Rakowiecka", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 29, "Dworkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 30, "Morskie Oko", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 31, "Park Dreszera", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 32, "Malczewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 33, "Królikarnia", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 34, "Bukowińska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 35, "Metro Wilanowska", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 36, "Niedźwiedzia", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 37, "Aleja Lotników", TramStop.DIRECTION_BOTH);
            insertLineStop("4", 38, "Wyścigi", TramStop.DIRECTION_BOTH);

            /**LINE 6*/
            insertLineStop("6", 0, "Metro Młociny", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 1, "Zajezdnia Żoliborz ", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 2, "Cmentarz Włoski", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 3, "Przy Agorze", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 4, "UKSW", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 5, "Szpital Bielański", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 6, "AWF", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 7, "Podleśna IMiGW", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 8, "Żeromskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 9, "Park Kaskada", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 10, "Metro Marymont", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 11, "Teatr Komedia", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("6", 12, "Plac Wilsona", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 13, "Plac Inwalidów", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 14, "Generała Zajączka", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 15, "Dworzec Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 16, "KS Polonia", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 17, "Park Traugutta", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 18, "Most Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 19, "Wybrzeże Helskie", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 20, "Rondo Starzyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 21, "Plac Hallera", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 22, "Ratuszowa-ZOO", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 23, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 24, "Ząbkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 25, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 26, "Aleja Zieleniecka", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 27, "Lubelska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 28, "Bliska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 29, "Gocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 30, "Praga Południe - Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 31, "Międzyborska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 32, "Kickiego", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 33, "Wiatraczna ", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 34, "Czapelska", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 35, "Wspólna Droga", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 36, "Plac Szembeka", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 37, "Żółkiewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 38, "Kwatery Głównej", TramStop.DIRECTION_BOTH);
            insertLineStop("6", 39, "Gocławek", TramStop.DIRECTION_BOTH);

            /**LINE 7*/
            insertLineStop("7", 0, "P+R Aleja Krakowska", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 1, "Instytut Lotnictwa", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 2, "Lipowczana", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 3, "Krakowiaków", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 4, "Hynka", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 5, "Włochy Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 6, "PKP Rakowiec", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 7, "Korotyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 8, "Dickensa", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 9, "Hale Banacha", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 10, "Bitwy Warszawskiej 1920", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 11, "Och Teatr", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 12, "Wawelska", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 13, "Plac Narutowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 14, "Ochota Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 15, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 16, "Plac Starynkiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 17, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 18, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 19, "Krucza", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 20, "Muzeum Narodowe", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 21, "Most Poniatowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 22, "Rondo Waszyngtona", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 23, "Aleja Zieleniecka", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 24, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 25, "Dworzec Wschodni", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 26, "Zajezdnia Praga", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 27, "Wojnicka", TramStop.DIRECTION_BOTH);
            insertLineStop("7", 28, "Kawęczyńska Bazylika", TramStop.DIRECTION_BOTH);

            /**LINE 9*/
            insertLineStop("9", 0, "Gocławek", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 1, "Kwatery Głównej", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 2, "Żółkiewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 3, "Plac Szembeka", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 4, "Wspólna Droga", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 5, "Czapelska", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 6, "Wiatraczna", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 7, "Grenadierów", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 8, "Kinowa", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 9, "Park Skaryszewski", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 10, "Berezyńska", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 11, "Rondo Waszyngtona", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 12, "Most Poniatowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 13, "Muzeum Narodowe", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 14, "Krucza", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 15, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 16, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 17, "Plac Starynkiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 18, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 19, "Ochota Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 20, "Plac Narutowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 21, "Wawelska", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 22, "Och Teatr", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 23, "Bitwy Warszawskiej 1920", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 24, "Hale Banacha", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 25, "Dickensa", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 26, "Korotyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 27, "PKP Rakowiec", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 28, "Włochy Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 29, "Hynka", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 30, "Krakowiaków", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 31, "Lipowczana", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 32, "Instytut Lotnictwa", TramStop.DIRECTION_BOTH);
            insertLineStop("9", 33, "P+R Aleja Krakowska", TramStop.DIRECTION_BOTH);

            /**LINE 10*/
            insertLineStop("10", 0, "Wyścigi", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 1, "Aleja Lotników", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 2, "Niedźwiedzia", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 3, "Metro Wilanowska", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 4, "Bukowińska", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 5, "Króliarnia", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 6, "Malczewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 7, "Park Dreszera", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 8, "Morskie Oko", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 9, "Dworkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 10, "Rakowiecka", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 11, "Plac Unii Lubelskiej", TramStop.DIRECTION_BOTH);
            //first
            insertLineStop("10", 12, "Trasa Łazienkowska", TramStop.DIRECTION_FIRST_STOP);
            //last
            insertLineStop("10", 13, "Plac Zbawiciela", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("10", 14, "Metro Politechnika", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 15, "Plac Politechniki", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 16, "Nowowiejska", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 17, "Koszykowa", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 18, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 19, "Rondo ONZ", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 20, "Norblin", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 21, "Rondo Daszyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 22, "Karolkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 23, "Szpital Wolski", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 24, "Rogalińska", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 25, "Płocka", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 26, "Sokołowska", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 27, "Osiedle Wolska", TramStop.DIRECTION_BOTH);
            //first
            insertLineStop("10", 28, "Elekcyjna", TramStop.DIRECTION_FIRST_STOP);
            //last
            insertLineStop("10", 29, "Cmentarz Prawosławny", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("10", 30, "Reduta Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 31, "Sowińskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 32, "Fort Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 33, "Połczyńska P+R", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 34, "Ciepłownia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 35, "Synów Pułku", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 36, "Hala Wola", TramStop.DIRECTION_BOTH);
            //last
            insertLineStop("10", 37, "Plac Kasztelański", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("10", 38, "Czumy", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 39, "Bemowo Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 40, "Klemensiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("10", 41, "Osiedle Górczewska", TramStop.DIRECTION_BOTH);

            /**LINE 11*/
            insertLineStop("11", 0, "Rondo Daszyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 1, "Karolkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 2, "Szpital Wolski", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 3, "Rogalińska", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 4, "Płocka", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 5, "Sokołowska", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 6, "Osiedle Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 7, "Elekcyjna", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 8, "Cmentarz Prawosławny", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("11", 9, "Reduta Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 10, "Sowińskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 11, "Fort Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 12, "Połczyńska P+R", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 13, "Ciepłownia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 14, "Synów Pułku", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 15, "Hala Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 16, "Plac Kasztelański", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("11", 17, "Czumy", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 18, "Bemowo Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 19, "Kazubów", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 20, "Dywizjonu 303", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 21, "Radiowa", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 22, "Wrocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 23, "Nowe Bemowo ", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 24, "Piastów Śląskich", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 25, "Ogrody Działkowe Bemowo", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 26, "Conrada", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 27, "Aleja Reymonta", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 28, "Aspekt", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 29, "Cmentarz Wawrzyszewski", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 30, "Bogusławskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 31, "Popiela", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 32, "Nocznickiego", TramStop.DIRECTION_BOTH);
            insertLineStop("11", 33, "Metro Młociny", TramStop.DIRECTION_BOTH);

            /**LINE 13*/
            insertLineStop("13", 0, "Cmentarz Wolski", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 1, "Sowińskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 2, "Reduta Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 3, "Cmentarz Prawosławny", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("13", 4, "Elekcyjna", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 5, "Osiedle Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 6, "Sokołowska", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 7, "Płocka", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 8, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 9, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 10, "Wola Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 11, "Kino Femina", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 12, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 13, "Stare Miasto", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 14, "Park Praski", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 15, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 16, "Dworzec Wschodni", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 17, "Ząbkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 18, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 19, "Zajezdnia Praga", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 20, "Wojnicka", TramStop.DIRECTION_BOTH);
            insertLineStop("13", 21, "Kawęczyńska Bazylika", TramStop.DIRECTION_BOTH);

            /**LINE 14*/
            insertLineStop("14", 0, "Metro Wilanowska", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 1, "Bukowińska", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 2, "Królikarnia", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 3, "Malczewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 4, "Park Dreszera", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 5, "Morskie Oko", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 6, "Dworkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 7, "Rakowiecka", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 8, "Plac Unii Lubelskiej", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 9, "Trasa Łazienkowska", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("14", 10, "Plac Zbawiciela", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("14", 11, "Metro Politechnika", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 12, "Plac Politechniki", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 13, "Nowowiejska", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 14, "NIK", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 15, "Raszyńska", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 16, "Plac Narutowicza ", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 17, "Wawelska", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 18, "Och Teatr", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 19, "Bitwy Warszawskiej 1920", TramStop.DIRECTION_BOTH);
            insertLineStop("14", 20, "Banacha", TramStop.DIRECTION_BOTH);

            /**LINE 15*/
            insertLineStop("15", 0, "P+R Aleja Krakowska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 1, "Instytut Lotnictwa", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 2, "Lipowczana", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 3, "Krakowiaków", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 4, "Hynka", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 5, "Włochy Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 6, "PKP Rakowiec", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 7, "Korotyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 8, "Dickensa", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 9, "Hale Banacha", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 10, "Bitwy Warszawskiej 1920", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 11, "Och Teatr", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 12, "Wawelska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 13, "Plac Narutowicza ", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 14, "Raszyńska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 15, "NIK", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 16, "Nowowiejska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 17, "Plac Politechniki", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 18, "Metro Politechnika", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 19, "Plac Konstytucji", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 20, "Hoża", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 21, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 22, "Metro Świętokrzyska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 23, "Królewska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 24, "Plac Bankowy", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 25, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 26, "Muranów", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 27, "Muranowska", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 28, "Dworzec Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 29, "Generała Zajączka", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 30, "Plac Inwalidów", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 31, "Plac Wilsona", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 32, "Bohomolca", TramStop.DIRECTION_BOTH);
            insertLineStop("15", 33, "Marymont Potok", TramStop.DIRECTION_BOTH);

            /**LINE 17*/
            insertLineStop("17", 0, "Woronicza", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 1, "Wołoska", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 2, "Szpital MSW", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 3, "Kulskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 4, "Rakowiecka Sanktuarium", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 5, "Kielecka", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 6, "Metro Pole Mokotowskie", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 7, "Biblioteka Narodowa", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 8, "GUS", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 9, "Nowowiejska", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 10, "Koszykowa", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 11, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 12, "Rondo ONZ", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 13, "Hala Mirowska", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 14, "Kino Femina", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 15, "Nowolipki", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 16, "Anielewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 17, "Stawki", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 18, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 19, "Plac Grunwaldzki", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 20, "Księdza Popiełuszki", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 21, "Metro Marymont", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 22, "Park Kaskada", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 23, "Żeromskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 24, "Podleśna IMiGW", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 25, "AWF", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 26, "Szpital Bielański", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 27, "UKSW", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 28, "Przy Agorze", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 29, "Cmentarz Włoski", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 30, "Świderska", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 31, "Stare Świdry", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 32, "Myśliborska", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 33, "Tarchomin", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 34, "Mehoffera", TramStop.DIRECTION_BOTH);
            insertLineStop("17", 35, "Tarchomin Kościelny", TramStop.DIRECTION_BOTH);

            /**LINE 18*/
            insertLineStop("18", 0, "Żerań FSO", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 1, "Dyrekcja FSO", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 2, "Budzińskiej-Tylickiej", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 3, "Śliwice", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 4, "Batalionu Platerówek", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 5, "Pimot", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 6, "Golędzinów", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 7, "Rondo Starzyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 8, "Wybrzeże Helskie", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 9, "Most Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 10, "Park Traugutta", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 11, "KS Polonia", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 12, "Muranowska", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 13, "Muranów", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 14, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 15, "Plac Bankowy", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 16, "Królewska", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 17, "Metro Świętokrzyska", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 18, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 19, "Hoża", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 20, "Plac Konstytucji", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 21, "Plac Zbawiciela", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 22, "Trasa Łazienkowska", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("18", 23, "Plac Unii Lubelskiej", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 24, "Rakowiecka", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 25, "Dworkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 26, "Morskie Oko", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 26, "Park Dreszera", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 28, "Malczewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 29, "Królikarnia", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 30, "Metro Wierzbno", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 31, "Telewizja Polska", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 32, "Samochodowa", TramStop.DIRECTION_BOTH);
            insertLineStop("18", 33, "Woronicza", TramStop.DIRECTION_BOTH);

            /**LINE 20*/
            insertLineStop("20", 0, "Żerań FSO", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 1, "Dyrekcja FSO", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 2, "Budzińskiej-Tylickiej", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 3, "Śliwice", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 4, "Batalionu Platerówek", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 5, "Pimot", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 6, "Golędzinów", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 7, "Rondo Starzyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 8, "Plac Hallera", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 9, "Ratuszowa-ZOO", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 10, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 11, "Park Praski", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 12, "Stare Miasto", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 13, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 14, "Kino Femina", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 15, "Wola Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 16, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 17, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 18, "Młynarska", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 19, "Długosza", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 20, "Młynów", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 21, "Wawrzyszewska", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 22, "Majakowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 23, "Magistracka", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 24, "Deotymy", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 25, "Dalibora", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 26, "Koło", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 27, "Marynin", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 28, "Siodlarska", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 29, "Osiedle Zielony Staw", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 30, "Fort Blizne", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 31, "Kocjana", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 32, "WAT", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 33, "Archimedesa", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 34, "Stare Bemowo", TramStop.DIRECTION_BOTH);
            insertLineStop("20", 35, "Boernerowo", TramStop.DIRECTION_BOTH);

            /**LINE 22*/
            insertLineStop("22", 0, "Piaski", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 1, "Piaski ", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 2, "Duracza", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 3, "Park Olszyna", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 4, "Włościańska", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 5, "Sady Żoliborskie", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 6, "Plac Grunwaldzki", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 7, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 8, "Powązkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 9, "Niska", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 10, "Cmentarz Żydowski", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 11, "Dzielna", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("22", 12, "Żytnia", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 13, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 14, "Muzeum Powstania Warszawskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 15, "Rondo Daszyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 16, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 17, "Plac Starynkiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 18, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 19, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 20, "Krucza", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 21, "Muzeum Narodowe", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 22, "Most Poniatowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 23, "Rondo Waszyngtona", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 24, "Aleja Zieleniecka", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 25, "Lubelska", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 26, "Gocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 27, "Praga Południe - Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 28, "Międzyborska", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 29, "Kickiego", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 30, "Wiatraczna ", TramStop.DIRECTION_BOTH);
            insertLineStop("22", 31, "Wiatraczna", TramStop.DIRECTION_BOTH);

            /**LINE 23*/
            insertLineStop("23", 0, "Czynszowa", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 1, "Środkowa", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("23", 2, "Konopacka", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 3, "Inżynierska", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 4, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 5, "Park Praski", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 6, "Stare Miasto", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 7, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 8, "Kino Femina", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 9, "Wola Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 10, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 11, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 12, "Młynarska", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 13, "Długosza", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 14, "Młynów", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 15, "Wawrzyszewska", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 16, "Majakowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 17, "Magistracka", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 18, "Deotymy", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 19, "Dalibora", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 20, "Koło", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 21, "Marynin", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 22, "Orlich Gniazd", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 23, "Radiowa", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 24, "Wrocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("23", 25, "Nowe Bemowo", TramStop.DIRECTION_BOTH);

            /**LINE 24*/
            insertLineStop("24", 0, "Nowe Bemowo", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 1, "Wrocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 2, "Radiowa", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 3, "Orlich Gniazd", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 4, "Marynin", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 5, "Koło", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 6, "Dalibora", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 7, "Deotymy", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 8, "Magistracka", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 9, "Majakowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 10, "Wawrzyszewska", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 11, "Młynów", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 12, "Długosza", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 13, "Młynarska", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 14, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 15, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 16, "Muzeum Powstania Warszawskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 17, "Rondo Daszyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 18, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 19, "Plac Starynkiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 20, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 21, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 22, "Krucza", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 23, "Muzeum Narodowe", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 24, "Most Poniatowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 25, "Rondo Waszyngtona", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 26, "Berezyńska", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 27, "Park Skaryszewski", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 28, "Kinowa", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 29, "Grenadierów", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 30, "Wiatraczna", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 31, "Czapelska", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 32, "Wspólna Droga", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 33, "Plac Szembeka", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 34, "Żółkiewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 35, "Kwatery Głównej", TramStop.DIRECTION_BOTH);
            insertLineStop("24", 36, "Gocławek", TramStop.DIRECTION_BOTH);

            /**LINE 25*/
            insertLineStop("25", 0, "Banacha", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 1, "Bitwy Warszawskiej 1920", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 2, "Och Teatr", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 3, "Wawelska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 4, "Plac Narutowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 5, "Ochota Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 6, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 7, "Plac Starykiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 8, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 9, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 10, "Krucza", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 11, "Muzeum Narodowe", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 12, "Most Poniatowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 13, "Rondo Waszyngtona", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 14, "Aleja Zieleniecka", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 15, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 16, "Ząbkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 17, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 18, "Inżynierska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 19, "Bródnowska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 20, "Rondo Żaba", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 21, "Staniewicka", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 22, "Pożarowa", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 23, "Budowlana", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 24, "Julianowska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 25, "Rembielińska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 26, "Poborzańska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 28, "Kondratowicza", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 29, "Toruńska", TramStop.DIRECTION_BOTH);
            insertLineStop("25", 30, "Annopol", TramStop.DIRECTION_BOTH);

            /**LINE 26*/
            insertLineStop("26", 0, "Wiatraczna", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 1, "Wiatraczna ", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 2, "Kickiego", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 3, "Międzyborska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 4, "Praga Południe - Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 5, "Gocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 6, "Bliska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 7, "Lubelska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 8, "Aleja Zieleniecka", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 9, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 10, "Ząbkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 11, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 12, "Park Praski", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 13, "Stare Miasto", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 14, "Metro Ratusz Arsenł", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 15, "Kino Femina", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 16, "Wola Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 17, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 18, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 19, "Płocka", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 20, "Sokołowska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 21, "Osiedle Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 22, "Elekcyjna", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 23, "Cmentarz Prawosławny", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("26", 24, "Reduta Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 25, "Sowińskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 26, "Fort Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 27, "Połczyńska P+R", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 28, "Ciepłownia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 29, "Synów Pułku", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 30, "Hala Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 31, "Plac Kasztelański", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("26", 32, "Czumy", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 33, "Bemowo Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 34, "Klemensiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("26", 35, "Osiedle Górczewska", TramStop.DIRECTION_BOTH);

            /**LINE 27*/
            insertLineStop("27", 0, "Cmentarz Wolski", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 1, "Sowińskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 2, "Reduta Wolska", TramStop.DIRECTION_BOTH);
            //first
            insertLineStop("27", 3, "Cmentarz Prawosławny", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("27", 4, "Elekcyjna", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 5, "Osiedle Wolska", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 6, "Sokołowska", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 7, "Płocka", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 8, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 9, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 10, "Żytnia", TramStop.DIRECTION_BOTH);
            //last
            insertLineStop("27", 11, "Dzielna", TramStop.DIRECTION_LAST_STOP);
            insertLineStop("27", 12, "Cmentarz Żydowski", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 13, "Niska", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 14, "Powązkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 15, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 16, "Plac Grunwaldzki", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 17, "Księdza Popiełuszki", TramStop.DIRECTION_BOTH);
            insertLineStop("27", 18, "Metro Marymont", TramStop.DIRECTION_BOTH);

            /**LINE 28*/
            insertLineStop("28", 0, "Dworzec Wschodni", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 1, "Kijowska", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 2, "Ząbkowska", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 3, "Dworzec Wileński", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 4, "Ratuszowa-ZOO", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 5, "Plac Hallera", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 6, "Rondo Starzyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 7, "Wybrzeże Helskie", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 8, "Most Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 9, "Park Traugutta", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 10, "Dworzec Gdański", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 11, "Baseny Inflancka", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 12, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 13, "Plac Grunwaldzki", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 14, "Sady Żoliborskie", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 15, "Włościańska", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 16, "Park Olszyna", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 17, "Duracza", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 18, "Piaski ", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 19, "Aleja Reymonta", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 20, "Conrada", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 21, "Ogrody Działkowe Bemowo", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 22, "Piastów Śląskich", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 23, "Nowe Bemowo ", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 24, "Wrocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 25, "Radiowa", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 26, "Dywizjonu 303", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 27, "Kazubów", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 28, "Bemowo Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 29, "Klemensiewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("28", 30, "Osiedle Górczewska", TramStop.DIRECTION_BOTH);

            /**LINE 31*/
            insertLineStop("31", 1, "Metro Wilanowska", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 2, "Bukowińska", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 3, "Królikarnia", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 4, "Metro Wierzbno", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 5, "Telewizja Polska", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 6, "Samochodowa", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 7, "Woronicza ", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 8, "Konstruktorska", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 9, "Domaniewska", TramStop.DIRECTION_BOTH);
            insertLineStop("31", 10, "Rzymowskiego", TramStop.DIRECTION_BOTH);

            /**LINE 33*/
            insertLineStop("33", 0, "Metro Młociny", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 1, "Nocznickiego", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 2, "Popiela", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 3, "Bogusławskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 4, "Cmentarz Wawrzyszewski", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 5, "Aspekt", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 6, "Aleja Reymonta", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 7, "Piaski ", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 8, "Duracza", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 9, "Park Olszyna", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 10, "Włościańska", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 11, "Sady Żoliborskie", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 12, "Plac Grunwaldzki", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 13, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 14, "Stawki", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 15, "Anielewicza", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 16, "Nowolipki", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 17, "Kino Femina", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 18, "Hala Mirowska", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 19, "Rondo ONZ", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 20, "Dworzec Centralny", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 21, "Koszykowa", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 22, "Nowowiejska", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 23, "GUS", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 24, "Biblioteka Narodowa", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 25, "Metro Pole Mokotowskie", TramStop.DIRECTION_BOTH);
            insertLineStop("33", 26, "Kielecka", TramStop.DIRECTION_BOTH);

            /**LINE 35*/
            insertLineStop("35", 0, "Wyścigi", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 1, "Aleja Lotników", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 2, "Niedźwiedzia", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 3, "Metro Wilanowska", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 4, "Bukowińska", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 5, "Królikarnia", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 6, "Malczewskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 7, "Park Dreszera", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 8, "Morskie Oko", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 9, "Dworkowa", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 10, "Rakowiecka", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 11, "Plac Unii Lubelskiej", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 12, "Trasa Łazienkowska", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("35", 13, "Plac Zbawiciela", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 14, "Plac Konstytucji", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 15, "Hoża", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 16, "Centrum", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 17, "Metro Świętokrzyska", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 18, "Królewska", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 19, "Plac Bankowy", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 20, "Metro Ratusz Arsenał", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 21, "Muranów", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 22, "Muranowska", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 23, "Dzika", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 24, "Stawki", TramStop.DIRECTION_FIRST_STOP);
            insertLineStop("35", 25, "Rondo Radosława", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 26, "Plac Grunwaldzki", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 27, "Sady Żoliborskie", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 28, "Włościańska", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 29, "Park Olszyna", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 30, "Duracza", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 31, "Piaski ", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 32, "Aleja Reymonta", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 33, "Conrada", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 34, "Ogrody Działkowe Bemowo", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 35, "Piastów Śląskich", TramStop.DIRECTION_BOTH);
            insertLineStop("35", 36, "Nowe Bemowo", TramStop.DIRECTION_BOTH);

            /**LINE 44*/
            insertLineStop("44", 0, "Nowe Bemowo", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 1, "Wrocławska", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 2, "Radiowa", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 3, "Orlich Gniazd", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 4, "Marynin", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 5, "Koło", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 6, "Dalibora", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 7, "Deotymy", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 8, "Magistracka", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 9, "Majakowskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 10, "Wawrzyszewska", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 11, "Młynów", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 12, "Długosza", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 13, "Młynarska", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 14, "Zajezdnia Wola", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 15, "Okopowa", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 16, "Muzeum Powstania Warszawskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 17, "Rondo Daszyńskiego", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 18, "Plac Zawiszy", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 19, "Ochota Ratusz", TramStop.DIRECTION_BOTH);
            insertLineStop("44", 20, "Plac Narutowicza", TramStop.DIRECTION_BOTH);

            db.close();
        }
    }

    private long insertStop(String name,  double lat, double lon){

        ContentValues stopValues = new ContentValues();
        stopValues.put(FeedEntryStops.NAME, name);
        stopValues.put(FeedEntryStops.LATITUDE, lat);
        stopValues.put(FeedEntryStops.LONGITUDE, lon);

        return db.insert(FeedEntryStops.TABLE_NAME, null, stopValues);
    }

    private long insertLineStop(String line,  int number, String name, String direction){

        ContentValues lineStopValues = new ContentValues();

        lineStopValues.put(FeedEntryLines.LINE, line);
        lineStopValues.put(FeedEntryLines.STOP_NUMBER, number);
        lineStopValues.put(FeedEntryLines.STOP_NAME, name);
        lineStopValues.put(FeedEntryLines.DIRECTION, direction);

        return db.insert(FeedEntryLines.TABLE_NAME, null, lineStopValues);
    }


    /**Returns list of stops of specified line*/
    public List<TramStop> getLineStops(String line){

        List<TramStop> list = new ArrayList<>();
        String[] conditionsTable = {line};

        synchronized (dbHelper) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT s." + FeedEntryStops.NAME + ", s." + FeedEntryStops.LATITUDE +
                        ", s." + FeedEntryStops.LONGITUDE + " , l." + FeedEntryLines.DIRECTION +
                        " FROM " + FeedEntryStops.TABLE_NAME + " AS s JOIN " + FeedEntryLines.TABLE_NAME + " AS l " +
                        " ON " + FeedEntryLines.STOP_NAME + " = " + FeedEntryStops.NAME +
                        " WHERE " + FeedEntryLines.LINE + " = ?" +
                        " ORDER BY l." + FeedEntryLines.STOP_NUMBER, conditionsTable);

            while (cursor != null && cursor.moveToNext()) {
               list.add(TramStopFactory.createStop(cursor.getString(0),new LatLng(Double.parseDouble(cursor.getString(1)),
                       Double.parseDouble(cursor.getString(2))), new String[]{line}, cursor.getString(3)));
            }

            db.close();
            try {
                cursor.close();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return list;
    }

    /**gets all lines available for stop*/
    public String[] getStopLines(String name){

        String[] lines;
        String[] columns = {FeedEntryLines.LINE};
        String[] conditions = new String[]{name};

        synchronized (dbHelper) {
            db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(FeedEntryLines.TABLE_NAME,
                    columns, FeedEntryLines.STOP_NAME + " = ? ", conditions, null, null, null, null);

            lines = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                lines[i] = cursor.getString(0);
                i++;
            }
            db.close();
            try {
                cursor.close();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return lines;
    }

    public  List<TramStop> getAllStops(){

        List<TramStop> stopList = new ArrayList<>();

        synchronized (dbHelper) {
            db = dbHelper.getReadableDatabase();

            String[] columns = {FeedEntryStops.NAME, FeedEntryStops.ID, FeedEntryStops.LATITUDE, FeedEntryStops.LONGITUDE};
            Cursor cursor = db.query(FeedEntryStops.TABLE_NAME, columns, null, null, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                stopList.add(new TramStop(cursor.getString(0),
                        new LatLng(Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3)))));
            }

            db.close();
            try {
                cursor.close();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return stopList;
    }
}