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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        initializeListView();
    }

    private void initializeListView() {
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        final EarthquakeArrayAdapter earthquakeArrayAdapter = new EarthquakeArrayAdapter(EarthquakeActivity.this, earthquakes);

        earthquakeListView.setAdapter(earthquakeArrayAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String USGS_URL = earthquakeArrayAdapter.getItem(position).getUSGS_URL();
                openEarthquakePage(USGS_URL);
            }
        });
    }

    private void openEarthquakePage(String USGS_URL) {

        Intent webPageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(USGS_URL));
        this.startActivity(webPageIntent);
    }
}
