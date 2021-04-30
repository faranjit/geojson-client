package com.faranjit.geojson.features.home.data

import com.faranjit.geojson.features.home.domain.HomeRepository

/**
 * Created by Bulent Turkmen on 30.04.2021.
 */
class HomeDataRepository(
    private val localDataSource: HomeLocalDataSource
) : HomeRepository {

    override fun getLanguage() = localDataSource.getLanguage()

    override fun changeLanguage(lang: String) = localDataSource.changeLanguage(lang)
}