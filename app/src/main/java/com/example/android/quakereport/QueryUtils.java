package com.example.android.quakereport;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    final static String LOG_TAG = QueryUtils.class.getName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static List<Earthquake> makeEarthquakesQuery(String urlString) {

        List<Earthquake> earthquakesList = new ArrayList<>();

        URL url = makeURL(urlString);

        String JSONResponseString = "";

        try {
            JSONResponseString = fetchJSONStringFromURL(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting the JSON response from the srever", e);
        }

        if (JSONResponseString.equals("")) {
            Log.e(LOG_TAG, "Empty JSON response string");
        } else {
            earthquakesList = extractEarthquakesList(JSONResponseString);
        }

        return earthquakesList;

    }

    private static String fetchJSONStringFromURL(URL url) throws IOException {

        String JSONResponseString = "";

        if (url == null) {
            return JSONResponseString;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                JSONResponseString = getJSONStringFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error with the response code\nResponse code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with the network retreaving the JSON Response", e);
        } finally {

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return JSONResponseString;
    }

    private static String getJSONStringFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder JSONString = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                JSONString.append(readLine);
                readLine = bufferedReader.readLine();
            }
        }
        return JSONString.toString();
    }


    private static List<Earthquake> extractEarthquakesList(String JSONResponseString) {
        /**
         * Return a list of {@link Earthquake} objects that has been built up from
         * parsing a JSON response.
         */
        List<Earthquake> earthquakes = new ArrayList<>();

        try {

            JSONObject rootObject = new JSONObject(JSONResponseString);

            JSONArray quakesArray = rootObject.getJSONArray("features");

            for (int i = 0; i < quakesArray.length(); i++) {

                JSONObject quake = quakesArray.getJSONObject(i);
                JSONObject propertiesOfQuake = quake.getJSONObject("properties");

                double magnitude = propertiesOfQuake.getDouble("mag");
                String locationOfQuake = propertiesOfQuake.getString("place");
                long timeUNIX = propertiesOfQuake.getLong("time");
                String USGS_URL = propertiesOfQuake.getString("url");

                Earthquake earthquake = new Earthquake(locationOfQuake, timeUNIX, magnitude, USGS_URL);
                earthquakes.add(i, earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

    private static URL makeURL(String urlString) {

        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed url", e);
        }
        return url;
    }
}