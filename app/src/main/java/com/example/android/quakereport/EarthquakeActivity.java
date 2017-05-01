/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private final int EARTHQUAKE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        if (isConnectedToInternet()) {
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            removeProgressBar();
            removeNumberOfEarthquakesTextView();
            removeSeparatorView();
            setNoDataTextView(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {

        Log.i(LOG_TAG, "onCreateLoader()");
        return new EarthquakeLoader(EarthquakeActivity.this, USGSQueryURLsStringMaker.makeURL());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG, "onLoadFinished()");

        removeProgressBar();

        if (earthquakes.isEmpty()) {
            setNoDataTextView(R.string.no_data_found);
            removeNumberOfEarthquakesTextView();
            removeSeparatorView();
        }

        initializeListView(earthquakes);

        updateNumberOfEarthquakes(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "onLoaderReset()");
        initializeListView(new ArrayList<Earthquake>());
    }

    private void initializeListView(List<Earthquake> earthquakes) {

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        final EarthquakeArrayAdapter earthquakeArrayAdapter = new EarthquakeArrayAdapter(EarthquakeActivity.this, earthquakes);

        earthquakeListView.setAdapter(earthquakeArrayAdapter);

        earthquakeListView.setEmptyView(findViewById(R.id.no_data_found_text_view));

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String USGS_URL = earthquakeArrayAdapter.getItem(position).getUSGS_URL();
                openEarthquakePage(USGS_URL);
            }
        });
    }

    private void updateNumberOfEarthquakes(List<Earthquake> earthquakes) {
        TextView numberOfEarthquakesTextView = (TextView) findViewById(R.id.number_of_earthquakes_textview);
        numberOfEarthquakesTextView.setText(getResources().getString(R.string.total_earthquakes_found) + earthquakes.size());
    }

    private boolean isConnectedToInternet() {

        boolean connected;
        ConnectivityManager connectionManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectionManager.getActiveNetworkInfo();

        connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return connected;
    }

    private void openEarthquakePage(String USGS_URL) {

        Intent webPageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(USGS_URL));
        this.startActivity(webPageIntent);
    }

    private void setNoDataTextView(int resourceID) {

        TextView noDataFoundTextView = (TextView) findViewById(R.id.no_data_found_text_view);
        noDataFoundTextView.setText(resourceID);
    }

    private void removeSeparatorView() {
        findViewById(R.id.separator_view).setVisibility(GONE);
    }

    private void removeNumberOfEarthquakesTextView() {
        findViewById(R.id.number_of_earthquakes_textview).setVisibility(GONE);
    }

    private void removeProgressBar() {
        findViewById(R.id.progress_bar).setVisibility(GONE);
    }
}
