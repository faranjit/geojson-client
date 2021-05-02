package com.faranjit.geojson.features.map.domain

import com.faranjit.geojson.features.map.data.KiwiLocationResponse

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
interface KiwiRepository {

    /**
     * Fetches Kiwi locations and returns it
     * @return KiwiLocationResponse
     */
    suspend fun getKiwiLocations(): KiwiLocationResponse
}