package com.globant.codechanllenge.openweather.mockdata

import com.globant.codechanllenge.data.model.City
import com.globant.codechanllenge.data.model.Clouds
import com.globant.codechanllenge.data.model.Coord
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.Main
import com.globant.codechanllenge.data.model.Sys
import com.globant.codechanllenge.data.model.Weather
import com.globant.codechanllenge.data.model.WeatherData
import com.globant.codechanllenge.data.model.Wind
import com.globant.codechanllenge.openweather.ui.model.ForecastModel

object MockData {


    fun createMockForecastData(): ForecastData {
        return ForecastData(
            id = 1,
            main = "Clear",
            description = "Clear sky",
            icon = "01d",
            list = arrayListOf(createMockList())
        )
    }

    fun createMockList(): com.globant.codechanllenge.data.model.List {
        return com.globant.codechanllenge.data.model.List(
            dt = 1644121200,
            main = createMockMain(),
            clouds = createMockClouds(),
            wind = createMockWind(),
            weather = arrayListOf(createMockWeather()),
            visibility = 10000,
            pop = 0.1,
            sys = createMockSys(),
            dtTxt = "2022-02-06 12:00:00"
        )
    }

    private fun createMockMain(): Main {
        return Main(
            temp = 25.0,
            feelsLike = 26.0,
            tempMin = 24.0,
            tempMax = 26.5,
            pressure = 1010,
            seaLevel = 1020,
            grndLevel = 1005,
            humidity = 60,
            tempKf = 1.0
        )
    }

    private fun createMockClouds(): Clouds {
        return Clouds(
            all = 10
        )
    }

    private fun createMockWind(): Wind {
        return Wind(
            speed = 5.0,
            deg = 180,
            gust = 2.0
        )
    }

    private fun createMockWeather(): Weather {
        return Weather(
            id = 800,
            main = "Clear",
            description = "Clear sky",
            icon = "01d"
        )
    }

    private fun createMockSys(): Sys {
        return Sys(
            pod = "d"
        )
    }

    object MockData {
        fun createMockStart(): Long {
            return 1111
        }

        fun createMockLocation(): Pair<Double, Double> {
            return Pair(2.0, 2.0)
        }



        fun createMockForecastData(): ForecastData {
            return ForecastData(
                id = 1,
                main = "Clear",
                description = "Clear sky",
                icon = "01d",
                list = arrayListOf(createMockList())
            )
        }


        private fun createMockMain(): Main {
            return Main(
                temp = 25.0,
                feelsLike = 26.0,
                tempMin = 24.0,
                tempMax = 26.5,
                pressure = 1010,
                humidity = 60,
                seaLevel = 2,
                grndLevel = 2,
                tempKf = 50.3
            )
        }

        private fun createMockClouds(): Clouds {
            return Clouds(
                all = 10
            )
        }

        private fun createMockWind(): Wind {
            return Wind(
                speed = 5.0,
                deg = 180,
                5.0
            )
        }

        private fun createMockWeather(): Weather {
            return Weather(
                id = 800,
                main = "Clear",
                description = "Clear sky",
                icon = "01d"
            )
        }

        private fun createMockSys(): Sys {
            return Sys(
                pod = "d"
            )
        }

        fun createMockWeatherData(): WeatherData {
            return WeatherData(
                cod = "200",
                message = 123,
                cnt = 24,
                city = createMockCity(),
                name = "CityName",
                main = createMockMain(),
                wind = createMockWind(),
                weather = listOf(createMockWeather())
            )
        }

        fun createMockCity(): City {
            return City(
                id = 123,
                name = "MockCity",
                coord = createMockCoord(),
                country = "MockCountry",
                population = 1000000,
                timezone = 3600,
                sunrise = 1613216000,
                sunset = 1613256000
            )
        }

        private fun createMockCoord(): Coord {
            return Coord(
                lat = 123.456,
                lon = 987.654
            )
        }

        fun createMockForecastModel(): ForecastModel {
            val mockList = createMockList()

            return ForecastModel(
                highTemperature = mockList.main.tempMax.toString(),
                lowTemperature = mockList.main.tempMin.toString(),
                date = mockList.dtTxt,
                windSpeed = mockList.wind.speed.toString(),
                windSpeedDirection = mockList.wind.deg.toString(),
                iconUrl = "https://example.com/${mockList.weather.first().icon}.png",
                description = mockList.weather.first().description,
                hour = "12:00 PM" // Adjust this based on your actual implementation
            )
        }
    }


}
