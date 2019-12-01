package com.anna.wyrwal.musicplaceslocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.anna.wyrwal.musicplaceslocator.MusicBrainz.MusicBrainzApiService
import com.anna.wyrwal.musicplaceslocator.MusicBrainz.PlaceQueryResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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
            apiService.searchPlace(searchField.text.toString(), {showResult(it)}, {showError(it)})
        }
    }

    private fun showResult(placeQueryResponse: PlaceQueryResponse?) {
        Log.d("IMusicBrainzApiService", placeQueryResponse?.places?.size.toString())
    }

    private fun showError(message: String?) {
        Log.e("IMusicBrainzApiService", message ?: "Unknown Error")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
