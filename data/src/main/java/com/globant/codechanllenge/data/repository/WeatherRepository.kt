package com.globant.codechanllenge.data.repository

import com.globant.codechanllenge.data.api.ApiService
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.WeatherData
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

private const val API_KEY = "b3e51863ec07ab2f0f6a9bc8208b461e"

@ActivityRetainedScoped
class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String = API_KEY
    ): WeatherData =
        apiService.getWeatherData(latitude, longitude, apiKey)


    suspend fun getCurrentForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String = API_KEY
    ): ForecastData =
        apiService.getForecastData(latitude, longitude, apiKey)


    suspend fun getForecastRange(
        latitude: Double,
        longitude: Double,
        start: Long,
        end: Long,
        apiKey: String = API_KEY
    ): ForecastData =
        apiService.getForecastDataRange(latitude, longitude, start, end, apiKey)
}