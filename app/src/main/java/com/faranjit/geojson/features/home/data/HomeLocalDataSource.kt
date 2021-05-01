package com.faranjit.geojson.features.home.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faranjit.geojson.KEY_ENGLISH
import com.faranjit.geojson.KEY_TURKISH

/**
 * Created by Bulent Turkmen on 30.04.2021.
 */
class HomeLocalDataSource(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_LANGUAGE = "language_key"
    }

    private val language = MutableLiveData<String>()

    /**
     * Gets current language
     * @return String
     */
    fun getLanguage(): LiveData<String> {
        val lang = sharedPreferences.getString(KEY_LANGUAGE, KEY_ENGLISH)!!
        language.postValue(lang)
        return language
    }

    /**
     * Changes language
     * @param lang New language key.
     *        If it is 'tr' language will be Turkish otherwise English
     */
    fun changeLanguage(lang: String) {
        val newLang = if (lang != KEY_TURKISH) KEY_ENGLISH else lang
        sharedPreferences.edit().putString(KEY_LANGUAGE, newLang).apply()

        language.postValue(lang)
    }
}