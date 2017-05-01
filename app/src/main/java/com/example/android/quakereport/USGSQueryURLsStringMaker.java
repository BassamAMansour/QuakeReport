package com.example.android.quakereport;

/**
 * Created by Bassam on 5/1/2017.
 */

public class USGSQueryURLsStringMaker {

    private static String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=5&limit=20";

    private USGSQueryURLsStringMaker() {
    }

    public static String makeURL() {

        return url;
    }

}
