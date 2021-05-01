package com.faranjit.geojson.features.home

import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.faranjit.geojson.BaseViewModel
import com.faranjit.geojson.KEY_ENGLISH
import com.faranjit.geojson.KEY_TURKISH
import com.faranjit.geojson.R
import com.faranjit.geojson.features.home.domain.HomeRepository

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */
class HomeViewModel(
    private val repository: HomeRepository
) : BaseViewModel() {

    companion object {
        private const val DEFAULT_LANG_FLAG = R.drawable.english
        private val flags =
            mapOf(KEY_TURKISH to R.drawable.turkish, KEY_ENGLISH to R.drawable.english)
    }

    val languageFlagObservable = ObservableInt()
    val titleObservable = ObservableField<String>()
    val buttonObservable = ObservableField<String>()

    @Volatile
    var language = ""
        @VisibleForTesting set
    var languageChangedLiveData = repository.getLanguage().switchMap {
        language = it

        liveData { emit(it) }
    }

    /**
     * Changes language.
     * If current is English, it will be Turkish.
     * If current is Turkish, it will be English.
     */
    fun changeLanguage() {
        language = if (language == KEY_TURKISH) {
            KEY_ENGLISH
        } else {
            KEY_TURKISH
        }
        repository.changeLanguage(language)
    }

    /**
     * Updates text of the UI elements
     */
    fun updateTexts(lang: String) {
        titleObservable.set(getString("home.title"))
        buttonObservable.set(getString("home.yes"))
        languageFlagObservable.set(flags[lang] ?: DEFAULT_LANG_FLAG)
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (HomeViewModel(repository) as T)
}
