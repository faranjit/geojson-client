package com.faranjit.geojson.features.map.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.faranjit.geojson.R
import com.faranjit.geojson.ServiceLocator
import com.faranjit.geojson.features.map.domain.KiwiMarker
import com.faranjit.geojson.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.flow.collect

class KiwisActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 61
    }

    private val viewModel: KiwisViewModel by viewModels {
        KiwisViewModelFactory(ServiceLocator.provideKiwiRepository())
    }

    private var mMap: GoogleMap? = null
    private var userMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kiwis)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.markersLiveData.observe(this, this::addMarkers)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.apply {
            setOnMyLocationButtonClickListener(this@KiwisActivity)
            setOnMyLocationClickListener(this@KiwisActivity)
            setMinZoomPreference(6.0f)
            setMaxZoomPreference(20.0f)
        }

        checkLocationPermission()
    }

    override fun onMyLocationButtonClick() = false

    override fun onMyLocationClick(location: Location) {
        zoomToUser(location, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocations()
            } else {
                finish()
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap?.isMyLocationEnabled = true
            getLocations()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun getLocations() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        listenLocationUpdate(fusedLocationProviderClient)

        viewModel.getKiwis()
    }

    private fun listenLocationUpdate(fusedLocationProviderClient: FusedLocationProviderClient) {
        lifecycleScope.launchWhenResumed {
            fusedLocationProviderClient.locationFlow(viewModel.createLocationRequest()).collect {
                zoomToUser(it, userMarker == null)
            }
        }
    }

    private fun addMarker(marker: KiwiMarker) {
        mMap?.apply {
            addMarker(
                MarkerOptions()
                    .position(marker.location)
                    .title(marker.title)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            if (marker.color == null) {
                                BitmapDescriptorFactory.HUE_AZURE
                            } else {
                                viewModel.getHsvFromColor(marker.color)[0]
                            }
                        )
                    )
            )
        }
    }

    private fun addMarkers(markers: List<KiwiMarker>) {
        markers.forEach(this::addMarker)
    }

    private fun zoomToUser(location: Location, zoom: Boolean = false) {
        mMap?.apply {
            val latLng = LatLng(location.latitude, location.longitude)
            if (zoom) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
            }

            userMarker = addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("You!")
            )
        }
    }
}
