package com.globant.codechanllenge.domain.usecase.weather

import com.globant.codechanllenge.data.common.Result
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    private var forecastData: ForecastData? = null

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Flow<Result<ForecastData>> =
        flow {
            try {
                emit(Result.loading())
                forecastData = repository.getCurrentForecast(latitude, longitude)
                forecastData?.let {
                    emit(Result.success(it))
                }
            } catch (e: Throwable) {
                emit(Result.error(e))
            }
        }

}