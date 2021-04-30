package com.faranjit.geojson

import android.content.Context
import com.faranjit.geojson.features.home.data.HomeDataRepository
import com.faranjit.geojson.features.home.data.HomeLocalDataSource
import com.faranjit.geojson.features.home.domain.HomeRepository

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
object ServiceLocator {

    /**
     * Provides HomeRepository to inject within view models.
     */
    fun provideHomeRepository(context: Context): HomeRepository =
        HomeDataRepository(createHomeLocalDataSource(context))

    private fun createHomeLocalDataSource(context: Context) =
        HomeLocalDataSource(context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE))
}