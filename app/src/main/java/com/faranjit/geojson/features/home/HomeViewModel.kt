package com.faranjit.geojson.features.home

import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.faranjit.geojson.R

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */
class HomeViewModel : ViewModel() {

    val languageFlagObservable = ObservableInt(R.drawable.english)
}
