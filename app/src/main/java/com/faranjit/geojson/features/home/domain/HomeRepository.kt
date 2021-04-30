package com.faranjit.geojson.features.home.domain

/**
 * Created by Bulent Turkmen on 30.04.2021.
 */
interface HomeRepository {

    /**
     * Gets current language
     * @return String
     */
    fun getLanguage(): String

    /**
     * Changes language
     * @param lang New language key.
     *        If it is 'tr' language will be Turkish otherwise English
     */
    fun changeLanguage(lang: String)
}