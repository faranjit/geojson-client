package com.faranjit.geojson.features.map.presentation

import androidx.lifecycle.*
import com.faranjit.geojson.BaseViewModel
import com.faranjit.geojson.features.map.data.KiwiLocationResponse
import com.faranjit.geojson.features.map.domain.KiwiRepository
import kotlinx.coroutines.launch

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class KiwisViewModel(
    private val repository: KiwiRepository
) : BaseViewModel() {

    private val kiwis = MutableLiveData<KiwiLocationResponse>()
    val kiwisLiveData: LiveData<KiwiLocationResponse>
        get() = kiwis

    fun getKiwis() {
        viewModelScope.launch {
            val response = repository.getKiwiLocations()
            kiwis.value = response
        }
    }
}

@Suppress("UNCHECKED_CAST")
class KiwisViewModelFactory(
    private val repository: KiwiRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (KiwisViewModel(repository) as T)
}