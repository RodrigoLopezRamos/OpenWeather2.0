package com.globant.codechanllenge.openweather.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.codechanllenge.data.common.Result
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.WeatherData
import com.globant.codechanllenge.domain.usecase.weather.GetWeatherUseCase
import com.globant.codechanllenge.openweather.mapper.WeatherMapper
import com.globant.codechanllenge.openweather.ui.landing.model.LandingModel
import com.globant.codechanllenge.openweather.util.AppPreferences

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val appPreferences: AppPreferences,
    private val weatherMapper: WeatherMapper
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> get() = _state

    init {
        refresh()
    }

    fun refresh() {
        val currentTime = System.currentTimeMillis()
        val threeHoursLater = currentTime + (3 * 60 * 60 * 1000) // Adding 3 hours in milliseconds
        getCurrentWeather(currentTime, threeHoursLater)
    }

    private fun getCurrentWeather(start: Long, end: Long) = viewModelScope.launch {
        appPreferences.getLocation()?.let { location ->
            getWeatherUseCase(
                location.first,
                location.second,
                start,
                end
            ).onEach {
             handleResponse(it)
            }.catch { throwable -> handleResponse(Result.error(throwable)) }.launchIn(viewModelScope)
        }
    }

    private suspend fun handleResponse(
        result: Result<Pair<WeatherData, ForecastData>>
    ) = withContext(Dispatchers.Main) {
        when (result.status) {
            Result.Status.LOADING -> {
                _state.value = WeatherUiState.Loading
            }

            Result.Status.OK -> {
                val weatherData = result.data?.first
                val forecastData = result.data?.second
                if (weatherData != null && forecastData != null) {
                    _state.value = WeatherUiState.WeatherUiStateReady(
                        weatherMapper.buildLandingViewModel(weatherData, forecastData)
                    )
                } else {
                    _state.value =
                        WeatherUiState.WeatherUiStateError(error = "Weather data is null")
                }
            }

            Result.Status.ERROR -> {
                _state.value =
                    WeatherUiState.WeatherUiStateError(error = result.error?.type?.name)
            }
        }
    }
}

sealed class WeatherUiState {
    data class WeatherUiStateReady(val landingModel: LandingModel) :
        WeatherUiState()

    data object Loading : WeatherUiState()
    data class WeatherUiStateError(val error: String? = null) : WeatherUiState()
}
