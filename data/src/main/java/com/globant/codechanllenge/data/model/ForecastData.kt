package com.globant.codechanllenge.data.model

import com.google.gson.annotations.SerializedName


data class ForecastData (

    @SerializedName("id") var id : Int,
    @SerializedName("main") var main : String,
    @SerializedName("description") var description : String,
    @SerializedName("icon") var icon : String,
    @SerializedName("list"    ) var list    : ArrayList<List> = arrayListOf()
)