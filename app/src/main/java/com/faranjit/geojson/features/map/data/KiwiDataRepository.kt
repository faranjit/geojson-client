package com.faranjit.geojson.features.map.data

import com.faranjit.geojson.features.map.domain.KiwiRepository

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class KiwiDataRepository(private val remoteDataSource: KiwiRemoteDataSource) : KiwiRepository {

    override suspend fun getKiwiLocations() = remoteDataSource.getKiwiLocations()
}