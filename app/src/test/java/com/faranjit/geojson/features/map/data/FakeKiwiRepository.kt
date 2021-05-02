package com.faranjit.geojson.features.map.data

import com.faranjit.geojson.Json
import com.faranjit.geojson.features.map.domain.KiwiRepository
import kotlinx.serialization.decodeFromString

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class FakeKiwiRepository : KiwiRepository {

    override suspend fun getKiwiLocations() =
        Json.decodeFromString<KiwiLocationResponse>(dummyKiwiLocationResponse)
}