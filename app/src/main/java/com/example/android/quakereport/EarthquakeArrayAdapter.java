package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 2/22/2017.
 */

public class EarthquakeArrayAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = "of ";
    private static final byte SECONDARY_LOCATION_INDEX = 0;
    private static final byte PRIMARY_LOCATION_INDEX = 1;

    public EarthquakeArrayAdapter(Context context, List<Earthquake> earthquakeArrayList) {
        super(context, 0, earthquakeArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        Earthquake earthquake = getItem(position);

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        modifyMagnitudeTextView(listItemView, getFormattedDecimal(earthquake.getMagnitude()));

        modifyMagnitudeCircleColor(listItemView, earthquake.getMagnitude());

        modifyPrimaryLocationTextView(listItemView, extractPrimaryLocation(earthquake.getLocation()));

        modifySecondaryLocationTextView(listItemView, extractSecondaryLocation(earthquake.getLocation()));

        modifyDateTextView(listItemView, returnFormattedDateString(earthquake.getTimeUNIX()));

        modifyTimeTextView(listItemView, returnFormattedTimeString(earthquake.getTimeUNIX()));

        return listItemView;
    }

    private void modifyMagnitudeTextView(View rootView, String magnitude) {

        TextView magnitudeTextView = (TextView) rootView.findViewById(R.id.magnitude_text_view);
        magnitudeTextView.setText(magnitude);
    }

    private void modifyMagnitudeCircleColor(View rootView, double magnitude) {

        TextView magnitudeTextView = (TextView) rootView.findViewById(R.id.magnitude_text_view);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(magnitude));
    }

    private void modifyPrimaryLocationTextView(View rootView, String primaryLocation) {

        TextView locationTextView = (TextView) rootView.findViewById(R.id.primary_location_text_view);
        locationTextView.setText(primaryLocation);
    }

    private void modifySecondaryLocationTextView(View rootView, String secondaryLocation) {

        TextView locationTextView = (TextView) rootView.findViewById(R.id.secondary_location_text_view);
        locationTextView.setText(secondaryLocation);
    }

    private void modifyDateTextView(View rootView, String date) {

        TextView dateTextView = (TextView) rootView.findViewById(R.id.date_text_view);
        dateTextView.setText(date);
    }

    private void modifyTimeTextView(View rootView, String time) {

        TextView timeTextView = (TextView) rootView.findViewById(R.id.time_text_view);
        timeTextView.setText(time);
    }

    private String getFormattedDecimal(double decimal) {

        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        return decimalFormatter.format(decimal);
    }

    private String returnFormattedTimeString(long timeUNIX) {

        Date dateObject = new Date(timeUNIX);
        SimpleDateFormat formattedTime = new SimpleDateFormat("hh:mm a");
        return formattedTime.format(dateObject);

    }

    private String returnFormattedDateString(long timeUNIX) {

        Date dateObject = new Date(timeUNIX);
        SimpleDateFormat formattedDate = new SimpleDateFormat("MMM dd, yyyy");
        return formattedDate.format(dateObject);
    }

    private String extractPrimaryLocation(String location) {

        if (location.contains(LOCATION_SEPARATOR)) {
            String[] splittedLocationArray = location.split(LOCATION_SEPARATOR);
            return splittedLocationArray[PRIMARY_LOCATION_INDEX];
        } else {
            return location;
        }
    }

    private String extractSecondaryLocation(String location) {

        if (location.contains(LOCATION_SEPARATOR)) {
            String[] splittedLocationArray = location.split(LOCATION_SEPARATOR);
            return splittedLocationArray[SECONDARY_LOCATION_INDEX];
        } else {
            return getContext().getString(R.string.close_to);
        }
    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorInt;

        switch ((int) magnitude) {
            case 0:
            case 1:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 7:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            default:
                magnitudeColorInt = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                break;
        }
        return magnitudeColorInt;
    }
}
