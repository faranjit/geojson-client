package com.faranjit.geojson.features.map.presentation

import android.graphics.Color
import androidx.lifecycle.*
import com.faranjit.geojson.BaseViewModel
import com.faranjit.geojson.features.map.data.FeatureModel
import com.faranjit.geojson.features.map.domain.KiwiMarker
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

    private val markers = MutableLiveData<List<KiwiMarker>>()
    val markersLiveData: LiveData<List<KiwiMarker>>
        get() = markers

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

    fun getHsvFromColor(color: String): FloatArray {
        val hsv = FloatArray(3)
        val intColor = Color.parseColor(color)
        Color.colorToHSV(intColor, hsv)
        return hsv
    }

    private fun createMarkers(kiwis: List<FeatureModel>) = kiwis.filterNot {
        it.geometry?.coordinates == null
    }.mapNotNull {
        createMarker(it)
    }

    private fun createMarker(kiwi: FeatureModel) = kiwi.run {
        if (geometry?.coordinates?.get(0) != null && geometry.coordinates.get(1) != null) {
            KiwiMarker(
                properties?.deviceName,
                properties?.color,
                LatLng(geometry.coordinates[1] ?: 0.0, geometry.coordinates[0] ?: 0.0)
            )
        } else {
            null
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