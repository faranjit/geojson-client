package com.faranjit.geojson.features.map.presentation

import android.location.Location
import androidx.lifecycle.*
import com.faranjit.geojson.BaseViewModel
import com.faranjit.geojson.features.map.data.FeatureModel
import com.faranjit.geojson.features.map.domain.KiwiRepository
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
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

    private val kiwiPoints: MutableMap<String, MutableList<FeatureModel>> = HashMap()

    private val markers = MutableLiveData<List<FeatureModel>>()
    val markersLiveData: LiveData<List<FeatureModel>>
        get() = markers

    /**
     * Creates LocationRequest
     */
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
                fillKiwiLocations(it)
                markers.value = it
            }
        }
    }

    /**
     * Finds locations that distance to current location of user is less than given threshold.
     * @param location Location of the user
     * @param threshold Comparing value
     */
    fun findDistanceLessThan(location: Location, threshold: Float): FeatureModel? {
        val key = "${location.latitude.toInt()}-${location.longitude.toInt()}"
        return kiwiPoints[key]?.filter {
            val results = FloatArray(3)
            Location.distanceBetween(
                location.latitude, location.longitude,
                it.geometry.coordinates[1] ?: 0.0, it.geometry.coordinates[0] ?: 0.0,
                results
            )

            results[0] <= threshold
        }?.getOrNull(0)
    }

    private fun fillKiwiLocations(kiwis: List<FeatureModel>) = kiwis.filterNot {
        it.geometry.coordinates.filterNotNull().isEmpty()
    }.forEach {
        addToMap(it)
    }

    private fun addToMap(kiwi: FeatureModel) = kiwi.run {
        if (geometry.coordinates[0] != null && geometry.coordinates.get(1) != null) {
            val latLng = LatLng(geometry.coordinates[1] ?: 0.0, geometry.coordinates[0] ?: 0.0)
            val key = "${latLng.latitude.toInt()}-${latLng.longitude.toInt()}"

            if (kiwiPoints.containsKey(key)) {
                kiwiPoints[key]?.add(kiwi)
            } else {
                kiwiPoints[key] = mutableListOf(kiwi)
            }
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