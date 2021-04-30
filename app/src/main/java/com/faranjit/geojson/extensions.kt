package com.faranjit.geojson

import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.viewbinding.ViewBinding

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */

const val KEY_TURKISH = "tr"
const val KEY_ENGLISH = "en"

/**
 * Sets background of the ImageView with given resource id.
 * @param backgroundResId The of of the resource
 */
@BindingAdapter("app:background")
fun ImageView.background(@DrawableRes backgroundResId: Int) {
    setBackgroundResource(backgroundResId)
}

/**
 * Creates binding using property delegate
 *
 * @param initializer Function that sets up binding
 * @return delegate property of binding
 */
fun <T : ViewBinding> AppCompatActivity.viewBinding(initializer: (LayoutInflater) -> T) =
    ViewBindingPropertyDelegate(this, initializer)
