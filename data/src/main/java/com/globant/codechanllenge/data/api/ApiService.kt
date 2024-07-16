package com.globant.codechanllenge.data.api

import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.WeatherData
import retrofit2.http.*

interface ApiService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): WeatherData


    @GET("forecast")
    suspend fun getForecastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): ForecastData


    @GET("forecast")
    suspend fun getForecastDataRange(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("start") start: Long,
        @Query("end") end: Long,
        @Query("appid") apiKey: String
    ): ForecastData


}