package com.faranjit.geojson

import androidx.lifecycle.ViewModel
import com.faranjit.geojson.language.LanguageResourceProvider

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Gets value of given key from the dictionary.
     * @param key name of value in the dictionary
     * @return String
     */
    fun getString(key: String) = LanguageResourceProvider.getString(key)
}