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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.anwyr1.astronomicweatherapp.DateUtil.convertAstronomicData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MoonFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.moonRise)TextView moonriseDate;
    @BindView(R.id.moonSet)TextView moonSetDate;
    @BindView(R.id.nextNewMoon)TextView nextNewMoon;
    @BindView(R.id.nextFullMoon)TextView nextFullMoon;
    @BindView(R.id.illumination)TextView illumination;
    @BindView(R.id.monthAge)TextView monthAge;

    public MoonFragment() { }

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
        Single<AstroCalculator> astroCalendarObserver = Single.create(emitter -> {
            try {
                AstroCalculator astroCalculator = AstronomicCalendarUtil.createAstroCalculator(getContext());
                emitter.onSuccess(astroCalculator);
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
        astroCalendarObserver
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::loadMoonRelatedData, System.err::println);
        astroCalendarObserver
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .delay(time, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .repeat()
                .subscribe(this::loadMoonRelatedData, System.err::println);
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

    private void loadMoonRelatedData(AstroCalculator astroCalculator) {
        AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();
        try {
            if (moonInfo.getMoonrise() != null)
                moonriseDate.setText(convertAstronomicData(moonInfo.getMoonrise().toString()));
            else
                moonriseDate.setText("No data available");
            moonSetDate.setText(convertAstronomicData(moonInfo.getMoonset().toString()));
            nextNewMoon.setText(convertAstronomicData(moonInfo.getNextNewMoon().toString()));
            nextFullMoon.setText(convertAstronomicData(moonInfo.getNextFullMoon().toString()));
            illumination.setText(String.valueOf(moonInfo.getIllumination()));
            monthAge.setText(String.valueOf(moonInfo.getAge()));
        } catch (NullPointerException ex) {
     //       final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
     //       builder.setTitle("Sync error");
      //      builder.setMessage("Something's gone wrong... Restoring default longitude and latitude " +
        //            "values");
       //     builder.setPositiveButton(android.R.string.ok, null);
//            builder.show();
            restoreDefaultLatitudeAndLongitude(astroCalculator);
        }
    }

    private void restoreDefaultLatitudeAndLongitude(AstroCalculator astroCalculator) {
        SettingsActivity.setSettings("latitude",
                getString(R.string.pref_default_display_latitude), getContext());
        SettingsActivity.setSettings("longitude",
                getString(R.string.pref_default_display_longitude), getContext());
        loadMoonRelatedData(astroCalculator);
    }
}