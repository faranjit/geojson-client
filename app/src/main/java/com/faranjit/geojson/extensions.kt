package com.faranjit.geojson

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.viewbinding.ViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

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

fun Location.latLng() = LatLng(latitude, longitude)

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
fun FusedLocationProviderClient.locationFlow(locationRequest: LocationRequest) =
    callbackFlow<Location> {

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result ?: return // Ignore null responses
                for (location in result.locations) {
                    try {
                        offer(location) // Send location to the flow
                    } catch (t: Throwable) {
                        // Location couldn't be sent to the flow
                    }
                }
            }
        }

        requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e)
        }

        awaitClose {
            // Clean up code goes here
            removeLocationUpdates(callback)
        }
    }
