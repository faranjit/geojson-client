package com.faranjit.geojson.features.map.data

import com.faranjit.geojson.BaseUnitTest
import com.faranjit.geojson.Json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
@ExperimentalCoroutinesApi
class KiwiDataRepositoryTest : BaseUnitTest() {

    @Test
    fun verifyGetKiwiLocations() {
        testScope.launch {
            // Given
            val remoteDataSource = mock(KiwiRemoteDataSource::class.java)
            val repository = KiwiDataRepository(remoteDataSource)
            val dummyResponse =
                Json.decodeFromString<KiwiLocationResponse>(dummyKiwiLocationResponse)

            // When
            `when`(remoteDataSource.getKiwiLocations()).thenReturn(dummyResponse)
            val response = repository.getKiwiLocations()

            // Then
            assertNotNull(response)
            assertEquals(5, response.features?.size)
        }
    }
}