package com.globant.codechanllenge.openweather.ui.landing.model

import com.globant.codechanllenge.openweather.ui.model.ForecastModel

data class LandingModel(
    var cityName: String,
    var windSpeedDirection: String,
    var windSpeed: String,
    var feelsLike: String,
    var celsiusTemperature: String,
    var fahrenheitTemperature: String,
    val forecastList: List<ForecastModel>
)

