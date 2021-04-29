package com.faranjit.geojson

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */
@BindingAdapter("app:background")
fun ImageView.background(backgroundResId: Int) {
    setBackgroundResource(backgroundResId)
}
