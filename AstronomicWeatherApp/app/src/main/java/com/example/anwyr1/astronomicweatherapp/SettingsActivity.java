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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public static String getFromSettings(String key, String defValue,  Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defValue);
    }

    public static void setSettings(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        final String invalidCityTitle = "Invalid City Selected";
        final String invalidCityContent = "Something's gone wrong... There is no such city or you have no access to Internet.";

        @Override
        public boolean onPreferenceChange(final Preference preference, Object value) {
            final String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                final ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                if (preference.getKey().equals("selected_city")) {
                    return isCityValid(stringValue);
                }
            } else if (preference instanceof EditTextPreference && (preference.getKey().equals("latitude")
                    || preference.getKey().equals("longitude"))) {
                if (!updateLatitudeOrLongitude(preference, stringValue))
                    return false;
            } else if (preference instanceof EditTextPreference) {
                if (updateProperFavoriteCity(preference, stringValue))
                    preference.setSummary(stringValue);
                else
                    return false;
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }

        private boolean updateProperFavoriteCity(Preference preference, String stringValue) {
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

        private boolean isCityValid(String city, int numberOfCity) {
            if (city.length() == 0 && numberOfCity > 0)
                return true;
            final String cityId = city.replaceAll("\\s","");
            return isCityValid(cityId);
        }

        private boolean isCityValid(final String cityId) {
            if(!cityId.matches("[A-Za-z].*,*[A-Za-z].*"))
                return false;
            final String firstUrlWeatherApiPart = "http://api.openweathermap.org/data/2.5/weather?q=";
            final String secondUrlWeatherApiPart = "&mode=xml&appid=6568cca14ced23610c0a31b4f0bc5562&units=";
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<Boolean> result = es.submit(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    try {
                        downloadUrl(firstUrlWeatherApiPart + cityId + secondUrlWeatherApiPart);
                    } catch (IOException e) {
                        return false;
                    }
                    return true;
                }
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

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000 /* milliseconds */);
            conn.setConnectTimeout(1500 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }

        private boolean updateLatitudeOrLongitude(Preference preference, String stringValue) {
            if (!NumberUtils.isParsable(stringValue)
                    || Double.parseDouble(stringValue) <= -80
                    || Double.parseDouble(stringValue) >= 80) {
                final String invalidCoordsTitle = "Invalid Input";
                final String invalidCoordsContent = "Something's gone wrong...";
                displayAlert(invalidCoordsTitle, invalidCoordsContent, preference.getContext());
                return false;
            }
            preference.setSummary(stringValue);
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


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
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
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        class OnGeneralPreferenceChangeListener implements Preference.OnPreferenceChangeListener {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (!NumberUtils.isParsable((String)newValue)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Something's gone wrong...");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    rtnval = false;
                }
                return rtnval;

            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their val n'
            // ues. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.latitude_key)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.longitude_key)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        private static ListPreference listPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_weather);
            setHasOptionsMenu(true);
            listPreference = (ListPreference) findPreference(getResources().getString(R.string.weather_city_key));
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
        public boolean onOptionsItemSelected(MenuItem item) {
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
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.sync_frequency_key)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
