package com.example.anwyr1.astronomicweatherapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeather;
import com.example.anwyr1.astronomicweatherapp.XmlUtils.ActualWeatherXmlParser;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    private static String city1, city2, city3, city4, city5;
    private static ListPreference listPreference;
    private static final String latitudeKey = "latitude";
    private static final String longitudeKey = "longitude";
    private static final String selectedCityKey = "selected_city";
    private static final String astroweatherSourceKey = "astroweatherSource";
    private static final String defaultCity = "Lodz, PL";
    private static final String spaceReplacement = "%20";
    private static final String spacePattern = "\\s";
    private static final String firstPartOpenWeatherApiForCityNameUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String firstPartOpenWeatherApiForCoordsUrl = "http://api.openweathermap.org/data/2.5/weather?lat=";
    private static final String openWeatherApiLongitudePartUrl = "&lon=";
    private static final String secondPartOpenWeatherApiUrl = "&mode=xml&appid=6568cca14ced23610c0a31b4f0bc5562&units=";

    public static boolean isCityByNameEnabled(final Context context) {
        return getBoolFromSettings(astroweatherSourceKey, true, context);
    }

    public static String getFromSettings(final String key, final String defValue, final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defValue);
    }

    public static Boolean getBoolFromSettings(final String key, final boolean defValue, final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defValue);
    }

    public static void setSettings(final String key, final String value, final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void updateLatitudeAmdLongitude(final String city, final Context context) {
        Single<CurrentWeather> currentWeatherSingle = Single.create(emitter -> {
            String searchCity  = city.replaceAll(spacePattern,spaceReplacement);
            CurrentWeather currentWeather = loadCurrentWeatherData(String.format("%s%s%s",
                    firstPartOpenWeatherApiForCityNameUrl, searchCity, secondPartOpenWeatherApiUrl));
            emitter.onSuccess(currentWeather);
        });
        currentWeatherSingle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(currentWeather -> {
                    setSettings(latitudeKey, currentWeather.getCity().getCoord().getLatitude(), context);
                    setSettings(longitudeKey, currentWeather.getCity().getCoord().getLongitude(), context);
                });
    }

    private static void updateSelectedCity(final Context context) {
        Single<CurrentWeather> currentWeatherSingle = Single.create(emitter -> {
            String latitude = getFromSettings(latitudeKey, defaultCity, context);
            String longitude = getFromSettings(longitudeKey, defaultCity, context);
            CurrentWeather currentWeather = loadCurrentWeatherData(String.format("%s%s%s%s%s",
                    firstPartOpenWeatherApiForCoordsUrl, latitude,
                    openWeatherApiLongitudePartUrl, longitude,
                    secondPartOpenWeatherApiUrl));
            emitter.onSuccess(currentWeather);
        });
        currentWeatherSingle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(currentWeather -> {
                    setSettings(selectedCityKey, String.format("%s, %s",
                            currentWeather.getCity().getName(),
                            currentWeather.getCity().getCountry().getCountryCode()) , context);
                });
    }

    private static CurrentWeather loadCurrentWeatherData(final String urlString) {
        try {
            return new ActualWeatherXmlParser().parse(downloadUrl(urlString));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static InputStream downloadUrl(final String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(1000 /* milliseconds */);
        conn.setConnectTimeout(1500 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        private final String invalidCityTitle = "Invalid City Selected";
        private final String invalidCityContent = "Something's gone wrong... There is no such city or you have no access to Internet.";
        private final String invalidCoordsTitle = "Invalid Input";
        private final String invalidCoordsContent = "Something's gone wrong...";
        private final String astroWeatherByCityNameTitle = "Astroweather by City Name Enabled";
        private final String astroWeatherByCityNameContent = "Disable it to set custom coordinates";

        @Override
        public boolean onPreferenceChange(final Preference preference, final Object value) {
            final String stringValue = value.toString();

            if (preference instanceof SwitchPreference) {
                if (value instanceof Boolean && (Boolean)value) {
                    String city = getFromSettings(selectedCityKey,defaultCity, preference.getContext());
                    updateLatitudeAmdLongitude(city, preference.getContext());
                }
            } else if (preference instanceof ListPreference) {
                final ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                if (preference.getKey().equals(selectedCityKey)) {
                    if(isCityValid(stringValue)) {
                        if (isCityByNameEnabled(preference.getContext())) {
                            updateLatitudeAmdLongitude(stringValue, preference.getContext());
                        }
                        return true;
                    }
                    return false;
                }
            } else if (preference instanceof EditTextPreference && (preference.getKey().equals(latitudeKey)
                    || preference.getKey().equals(longitudeKey))) {
                if (!updateLatitudeOrLongitude(preference, stringValue))
                    return false;
                updateSelectedCity(preference.getContext());
            } else if (preference instanceof EditTextPreference) {
                if (updateProperFavoriteCity(preference, stringValue))
                    preference.setSummary(stringValue);
                else
                    return false;
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private boolean updateProperFavoriteCity(final Preference preference, final String stringValue) {
            int numberOfCity;
            final int numberOfCities = 5;
            String key = preference.getKey();
            for (numberOfCity = 0 ; numberOfCity < numberOfCities; ++numberOfCity) {
                if (key.equals("city" + (numberOfCity + 1) + "_value"))
                    break;
            }

            if (!isCityValid(stringValue, numberOfCity)) {
                displayAlert(invalidCityTitle, invalidCityContent, preference.getContext());
                return false;
            }

            switch (numberOfCity) {
                case 0:
                    city1 = stringValue;
                    break;
                case 1:
                    city2 = stringValue;
                    break;
                case 2:
                    city3 = stringValue;
                    break;
                case 3:
                    city4 = stringValue;
                    break;
                case 4:
                    city5 = stringValue;
                    break;
                default:
                    return false;
            }
            NotificationPreferenceFragment.loadCitiesListEntries();
            return true;
        }

        private boolean isCityValid(final String city, final int numberOfCity) {
            if (city.length() == 0 && numberOfCity > 0)
                return true;
            final String cityId = city.replaceAll(spacePattern, spaceReplacement);
            return isCityValid(cityId);
        }

        private boolean isCityValid(final String cityId) {
            if(!cityId.matches("[A-Za-z].*,*[A-Za-z].*"))
                return false;
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<Boolean> result = es.submit(() -> {
                try {
                    downloadUrl(firstPartOpenWeatherApiForCityNameUrl + cityId + secondPartOpenWeatherApiUrl);
                } catch (IOException e) {
                    return false;
                }
                return true;
            });
            try {
                if(!result.get()) {
                    es.shutdown();
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        private boolean updateLatitudeOrLongitude(final Preference preference, final String stringValue) {
            if (isCityByNameEnabled(preference.getContext())) {
                displayAlert(astroWeatherByCityNameTitle, astroWeatherByCityNameContent, preference.getContext());
                return false;
            }
            if (!NumberUtils.isParsable(stringValue)
                    || Double.parseDouble(stringValue) <= -80
                    || Double.parseDouble(stringValue) >= 80) {
                displayAlert(invalidCoordsTitle, invalidCoordsContent, preference.getContext());
                return false;
            }
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<Boolean> result = es.submit(() -> isLatitudeAndLongitudeValid(preference, stringValue));
            try {
                if(!result.get()) {
                    es.shutdown();
                    displayAlert(invalidCoordsTitle, invalidCoordsContent, preference.getContext());
                    return false;
                }
            } catch (Exception e) {
                displayAlert(invalidCoordsTitle, invalidCoordsContent, preference.getContext());
                return false;
            }
            preference.setSummary(stringValue);
            SettingsActivity.setSettings(preference.getKey(), stringValue, preference.getContext());
            return true;
        }

        private void displayAlert(final String title, final String content, final Context context) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(content);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    };

    private static boolean isLatitudeAndLongitudeValid(final Preference preference, final String stringValue) {
        String latitude;
        String longitude;
        if (preference.getKey().equals(latitudeKey)) {
            latitude = stringValue;
            longitude = getFromSettings(longitudeKey, "0", preference.getContext());
        } else {
            latitude = getFromSettings(latitudeKey, "0", preference.getContext());
            longitude = stringValue;
        }
        CurrentWeather currentWeather = loadCurrentWeatherData(String.format("%s%s%s%s%s",
                firstPartOpenWeatherApiForCoordsUrl, latitude,
                openWeatherApiLongitudePartUrl, longitude,
                secondPartOpenWeatherApiUrl));
        return (currentWeather != null ? currentWeather.getCity().getName().length() : 0) != 0;
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(final Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(final Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        if (preference instanceof SwitchPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, "");
        } else
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        city1 = getFromSettings(getResources().getString(R.string.city1_key),
                getResources().getString(R.string.pref_weather_cities_default_city), this);
        city2 = getFromSettings(getResources().getString(R.string.city2_key),
                getResources().getString(R.string.additional_city_def_value), this);
        city3 = getFromSettings(getResources().getString(R.string.city3_key),
                getResources().getString(R.string.additional_city_def_value), this);
        city4 = getFromSettings(getResources().getString(R.string.city4_key),
                getResources().getString(R.string.additional_city_def_value), this);
        city5 = getFromSettings(getResources().getString(R.string.city5_key),
                getResources().getString(R.string.additional_city_def_value), this);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(final List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(final String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.latitude_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.longitude_key)));
        }

        @Override
        public boolean onOptionsItemSelected(final MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_weather);
            setHasOptionsMenu(true);
            listPreference = (ListPreference) findPreference(getResources().getString(R.string.weather_city_key));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.astroweatherSource)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.weather_units_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.city1_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.city2_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.city3_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.city4_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.city5_key)));
            bindPreferenceSummaryToValue(loadCitiesListEntries());
        }

        public static Preference loadCitiesListEntries () {
            if (listPreference == null)
                return listPreference;
            String[] cities = new String[] {city1, city2, city3, city4, city5};
            ArrayList<String> entries = new ArrayList<>();
            for (String city : cities) {
                if (city.length() > 0) {
                    entries.add(city);
                }
            }
            listPreference.setEntries(entries.toArray(new String[0]));
            listPreference.setEntryValues(entries.toArray(new String[0]));
            return listPreference;
        }

        @Override
        public boolean onOptionsItemSelected(final MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.sync_frequency_key)));
        }

        @Override
        public boolean onOptionsItemSelected(final MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
