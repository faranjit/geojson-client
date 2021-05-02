package com.faranjit.geojson.features.map.presentation

import android.location.Location
import androidx.lifecycle.*
import com.faranjit.geojson.BaseViewModel
import com.faranjit.geojson.features.map.data.FeatureModel
import com.faranjit.geojson.features.map.domain.KiwiMarker
import com.faranjit.geojson.features.map.domain.KiwiRepository
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class KiwisViewModel(
    private val repository: KiwiRepository
) : BaseViewModel() {

    companion object {
        private const val LOCATION_INTERVAL = 5000L
    }

    private val kiwiPoints: MutableMap<String, MutableList<KiwiMarker>> = HashMap()

    private val markers = MutableLiveData<List<KiwiMarker>>()
    val markersLiveData: LiveData<List<KiwiMarker>>
        get() = markers

    private val kiwiFound = MutableLiveData<KiwiMarker>()
    val kiwiFoundLiveData: LiveData<KiwiMarker>
        get() = kiwiFound

    fun createLocationRequest() = LocationRequest.create().apply {
        interval = LOCATION_INTERVAL
        fastestInterval = LOCATION_INTERVAL / 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Fetches locations of kiwis and than draws markers
     */
    fun getKiwis() {
        viewModelScope.launch {
            val response = repository.getKiwiLocations()
            response.features?.let {
                markers.value = createMarkers(it)
            }
        }
    }

    fun addMarker(map: GoogleMap, kiwiMarker: KiwiMarker, icon: BitmapDescriptor) {
            val marker = map.addMarker(
                MarkerOptions()
                    .position(kiwiMarker.location)
                    .title(kiwiMarker.title)
                    .icon(icon)
            )
    }

    fun findDistanceLessThan(location: Location, threshold: Float) {
        val key = "${location.latitude.toInt()}-${location.longitude.toInt()}"
        kiwiPoints[key]?.filter {
            val results = FloatArray(3)
            Location.distanceBetween(
                location.latitude, location.longitude,
                it.location.latitude, it.location.longitude,
                results
            )

            results[0] <= threshold
        }?.getOrNull(0)?.let {
            kiwiFound.value = it
        }
    }

    private fun createMarkers(kiwis: List<FeatureModel>) = kiwis.filterNot {
        it.geometry?.coordinates == null
    }.mapNotNull {
        createMarker(it)
    }

    private fun createMarker(kiwi: FeatureModel) = kiwi.run {
        if (geometry?.coordinates?.get(0) != null && geometry.coordinates.get(1) != null) {
            val latLng = LatLng(geometry.coordinates[1] ?: 0.0, geometry.coordinates[0] ?: 0.0)
            addToMap(kiwi, latLng)

            KiwiMarker(
                properties?.deviceName,
                properties?.color,
                latLng
            )
        } else {
            null
        }
    }

    private fun addToMap(kiwi: FeatureModel, latLng: LatLng) {
        val key = "${latLng.latitude.toInt()}-${latLng.longitude.toInt()}"
        val marker = KiwiMarker(
            kiwi.properties?.deviceName,
            kiwi.properties?.color,
            latLng
        )

        if (kiwiPoints.containsKey(key)) {
            kiwiPoints[key]?.add(marker)
        } else {
            kiwiPoints[key] = mutableListOf(marker)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class KiwisViewModelFactory(
    private val repository: KiwiRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (KiwisViewModel(repository) as T)
}