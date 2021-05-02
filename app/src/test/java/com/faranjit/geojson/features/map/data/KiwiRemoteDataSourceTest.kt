package com.faranjit.geojson.features.map.data

import com.faranjit.geojson.BaseUnitTest
import com.faranjit.geojson.network.GeojsonClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
@ExperimentalCoroutinesApi
class KiwiRemoteDataSourceTest : BaseUnitTest() {

    @Test
    fun verifyGetKiwiLocations() {
        testScope.launch {
            // Given
            val client = GeojsonClient(fakeClient)
            val remoteDataSource = KiwiRemoteDataSource(client)

            // when
            val response = remoteDataSource.getKiwiLocations()

            // Then
            assertNotNull(response)
            assertEquals(5, response.features?.size)
        }
    }
}