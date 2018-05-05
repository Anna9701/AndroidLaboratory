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

import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastData;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Precipitation;
import com.example.anwyr1.astronomicweatherapp.Forecast.ThreeHoursForecast;
import com.example.anwyr1.astronomicweatherapp.R;
import com.example.anwyr1.astronomicweatherapp.SettingsActivity;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ForecastXmlParser;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ObjectSerializerHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
//TODO check and set correctly display on tablets
public class ForecastFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private static ForecastData forecastData;
    private static int currentForecastPeriod = 0;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.cityTextView)TextView cityNameTextView;
    @BindView(R.id.forecast_0_general_weather_description)TextView generalWeatherDescriptionTextView;
    @BindView(R.id.forecast_0_time_from)TextView timeFromTextView;
    @BindView(R.id.forecast_0_time_to)TextView timeToTextView;
    @BindView(R.id.forecast_0_wind_direction)TextView windDirectionTextView;
    @BindView(R.id.forecast_0_wind_speed)TextView windSpeedTextView;
    @BindView(R.id.forecast_0_wind_name)TextView windNameTextView;
    @BindView(R.id.forecast_0_temp_unit)TextView tempUnitTextView;
    @BindView(R.id.forecast_0_temp_avg)TextView tempAvgTextView;
    @BindView(R.id.forecast_0_temp_max)TextView tempMaxTextView;
    @BindView(R.id.forecast_0_temp_min)TextView tempMinTextView;
    @BindView(R.id.forecast_0_humidity)TextView humidityTextView;
    @BindView(R.id.forecast_0_pressure)TextView pressureTextView;
    @BindView(R.id.forecast_0_clouds_description)TextView cloudsDescTextView;
    @BindView(R.id.forecast_0_clouds_all_value)TextView cloudsAllValueTextView;
    @BindView(R.id.forecast_0_precipitation_amount)TextView precipitationAmountTextView;
    @BindView(R.id.forecast_0_precipitation_type)TextView precipitationTypeTextView;
    @BindView(R.id.forecast_no_precipitation)TextView precipitationNullTextView;

    public ForecastFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        String currentForecastPeriodSaved = SettingsActivity.getFromSettings(getString(R.string.currentForecastPeriodKey), "0", getContext());
//        currentForecastPeriod = Integer.parseInt(currentForecastPeriodSaved);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            refreshCurrentWeather();
            }
        });
        thread.start();
    }

    @Override
    public void onDestroy() {
        //SettingsActivity.setSettings(getString(R.string.currentForecastPeriodKey), String.valueOf(currentForecastPeriod), getContext());
        super.onDestroy();
    }

    @OnClick(R.id.forecast_prev_button)
    public void loadPreviousForecastPeriod() {
        if (--currentForecastPeriod < 0) {
            displayAlert("Error", "You're watching first forecast period. Cannot turn back more!");
            currentForecastPeriod = 0;
        }
        updateDataView();
    }

    @OnClick({R.id.forecast_next_button})
    public void loadNextForecastPeriod() {
        if (++currentForecastPeriod == forecastData.getForecastList().size()) {
            displayAlert("Error", "You're watching last forecast period. Cannot go forward any further!");
            --currentForecastPeriod;
        }
        updateDataView();
    }

    private void loadXmlFromNetworkAndRefreshData(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
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
                displayNonActualForecastAlert();
            } else {
                displayNoForecastAvailableAlert();
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private void displayNoForecastAvailableAlert() {
        String alertContent = "There is no connection to internet available. " +
                "We have no any forecast saved. Please, connect to internet to get forecast data.";
        String alertTitle = "No internet connection available";
        displayAlert(alertTitle, alertContent);
    }

    private void displayAlert(final String alertTitle, final String alertMessage) {
        Single<AlertDialog.Builder> alertDialogSingle = Single.create(emitter -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(alertTitle);
            builder.setMessage(alertMessage);
            builder.setPositiveButton(android.R.string.ok, null);
            emitter.onSuccess(builder);
        });
        alertDialogSingle
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(builder -> {
                AlertDialog alertDialog = null;
                try {
                    alertDialog = builder.create();
                    alertDialog.show();
                } catch (Exception ex) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });
    }

    public void refreshCurrentWeather() {
        String currentCity = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_city_key),
                getResources().getString(R.string.pref_weather_cities_default_city), getContext());
        currentCity = currentCity.replaceAll("\\s","");
        String units = SettingsActivity.getFromSettings(getResources().getString(R.string.weather_units_key),
                getResources().getString(R.string.pref_default_display_unit_value), getContext());
        try {
            loadXmlFromNetworkAndRefreshData(getString(R.string.firstPartOfForecastWeatherApiUrl) + currentCity +
                    getString(R.string.secondPartOfWeatherApiUrl) + units);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        updateDataView();
    }

    private void updateDataView() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ThreeHoursForecast forecast = forecastData.getForecastList().get(currentForecastPeriod);
                    cityNameTextView.setText(String.format("%s, %s", forecastData.getLocation().getName(),
                            forecastData.getLocation().getCountry()));
                    generalWeatherDescriptionTextView.setText(forecast.getWeatherCondition());
                    timeFromTextView.setText(forecast.getTime().getFrom());
                    timeToTextView.setText(forecast.getTime().getTo());
                    windNameTextView.setText(forecast.getWind().getSpeedName());
                    windDirectionTextView.setText(forecast.getWind().getDirection());
                    windSpeedTextView.setText(forecast.getWind().getWindSpeedMps());
                    tempAvgTextView.setText(forecast.getTemperature().getValue());
                    tempMaxTextView.setText(forecast.getTemperature().getMax());
                    tempMinTextView.setText(forecast.getTemperature().getMin());
                    tempUnitTextView.setText(forecast.getTemperature().getUnit());
                    humidityTextView.setText(String.format("%s %s", forecast.getHumidity().getValue(), forecast.getHumidity().getUnit()));
                    pressureTextView.setText(String.format("%s %s", forecast.getPressure().getValue(), forecast.getPressure().getUnit()));
                    cloudsDescTextView.setText(forecast.getClouds().getValue());
                    cloudsAllValueTextView.setText(String.format("Cloudiness: %s%s", forecast.getClouds().getAll(), forecast.getClouds().getUnit()));
                    Precipitation precipitation = forecast.getPrecipitation();
                    if (precipitation != null) {
                        precipitationAmountTextView.setText(precipitation.getValue());
                        precipitationTypeTextView.setText(precipitation.getType());
                        precipitationNullTextView.setText("");
                    } else {
                        precipitationTypeTextView.setText("");
                        precipitationAmountTextView.setText("");
                        precipitationNullTextView.setText(R.string.NullPrecipitationDescription);
                    }
                }
            });
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void displayNonActualForecastAlert() {
        String alertContent = "There is no connection to internet available. " +
                "Presented forecast can be non actual. To update data, please, connect to internet.";
        String alertTitle = "No internet connection available";
        displayAlert(alertTitle, alertContent);
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
