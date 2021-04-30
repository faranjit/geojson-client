package com.faranjit.geojson.features.home

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faranjit.geojson.KEY_ENGLISH
import com.faranjit.geojson.KEY_TURKISH
import com.faranjit.geojson.R
import com.faranjit.geojson.features.home.domain.HomeRepository

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */
class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_LANG_FLAG = R.drawable.english
        private val flags =
            mapOf(KEY_TURKISH to R.drawable.turkish, KEY_ENGLISH to R.drawable.english)
    }

    val languageFlagObservable = ObservableInt()

    private val languageChanged = MutableLiveData<String>()
    val languageChangedLiveData: LiveData<String>
        get() = languageChanged

    var language: String = repository.getLanguage()
        set(value) {
            field = value
            changeLanguage(value)
        }

    init {
        languageFlagObservable.set(flags[language] ?: DEFAULT_LANG_FLAG)
    }

    private fun changeLanguage(lang: String) {
        repository.changeLanguage(lang)
        languageChanged.value = lang
        languageFlagObservable.set(flags[language] ?: DEFAULT_LANG_FLAG)
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (HomeViewModel(repository) as T)
}
