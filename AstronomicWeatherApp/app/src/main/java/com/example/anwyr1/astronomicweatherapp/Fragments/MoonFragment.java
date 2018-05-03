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

import com.astrocalculator.AstroCalculator;
import com.example.anwyr1.astronomicweatherapp.AstronomicCalculator;
import com.example.anwyr1.astronomicweatherapp.R;
import com.example.anwyr1.astronomicweatherapp.SettingsActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MoonFragment extends Fragment {
    private static final int MillisecondsInSecond = 1000;
    private static Timer timer;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.moonRise)TextView moonriseDate;
    @BindView(R.id.moonSet)TextView moonSetDate;
    @BindView(R.id.nextNewMoon)TextView nextNewMoon;
    @BindView(R.id.nextFullMoon)TextView nextFullMoon;
    @BindView(R.id.illumination)TextView illumination;
    @BindView(R.id.monthAge)TextView monthAge;

    public MoonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_moon, container, false);
        ButterKnife.bind(this, viewGroup);
        return viewGroup;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String syncTimeString = SettingsActivity.getFromSettings("sync_frequency",
                getString(R.string.pref_default_display_sync_time_value), getContext());
        int time = Integer.parseInt(syncTimeString);
        time *= MillisecondsInSecond;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        AstronomicCalculator.getInstance(getContext()).updateAstroCalendar(getContext());
                        loadMoonRelatedData();
                    }
                });
            }
        }, 0, time);
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

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void loadMoonRelatedData() {
        AstronomicCalculator astronomicCalculator = AstronomicCalculator.getInstance(getContext());
        AstroCalculator.MoonInfo moonInfo = astronomicCalculator.getAstroCalculator().getMoonInfo();
        try {
            moonriseDate.setText(moonInfo.getMoonrise().toString());
            moonSetDate.setText(moonInfo.getMoonset().toString());
            nextNewMoon.setText(moonInfo.getNextNewMoon().toString());
            nextFullMoon.setText(moonInfo.getNextFullMoon().toString());
            illumination.setText(String.valueOf(moonInfo.getIllumination()));
            monthAge.setText(String.valueOf(moonInfo.getAge()));
        } catch (NullPointerException ex) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Sync error");
            builder.setMessage("Something's gone wrong... Restoring default longitude and latitude " +
                    "values");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
            restoreDefaultLatitudeAndLongitude();
        }
    }

    private void restoreDefaultLatitudeAndLongitude() {
        SettingsActivity.setSettings("latitude",
                getString(R.string.pref_default_display_latitude), getContext());
        SettingsActivity.setSettings("longitude",
                getString(R.string.pref_default_display_longitude), getContext());
        loadMoonRelatedData();
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
