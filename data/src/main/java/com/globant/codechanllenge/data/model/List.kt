package com.globant.codechanllenge.data.model

import com.google.gson.annotations.SerializedName
import kotlin.collections.List


data class List(

    @SerializedName("dt") var dt: Int,
    @SerializedName("main") var main: Main,
    @SerializedName("clouds") var clouds: Clouds,
    @SerializedName("wind") var wind: Wind,
    @SerializedName("weather") var weather: List<Weather>,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("pop") var pop: Double,
    @SerializedName("sys") var sys: Sys,
    @SerializedName("dt_txt") var dtTxt: String
)