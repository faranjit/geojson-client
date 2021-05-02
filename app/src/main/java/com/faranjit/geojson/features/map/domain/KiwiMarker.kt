package com.faranjit.geojson.features.map.domain

import com.google.android.gms.maps.model.LatLng

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
data class KiwiMarker(
    val title: String?,
    val color: String?,
    val location: LatLng
)