package com.globant.codechanllenge.openweather.util

import android.content.Context
import android.content.SharedPreferences


import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val LATITUDE = "LATITUDE"

private const val LONGITUDE = "LONGITUDE"

class AppPreferences @Inject constructor(@ApplicationContext context: Context) {


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun saveLocation(latitude: Double, longitude: Double) {
        sharedPreferences.edit().apply {
            putString(LATITUDE, latitude.toString())
            putString(LONGITUDE, longitude.toString())
            putString(LATITUDE, latitude.toString())
            putString(LONGITUDE, longitude.toString())
            apply()
        }
    }

    fun getLocation(): Pair<Double, Double>? {
        val latitude = sharedPreferences.getString(LATITUDE, 0.0.toString())?.toDouble()
        val longitude = sharedPreferences.getString(LONGITUDE, 0.0.toString())?.toDouble()

        return if (latitude != null && longitude != null) {
            Pair(latitude, longitude)
        } else {
            null
        }
    }
}
