package com.globant.codechanllenge.openweather.mapper

import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.WeatherData
import com.globant.codechanllenge.openweather.ui.landing.model.LandingModel
import com.globant.codechanllenge.openweather.ui.model.ForecastModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherMapper @Inject constructor() {

    fun buildLandingViewModel(weatherData: WeatherData, forecastData: ForecastData): LandingModel {
        return LandingModel(
            cityName = weatherData.name,
            windSpeedDirection = degreesToCardinalDirection(weatherData.wind.deg),
            windSpeed = getWindSpeed(weatherData.wind.speed),
            feelsLike = getFahrenheitTemperature(weatherData.main.feelsLike),
            celsiusTemperature = getCelsiusTemperature(weatherData.main.temp),
            fahrenheitTemperature = getFahrenheitTemperature(weatherData.main.temp),
            forecastList = buildForeCastViewModels(forecastData.list)
        );
    }

    fun buildForeCastViewModels(listOfList: ArrayList<com.globant.codechanllenge.data.model.List>): List<ForecastModel> {
        return listOfList.map { list -> buildForeCastViewModel(list) }
    }

    private fun buildForeCastViewModel(list: com.globant.codechanllenge.data.model.List): ForecastModel {
        val highTemperatureString =
            list.main.tempMax.let {
                String.format(
                    Locale.getDefault(),
                    "%.0f",
                    kelvinToFahrenheit(it)
                )
            } ?: "0"

        val lowTemperatureString =
            list.main.tempMin.let {
                String.format(
                    Locale.getDefault(),
                    "%.0f",
                    kelvinToFahrenheit(it)
                )
            } ?: "0"

        val dateString = formatDateTime(list.dtTxt).first

        val windSpeedString =
            getWindSpeed(list.wind.speed)

        val windSpeedDirection =
            list.wind.deg.let { degreesToCardinalDirection(it) }

        val iconUrl =
            list.weather[0].icon.let { "https://openweathermap.org/img/w/${it}.png" }

        val description = capitalizeWords(list.weather[0].description.let { it } ?: "")

        return ForecastModel(
            highTemperatureString,
            lowTemperatureString,
            dateString,
            windSpeedString,
            windSpeedDirection,
            iconUrl,
            description,
            formatDateTime(list.dtTxt).second
        )
    }

    private fun getWindSpeed(speedInMetersPerSecond: Double): String {
        return String.format(
            Locale.getDefault(),
            "%.0f",
            metersPerSecondToMilesPerHour(speedInMetersPerSecond)
        )
    }

    private fun getFahrenheitTemperature(kelvin: Double): String {
        return String.format(Locale.getDefault(), "%.0f", kelvinToFahrenheit(kelvin))
    }

    private fun getCelsiusTemperature(kelvin: Double): String {
        return String.format(Locale.getDefault(), "%.0f", kelvinToCelsius(kelvin))
    }


    private fun capitalizeWords(input: String): String {
        return input.split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
    }


    fun formatDateTime(inputDateTime: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        val outputHourFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val date = inputFormat.parse(inputDateTime)
        val formattedDate = date?.let { outputDateFormat.format(it) } ?: ""
        val formattedHour = date?.let { outputHourFormat.format(it) } ?: ""

        return Pair(formattedDate, formattedHour)
    }


    fun metersPerSecondToMilesPerHour(speedInMetersPerSecond: Double): Double {
        val metersPerHour = speedInMetersPerSecond * 3600
        val milesPerHour = metersPerHour / 1609.344
        return milesPerHour
    }

    private fun kelvinToFahrenheit(kelvin: Double): Double {
        return (kelvin - 273.15) * 9 / 5 + 32
    }

    fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    fun degreesToCardinalDirection(degrees: Int): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val normalizedDegrees = (degrees % 360 + 360) % 360
        val index = ((normalizedDegrees + 22.5) / 45).toInt() % 8
        return directions[index]
    }

}