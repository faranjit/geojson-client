package com.faranjit.geojson.features.map.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
@Serializable
data class KiwiLocationResponse(
    @SerialName("type")
    val type: String? = null,
    @SerialName("features")
    val features: List<FeatureModel>? = null
)

@Serializable
data class FeatureModel(
    @SerialName("type")
    val type: String? = null,
    @SerialName("geometry")
    val geometry: GeometryModel? = null,
    @SerialName("properties")
    val properties: PropertiesModel? = null
)

@Serializable
data class GeometryModel(
    @SerialName("type")
    val type: String? = null,
    @SerialName("coordinates")
    val coordinates: List<Double?>
)

@Serializable
data class PropertiesModel(
    @SerialName("deviceId")
    val deviceId: String? = null,
    @SerialName("deviceName")
    val deviceName: String? = null,
    @SerialName("color")
    val color: String? = null
)