package com.example.miapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class Utils {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Devuelve un ArrayList de Terremotos que obtendrá de la API
     */
    public static ArrayList<Terremoto> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Terremoto> terremotos = extractFeatureFromJson(jsonResponse);

        return terremotos;
    }

    /**
     * Devuelve un objeto URL a partir del string proporcionado.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Realiza una petición HTTP a partir de la URL y devuelve un String con la respuesta JSON.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convierte el {@link InputStream} en un String que contiene
     * toda la respuesta JSON del servidor.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Devuelve un ArrayList de Terremotos a partir del String JSON proporcionado.
     */
    private static ArrayList<Terremoto> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        ArrayList<Terremoto> terremotos = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(earthquakeJSON);
            JSONArray array = rootJsonObject.optJSONArray("features");

            for (int i=0; i<array.length(); i++){
                JSONObject unTerremoto = array.optJSONObject(i);
                JSONObject propiedades = unTerremoto.optJSONObject("properties");
                Double magnitud = propiedades.optDouble("mag");
                String ciudad = propiedades.optString("place");
                Long fecha = propiedades.optLong("time");

                Date dateObject = new Date(fecha);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);

                Terremoto terremoto= new Terremoto(magnitud,ciudad, dateToDisplay);
                terremotos.add(terremoto);
            }
            return terremotos;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}
