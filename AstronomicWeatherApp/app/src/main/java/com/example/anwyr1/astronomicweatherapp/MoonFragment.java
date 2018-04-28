package com.example.anwyr1.astronomicweatherapp;

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

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MillisecondsInSecond = 1000;
    private static Timer timer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MoonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoonFragment newInstance(String param1, String param2) {
        MoonFragment fragment = new MoonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_moon, container, true);
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

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void loadMoonRelatedData() {
        AstronomicCalculator astronomicCalculator = AstronomicCalculator.getInstance(getContext());
        AstroCalculator.MoonInfo moonInfo = astronomicCalculator.getAstroCalculator().getMoonInfo();
        final TextView moonriseDate = getView().findViewById(R.id.moonRise);
        final TextView moonSetDate = getView().findViewById(R.id.moonSet);
        final TextView nextNewMoon = getView().findViewById(R.id.nextNewMoon);
        final TextView nextFullMoon = getView().findViewById(R.id.nextFullMoon);
        final TextView illumination = getView().findViewById(R.id.illumination);
        final TextView monthAge = getView().findViewById(R.id.monthAge);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
