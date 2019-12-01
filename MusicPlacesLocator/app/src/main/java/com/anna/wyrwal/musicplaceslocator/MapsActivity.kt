package com.anna.wyrwal.musicplaceslocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
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


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val apiService = MusicBrainzApiService()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val searchField = findViewById<TextView>(R.id.searchPlaceTextView)
        val searchButton = findViewById<ImageButton>(R.id.searchButton)
        searchButton.setOnClickListener {
            hideKeyboard()
            apiService.searchPlace(
                searchField.text.toString(),
                { showResult(it) },
                { showError(it) })
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun showResult(placeQueryResponse: PlaceQueryResponse?) {
        placeQueryResponse?.places?.filterNot { it.coordinates == null }?.forEach {
            addPlaceMarker(it)
        }
        val fistPlaceCords =
            placeQueryResponse?.places?.firstOrNull { it.coordinates != null }?.coordinates
                ?: Coordinates("0", "0")
        val position =
            LatLng(fistPlaceCords.latitude.toDouble(), fistPlaceCords.longitude.toDouble())
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
    }

    private fun addPlaceMarker(place: Place) {
        val position =
            LatLng(place.coordinates!!.latitude.toDouble(), place.coordinates.longitude.toDouble())
        mMap.addMarker(MarkerOptions().position(position).title(place.name))
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
