package com.example.anwyr1.astronomicweatherapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.example.anwyr1.astronomicweatherapp.AstronomicCalendarUtil;
import com.example.anwyr1.astronomicweatherapp.R;
import com.example.anwyr1.astronomicweatherapp.SettingsActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.anwyr1.astronomicweatherapp.DateUtil.convertAstronomicData;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SunFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SunFragment extends Fragment {
    private static final int MillisecondsInSecond = 1000;
    private static Timer timer;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.sunriseTime) TextView sunriseDate;
    @BindView(R.id.sunriseAzimuth)TextView sunriseAzimuth;
    @BindView(R.id.sunsetTime)TextView sunsetDate;
    @BindView(R.id.sunsetAzimuth)TextView sunsetAzimuth;
    @BindView(R.id.civilSunrise)TextView civilSunrise;
    @BindView(R.id.civilSunset)TextView civilSunset;

    public SunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sun, container, false);
        ButterKnife.bind(this, viewGroup);
        return viewGroup;
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
        timer.cancel();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
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
                if (getActivity() != null) {
                getActivity().runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                      //  AstronomicCalendarUtil.getInstance(getContext()).updateAstroCalendar(getContext());
                        loadSunRelatedData(AstronomicCalendarUtil.createAstroCalculator(getContext()));
                    }
                });}
            }
        }, 0, time);
    }

    private void loadSunRelatedData(AstroCalculator astroCalculator) {
        AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();
        sunriseDate.setText(convertAstronomicData(sunInfo.getSunrise().toString()));
        sunriseAzimuth.setText(String.valueOf(sunInfo.getAzimuthRise()));
        sunsetDate.setText(convertAstronomicData(sunInfo.getSunset().toString()));
        sunsetAzimuth.setText(String.valueOf(sunInfo.getAzimuthSet()));
        civilSunrise.setText(convertAstronomicData(sunInfo.getTwilightMorning().toString()));
        civilSunset.setText(convertAstronomicData(sunInfo.getTwilightEvening().toString()));
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
