package com.globant.codechanllenge.openweather.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.codechanllenge.data.common.Result
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.domain.usecase.weather.GetForecastUseCase
import com.globant.codechanllenge.openweather.mapper.WeatherMapper
import com.globant.codechanllenge.openweather.ui.model.ForecastModel
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
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val appPreferences: AppPreferences,
    private val weatherMapper: WeatherMapper
) : ViewModel() {

    private val _state = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val state: StateFlow<ForecastUiState> get() = _state

    init {
        getForecast5()
    }

    fun getForecast5() = viewModelScope.launch {
        appPreferences.getLocation()?.let {
            getForecastUseCase(it.first, it.second).catch { throwable ->
                handleResponse(Result.error(throwable))
            }.onEach(::handleResponse).launchIn(viewModelScope)
        }
    }

    private suspend fun handleResponse(it: Result<ForecastData>) =
        withContext(Dispatchers.Main) {
            when (it.status) {
                Result.Status.LOADING -> _state.value = ForecastUiState.Loading
                Result.Status.OK -> {
                    it.data?.list?.let {
                        _state.value =
                            ForecastUiState.ForecastUiStateReady(
                                weatherMapper.buildForeCastViewModels(
                                    it
                                )
                            )
                    }
                }

                Result.Status.ERROR -> _state.value =
                    ForecastUiState.ForecastUiStateError(error = it.error?.type?.name)
            }
        }
}

sealed class ForecastUiState {
    data class ForecastUiStateReady(val forecastModels: List<ForecastModel>) : ForecastUiState()
    data object Loading : ForecastUiState()
    data class ForecastUiStateError(val error: String? = null) : ForecastUiState()
}

