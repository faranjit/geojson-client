package com.faranjit.geojson.features.home.data

import android.content.SharedPreferences
import com.faranjit.geojson.KEY_ENGLISH
import com.faranjit.geojson.KEY_TURKISH

/**
 * Created by Bulent Turkmen on 30.04.2021.
 */
class HomeLocalDataSource(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_LANGUAGE = "language_key"
    }

    /**
     * Gets current language
     * @return String
     */
    fun getLanguage() = getString(KEY_LANGUAGE, KEY_ENGLISH)!!

    /**
     * Changes language
     * @param lang New language key.
     *        If it is 'tr' language will be Turkish otherwise English
     */
    fun changeLanguage(lang: String) {
        val newLang = if (lang != KEY_TURKISH) KEY_ENGLISH else lang
        putString(KEY_LANGUAGE, newLang)
    }

    /**
     * Gets a string value from shared preferences.
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist. This value may be null.
     */
    fun getString(key: String, defValue: String?) = sharedPreferences.getString(key, defValue)

    private fun putString(key: String, str: String) {
        sharedPreferences.edit().putString(key, str).apply()
    }
}