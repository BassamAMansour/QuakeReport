package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by Bassam on 5/1/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private final String LOG_TAG = EarthquakeLoader.class.getName();
    private String QueryURL;

    public EarthquakeLoader(Context context, String QueryURL) {

        super(context);

        this.QueryURL = QueryURL;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "onStartLoading()");
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground()");
        return QueryUtils.makeEarthquakesQuery(QueryURL);
    }
}
