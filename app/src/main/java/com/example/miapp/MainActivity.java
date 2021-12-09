package com.example.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    /** URL de información de terremotos de USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2021-12-31&minfelt=50&minmagnitude=7&latitude=40.69894312747046&longitude=-101.27966420433462&maxradiuskm=10000";
    private ArrayList<Terremoto> terremotos;
    private AdapterTerremotos adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        /**Creo un hilo para ejecutar en segundo plano una consulta a la API*/
        executor.execute(new Runnable() {
            @Override
            public void run() {

                terremotos = Utils.fetchEarthquakeData(USGS_REQUEST_URL);
                /**Inicializo el adapter con el array de terremotos después de que acabe la consulta*/
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new AdapterTerremotos(MainActivity.this,terremotos);
                        earthquakeListView.setAdapter(adapter);
                    }
                });
            }
        });
    }

}