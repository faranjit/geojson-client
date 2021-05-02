package com.faranjit.geojson

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.faranjit.geojson.features.home.data.HomeDataRepository
import com.faranjit.geojson.features.home.data.HomeLocalDataSource
import com.faranjit.geojson.features.home.domain.HomeRepository
import com.faranjit.geojson.features.map.data.KiwiDataRepository
import com.faranjit.geojson.features.map.data.KiwiRemoteDataSource
import com.faranjit.geojson.features.map.domain.KiwiRepository
import com.faranjit.geojson.network.GeojsonClient
import com.faranjit.geojson.network.TokenFeature
import io.ktor.client.*
import io.ktor.client.features.logging.*

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
object ServiceLocator {

    private const val PACKAGE_NAME = "com.faranjit.geojson"

    @Volatile
    var homeRepository: HomeRepository? = null
        @VisibleForTesting set

    @Volatile
    var kiwiRepository: KiwiRepository? = null
        @VisibleForTesting set

    var client: GeojsonClient? = null

    /**
     * Provides HomeRepository to inject within view models.
     */
    fun provideHomeRepository(context: Context): HomeRepository {
        synchronized(this) {
            return homeRepository ?: createHomeRepository(context)
        }
    }

    /**
     * Provides KiwiRepository to inject within view models.
     */
    fun provideKiwiRepository(): KiwiRepository {
        synchronized(this) {
            return kiwiRepository ?: createKiwiRepository()
        }
    }

    fun provideGeojsonClient(): GeojsonClient {
        synchronized(this) {
            return client ?: GeojsonClient(createHttpClient())
        }
    }

    private fun createHomeRepository(context: Context): HomeRepository {
        val repository = HomeDataRepository(createHomeLocalDataSource(context))
        homeRepository = repository
        return repository
    }

    private fun createHomeLocalDataSource(context: Context) =
        HomeLocalDataSource(context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE))

    private fun createKiwiRepository(): KiwiRepository {
        val repository = KiwiDataRepository(KiwiRemoteDataSource(provideGeojsonClient()))
        kiwiRepository = repository
        return repository
    }

    private fun createHttpClient() = HttpClient {
        if (BuildConfig.DEBUG) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }

        install(TokenFeature) {
            token = "test.find.ride.kiwi"
        }
    }
}