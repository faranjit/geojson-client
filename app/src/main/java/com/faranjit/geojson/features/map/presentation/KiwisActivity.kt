package com.faranjit.geojson.features.map.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.faranjit.geojson.*
import com.faranjit.geojson.databinding.ActivityKiwisBinding
import com.faranjit.geojson.features.map.data.FeatureModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect


class KiwisActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 61
        private const val MAP_MIN_ZOOM = 6f
        private const val MAP_MAX_ZOOM = 30f
        private const val MAP_DEFAULT_ZOOM = 25f
        private const val DISTANCE_THRESHOLD = 1.5f
    }

    private val viewModel: KiwisViewModel by viewModels {
        KiwisViewModelFactory(ServiceLocator.provideKiwiRepository())
    }

    private val binding: ActivityKiwisBinding by viewBinding(ActivityKiwisBinding::inflate)

    private var mMap: GoogleMap? = null

    private var zoomedToUser = false

    private val markers: MutableMap<String, Marker> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = viewModel
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
            setMinZoomPreference(MAP_MIN_ZOOM)
            setMaxZoomPreference(MAP_MAX_ZOOM)
        }

        checkLocationPermission()
    }

    override fun onMyLocationButtonClick() = false

    override fun onMyLocationClick(location: Location) {
        zoomToUser(location.latLng(), true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationEnabled()
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
            checkLocationEnabled()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun checkLocationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocations()
        } else {
            askEnableLocation()
        }
    }

    private fun askEnableLocation() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(viewModel.getString("map.enable_gps"))
            .setCancelable(false)
            .setPositiveButton(viewModel.getString("buttons.yes")) { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(viewModel.getString("buttons.no")) { dialog, _ ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun getLocations() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        listenLocationUpdate(fusedLocationProviderClient)

        viewModel.getKiwis()
    }

    private fun listenLocationUpdate(fusedLocationProviderClient: FusedLocationProviderClient) {
        lifecycleScope.launchWhenResumed {
            fusedLocationProviderClient.locationFlow(viewModel.createLocationRequest()).collect {
                zoomToUser(it.latLng(), !zoomedToUser)
                zoomedToUser = true
                viewModel.findDistanceLessThan(it, DISTANCE_THRESHOLD)?.let { kiwi ->
                    updateFoundKiwi(kiwi)
                    cancel("Kiwi found, no need to seek anymore")
                }
            }
        }
    }

    private fun addMarker(kiwiMarker: FeatureModel, icon: BitmapDescriptor) {
        if (kiwiMarker.geometry.coordinates.filterNotNull().isNotEmpty()) {
            val latLng = LatLng(
                kiwiMarker.geometry.coordinates[1]!!,
                kiwiMarker.geometry.coordinates[0]!!
            )
            mMap?.apply {
                markers[kiwiMarker.properties.deviceId] = addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(kiwiMarker.properties.deviceName)
                        .icon(icon)
                )
            }
        }
    }

    private fun addMarkers(markers: List<FeatureModel>) {
        markers.forEach { kiwiMarker ->
            addMarker(
                kiwiMarker, BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_CYAN
                )
            )
        }
    }

    private fun updateFoundKiwi(kiwi: FeatureModel) {
        markers[kiwi.properties.deviceId]?.let {
            it.setIcon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN
                )
            )

            zoomToUser(it.position, makeZoom = true, zoom = MAP_DEFAULT_ZOOM)

            viewModel.searchTextObservable.set(viewModel.getString("map.kiwis_found"))
        }
    }

    private fun zoomToUser(
        latLng: LatLng,
        makeZoom: Boolean = false,
        zoom: Float? = null
    ) {
        mMap?.apply {
            if (makeZoom) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom ?: MAP_DEFAULT_ZOOM))
            } else {
                moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            latLng,
                            cameraPosition.zoom,
                            cameraPosition.tilt,
                            cameraPosition.bearing
                        )
                    )
                )
            }
        }
    }
}
