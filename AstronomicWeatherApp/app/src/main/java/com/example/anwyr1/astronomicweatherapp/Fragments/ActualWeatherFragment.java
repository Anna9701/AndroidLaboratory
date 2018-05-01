package com.example.anwyr1.astronomicweatherapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActualWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActualWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO display weather info
public class ActualWeatherFragment extends Fragment {
    private static final String firstUrlWeatherApiPart = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String secondUrlWeatherApiPart = "&mode=xml&appid=6568cca14ced23610c0a31b4f0bc5562&units=";
    private static String currentCity;
    private static String units;
    private static CurrentWeather currentWeather;

    private OnFragmentInteractionListener mListener;

    public ActualWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActualWeatherFragment.
     */
    public static ActualWeatherFragment newInstance(String param1, String param2) {
        ActualWeatherFragment fragment = new ActualWeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actual_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshCurrentWeather();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void setCurrentWeatherView() {
        TextView placeTextView = getView().findViewById(R.id.current_weather_place_name);
        TextView sunriseTextView = getView().findViewById(R.id.current_weather_sunrise);
        TextView sunsetTextView = getView().findViewById(R.id.current_weather_sunset);
        TextView currentTempTextView = getView().findViewById(R.id.current_weather_temp_value);
        TextView maxTempTextView = getView().findViewById(R.id.current_weather_temp_max);
        TextView minTempTextView = getView().findViewById(R.id.current_weather_temp_min);
        TextView tempUnitTextView = getView().findViewById(R.id.current_weather_temp_unit_name);
        TextView humidityTextView = getView().findViewById(R.id.currentWeather_humidity_value);
        TextView pressureTextView = getView().findViewById(R.id.currentWeather_pressure_value);
        TextView cloudsTextView = getView().findViewById(R.id.currentWeather_clouds_value);
        TextView precipitationTextView = getView().findViewById(R.id.currentWeather_precipitation_mode);
        TextView windNameTextView = getView().findViewById(R.id.currentWeather_wind_name);
        TextView windDirectionTextView = getView().findViewById(R.id.currentWeather_wind_direction);
        TextView windSpeedTextView = getView().findViewById(R.id.currentWeather_wind_speed);
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

    public void refreshCurrentWeather() throws IOException {
        currentCity = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_city_key),
                getResources().getString(R.string.pref_weather_cities_default_city), getContext());
        currentCity = currentCity.replaceAll("\\s","");
        units = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_units_key),
                getResources().getString(R.string.pref_default_display_unit_value), getContext());
        try {
            loadXmlFromNetworkAndRefreshData(firstUrlWeatherApiPart + currentCity +
                    secondUrlWeatherApiPart + units);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        setCurrentWeatherView();
    }

    private void loadXmlFromNetworkAndRefreshData(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        ActualWeatherXmlParser actualWeatherXmlParser = new ActualWeatherXmlParser();
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
            throw ex;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private void printNoCurrentWeatherAvailableAlert() {
        printNoInternetAccessAlert("There is no connection to internet available. " +
                "We have no any current weather data saved. Please, connect to internet to get " +
                "current weather data.");
    }

    private void printNoInternetAccessAlert(final String alertMessage) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
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
        });
    }

    private void printNonActualCurrentWeatherAlert() {
        printNoInternetAccessAlert("There is no connection to internet available. " +
                "Presented current weather data can be non actual. To update data, please, " +
                "connect to internet.");
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
