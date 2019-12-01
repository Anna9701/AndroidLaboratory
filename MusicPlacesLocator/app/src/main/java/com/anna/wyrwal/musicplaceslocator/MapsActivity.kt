package com.anna.wyrwal.musicplaceslocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.anna.wyrwal.musicplaceslocator.MusicBrainz.Coordinates
import com.anna.wyrwal.musicplaceslocator.MusicBrainz.MusicBrainzApiService
import com.anna.wyrwal.musicplaceslocator.MusicBrainz.Place
import com.anna.wyrwal.musicplaceslocator.MusicBrainz.PlaceQueryResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.concurrent.TimeUnit


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val apiService = MusicBrainzApiService()
    private val minimalOpeningYear = 1990

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        searchButton.setOnClickListener {
            hideKeyboard()
            apiService.searchPlace(
                searchPlaceTextView.text.toString(),
                { showResult(it) },
                { showError(it) })
        }
        clearButton.setOnClickListener {
            mMap.clear()
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun showResult(placeQueryResponse: PlaceQueryResponse?) {
        val places =
            placeQueryResponse?.places?.filterNot { it.coordinates == null ||
                    it.life_span.getOpeningYear() < minimalOpeningYear }
        places?.forEach {
            addPlaceMarker(it)
        }
        val fistPlaceCords = places?.first()?.coordinates ?: Coordinates("0", "0")
        val position =
            LatLng(fistPlaceCords.latitude.toDouble(), fistPlaceCords.longitude.toDouble())
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
    }

    private fun addPlaceMarker(place: Place) {
        val position =
            LatLng(place.coordinates!!.latitude.toDouble(), place.coordinates.longitude.toDouble())

        val marker = mMap.addMarker(MarkerOptions().position(position).title(place.name))
        val delay = place.life_span.getOpeningYear() - minimalOpeningYear
        Observable
            .timer(delay.toLong(), TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { marker.remove() }
    }

    private fun showError(message: String?) {
        Log.e("MusicBrainzApiService", message ?: "Unknown Error")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
