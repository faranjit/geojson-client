package com.faranjit.geojson.features.map.presentation

import android.location.Location
import com.faranjit.geojson.BaseUnitTest
import com.faranjit.geojson.Json
import com.faranjit.geojson.features.map.data.FakeKiwiRepository
import com.faranjit.geojson.features.map.data.KiwiLocationResponse
import com.faranjit.geojson.features.map.data.dummyKiwiLocationResponse
import com.faranjit.geojson.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
@ExperimentalCoroutinesApi
class KiwisViewModelTest : BaseUnitTest() {

    private lateinit var viewModel: KiwisViewModel

    private val repository = FakeKiwiRepository()

    @Before
    fun setup() {
        viewModel = KiwisViewModel(repository)
    }

    @Test
    fun verifyGetKiwiLocations() {
        testScope.launch {
            // Given
            val dummyResponse =
                Json.decodeFromString<KiwiLocationResponse>(dummyKiwiLocationResponse)

            // When
            viewModel.getKiwis()
            val markers = viewModel.markersLiveData.getOrAwaitValue()

            // Then
            assertNotNull(markers)
            assertEquals(dummyResponse.features, markers)
        }
    }

    @Test
    fun shouldFindDistanceLessThanReturnsValue() {
        // Given
        val location = Location("flp")
        location.latitude = 46.0
        location.longitude = 30.770670
        location.accuracy = 1f

        // When
        viewModel.getKiwis()
        val distance = viewModel.findDistanceLessThan(location, 1f)

        // Then
        assertNotNull(distance)
    }

    @Test
    fun verifyCreateLocationRequest() {
        assertNotNull(viewModel.createLocationRequest())
    }
}