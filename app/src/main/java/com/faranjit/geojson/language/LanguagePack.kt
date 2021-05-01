package com.faranjit.geojson.language

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
@Serializable
class LanguagePack(
    @SerialName("app_name")
    val appName: String,
    @SerialName("entities")
    val dictionary: Map<String, LinkedHashMap<String, String>>
)