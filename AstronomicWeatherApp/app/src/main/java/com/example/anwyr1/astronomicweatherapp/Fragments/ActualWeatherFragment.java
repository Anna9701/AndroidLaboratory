package com.example.anwyr1.astronomicweatherapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anwyr1.astronomicweatherapp.R;
import com.example.anwyr1.astronomicweatherapp.SettingsActivity;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeather;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ActualWeatherXmlParser;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ObjectSerializerHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActualWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
//TODO check and set correctly display on tablets
public class ActualWeatherFragment extends Fragment {
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.current_weather_place_name)TextView placeTextView;
    @BindView(R.id.current_weather_sunrise)TextView sunriseTextView;
    @BindView(R.id.current_weather_sunset)TextView sunsetTextView;
    @BindView(R.id.current_weather_temp_value)TextView currentTempTextView;
    @BindView(R.id.current_weather_temp_max)TextView maxTempTextView;
    @BindView(R.id.current_weather_temp_min)TextView minTempTextView;
    @BindView(R.id.current_weather_temp_unit_name)TextView tempUnitTextView;
    @BindView(R.id.currentWeather_humidity_value)TextView humidityTextView;
    @BindView(R.id.currentWeather_pressure_value)TextView pressureTextView;
    @BindView(R.id.currentWeather_clouds_value)TextView cloudsTextView;
    @BindView(R.id.currentWeather_precipitation_mode)TextView precipitationTextView;
    @BindView(R.id.currentWeather_wind_name)TextView windNameTextView;
    @BindView(R.id.currentWeather_wind_direction)TextView windDirectionTextView;
    @BindView(R.id.currentWeather_wind_speed)TextView windSpeedTextView;

    public ActualWeatherFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_actual_weather, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        refreshCurrentWeather();
    }

    private void setCurrentWeatherView(final CurrentWeather currentWeather) {
        placeTextView.setText(String.format("%s, %s", currentWeather.getCity().getName(), currentWeather.getCity().getCountry().getCountryCode()));
        sunriseTextView.setText(currentWeather.getCity().getSun().getRise());
        sunsetTextView.setText(currentWeather.getCity().getSun().getSet());
        tempUnitTextView.setText(currentWeather.getTemperature().getUnit());
        currentTempTextView.setText(currentWeather.getTemperature().getValue());
        maxTempTextView.setText(currentWeather.getTemperature().getMax());
        minTempTextView.setText(currentWeather.getTemperature().getMin());
        humidityTextView.setText(String.format("%s%s", currentWeather.getHumidity().getValue(), currentWeather.getHumidity().getUnit()));
        pressureTextView.setText(String.format("%s %s", currentWeather.getPressure().getValue(), currentWeather.getPressure().getUnit()));
        precipitationTextView.setText(String.format("precipitation: %s", currentWeather.getPrecipitation().getMode()));
        cloudsTextView.setText(currentWeather.getClouds().getName());
        windNameTextView.setText(currentWeather.getWind().getSpeed().getName());
        windDirectionTextView.setText(currentWeather.getWind().getDirection().getName());
        windSpeedTextView.setText(currentWeather.getWind().getSpeed().getValue());
    }

    public void refreshCurrentWeather() {
        Single<CurrentWeather> currentWeatherSingle = Single.create(emitter -> {
            String currentCity = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_city_key),
                    getResources().getString(R.string.pref_weather_cities_default_city), getContext());
            currentCity = currentCity.replaceAll("\\s","");
            String units = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_units_key),
                    getResources().getString(R.string.pref_default_display_unit_value), getContext());
            CurrentWeather currentWeather = loadXmlFromNetworkAndRefreshData(getString(R.string.firstPartOfWeatherWeatherApiUrl) + currentCity +
                    getString(R.string.secondPartOfWeatherApiUrl) + units);
            emitter.onSuccess(currentWeather);
        });
        currentWeatherSingle
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::setCurrentWeatherView);
    }

    private CurrentWeather loadXmlFromNetworkAndRefreshData(String urlString) {
        InputStream stream = null;
        ActualWeatherXmlParser actualWeatherXmlParser = new ActualWeatherXmlParser();
        CurrentWeather currentWeather = null;
        try {
            stream = downloadUrl(urlString);
            currentWeather = actualWeatherXmlParser.parse(stream);
            String weatherToSave = ObjectSerializerHelper.objectToString(currentWeather);
            SettingsActivity.setSettings("actualWeatherSaved", weatherToSave, getContext());
        } catch (Exception ex) {
            String savedCurrentWeather = SettingsActivity.getFromSettings("actualWeatherSaved",
                    null , getContext());
            if (savedCurrentWeather != null) {
                currentWeather = (CurrentWeather) ObjectSerializerHelper.stringToObject(savedCurrentWeather);
                printNonActualCurrentWeatherAlert();
            } else {
                printNoCurrentWeatherAvailableAlert();
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return currentWeather;
    }

    private void printNoCurrentWeatherAvailableAlert() {
        final String alertMsg = "There is no connection to internet available. " +
                "We have no any current weather data saved. Please, connect to internet to get " +
                "current weather data.";
        final Single<String> noCurrentWeatherAvailableSingle = Single.just(alertMsg);
        noCurrentWeatherAvailableSingle
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::printNoInternetAccessAlert);
    }

    private void printNoInternetAccessAlert(final String alertMessage) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No internet connection available");
        builder.setMessage(alertMessage);
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog alertDialog = builder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            alertDialog.dismiss();
        }
    }

    private void printNonActualCurrentWeatherAlert() {
       final String alertMsg = "There is no connection to internet available. " +
                "Presented current weather data can be non actual. To update data, please, " +
                "connect to internet.";
        final Single<String> noCurrentWeatherAvailableSingle = Single.just(alertMsg);
        noCurrentWeatherAvailableSingle
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::printNoInternetAccessAlert);
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}