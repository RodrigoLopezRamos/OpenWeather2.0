package com.globant.codechanllenge.domain.usecase.weather

import android.util.Log
import com.globant.codechanllenge.data.common.Result
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.WeatherData
import com.globant.codechanllenge.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        start: Long,
        end: Long
    ): Flow<Result<Pair<WeatherData, ForecastData>>> = flow {
        try {
            emit(Result.loading())
            coroutineScope {
                val deferredWeather = async {
                    withContext(Dispatchers.IO) {
                        repository.getCurrentWeather(
                            latitude,
                            longitude
                        )
                    }
                }

                val deferredForecast = async {
                    withContext(Dispatchers.IO) {
                        repository.getForecastRange(latitude, longitude, start, end)
                    }
                }

                val weatherData = deferredWeather.await()
                val forecastData = deferredForecast.await()
                emit(Result.success(Pair(weatherData, forecastData)))
            }
        } catch (e: Throwable) {
            Log.e("GetWeatherUseCase", "Unexpected exception", e)
            emit(Result.error(e))
        }
    }


}