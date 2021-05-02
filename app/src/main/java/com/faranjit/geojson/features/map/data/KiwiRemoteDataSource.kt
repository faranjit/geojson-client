package com.faranjit.geojson.features.map.data

import com.faranjit.geojson.Json
import com.faranjit.geojson.network.GeojsonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class KiwiRemoteDataSource(private val client: GeojsonClient) {

    /**
     * Fetches Kiwi locations and returns it
     * @return KiwiLocationResponse
     */
    suspend fun getKiwiLocations() = withContext(Dispatchers.IO) {
        val response = client.get<String>("devices-position-geojson")
        Json.decodeFromString<KiwiLocationResponse>(response)
    }
}