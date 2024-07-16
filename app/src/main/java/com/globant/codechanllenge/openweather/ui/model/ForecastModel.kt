package com.globant.codechanllenge.openweather.ui.model

data class ForecastModel(
    var highTemperature: String,
    var lowTemperature: String,
    var date: String,
    var windSpeed: String,
    var windSpeedDirection: String,
    var iconUrl: String,
    var description: String,
    var hour: String)