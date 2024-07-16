package com.globant.codechanllenge.data.model

import com.google.gson.annotations.SerializedName


data class Wind (

    @SerializedName("speed") var speed : Double,
    @SerializedName("deg") var deg : Int,
    @SerializedName("gust") var gust : Double

)