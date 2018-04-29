package com.example.anwyr1.astronomicweatherapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastData;
import com.example.anwyr1.astronomicweatherapp.R;
import com.example.anwyr1.astronomicweatherapp.SettingsActivity;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ForecastXmlParser;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ObjectSerializerHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
    private static final String firstUrlWeatherApiPart = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private static final String secondUrlWeatherApiPart = "&mode=xml&appid=6568cca14ced23610c0a31b4f0bc5562";
    private static String currentCity;
    private static ForecastData forecastData;

    private OnFragmentInteractionListener mListener;

    public ForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(firstUrlWeatherApiPart, param1);
        args.putString(secondUrlWeatherApiPart, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(firstUrlWeatherApiPart);
            String mParam2 = getArguments().getString(secondUrlWeatherApiPart);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        currentCity = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_city_key),
                getResources().getString(R.string.pref_weather_cities_default_city), getContext());
        currentCity = currentCity.replaceAll("\\s","");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadXmlFromNetwork(firstUrlWeatherApiPart + currentCity + secondUrlWeatherApiPart);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        ForecastXmlParser forecastXmlParser = new ForecastXmlParser();
        try {
            stream = downloadUrl(urlString);
            forecastData = forecastXmlParser.parse(stream);
            String forecastToSave = ObjectSerializerHelper.objectToString(forecastData);
            SettingsActivity.setSettings("forecastDataSaved", forecastToSave, getContext());
        } catch (Exception ex) {
            String savedForecastData = SettingsActivity.getFromSettings("forecastDataSaved",
                    null, getContext());
            if (savedForecastData != null) {
                forecastData = (ForecastData) ObjectSerializerHelper.stringToObject(savedForecastData);
                printNonActualForecastAlert();
            } else {
                printNoForecastAvailableAlert();
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private void printNoForecastAvailableAlert() {
        printNoInternetAccessAlert("There is no connection to internet available. " +
                "We have no any forecast saved. Please, connect to internet to get forecast data.");
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

    private void printNonActualForecastAlert() {
        printNoInternetAccessAlert("There is no connection to internet available. " +
                "Presented forecast can be non actual. To update data, please, connect to internet.");
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

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
