package com.faranjit.geojson.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.faranjit.geojson.features.home.domain.HomeRepository

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class FakeHomeRepository(private var lang: String): HomeRepository {

    override fun getLanguage(): LiveData<String> = liveData {
        emit(lang)
    }

    override fun changeLanguage(lang: String) {
        this.lang = lang
    }
}