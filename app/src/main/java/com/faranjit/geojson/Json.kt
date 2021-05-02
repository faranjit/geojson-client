package com.faranjit.geojson

import kotlinx.serialization.json.Json

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
val Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = true
}
