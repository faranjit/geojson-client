package com.faranjit.geojson

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.faranjit.geojson.features.home.data.HomeDataRepository
import com.faranjit.geojson.features.home.data.HomeLocalDataSource
import com.faranjit.geojson.features.home.domain.HomeRepository

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
object ServiceLocator {

    private const val PACKAGE_NAME = "com.faranjit.geojson"

    @Volatile
    var homeRepository: HomeRepository? = null
        @VisibleForTesting set

    /**
     * Provides HomeRepository to inject within view models.
     */
    fun provideHomeRepository(context: Context): HomeRepository {
        synchronized(this) {
            return homeRepository ?: createHomeRepository(context)
        }
    }

    private fun createHomeRepository(context: Context): HomeRepository {
        val repository = HomeDataRepository(createHomeLocalDataSource(context))
        homeRepository = repository
        return repository
    }

    private fun createHomeLocalDataSource(context: Context) =
        HomeLocalDataSource(context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE))
}