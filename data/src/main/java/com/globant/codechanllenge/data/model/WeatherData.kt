package com.globant.codechanllenge.data.model

import com.google.gson.annotations.SerializedName


data class WeatherData(

    @SerializedName("cod") var cod: String,
    @SerializedName("message") var message: Int,
    @SerializedName("cnt") var cnt: Int,
    @SerializedName("city") var city: City,
    @SerializedName("name") var name: String,
    @SerializedName("main") var main: Main,
    @SerializedName("wind") var wind: Wind,
    @SerializedName("weather") var weather: kotlin.collections.List<Weather>
    )