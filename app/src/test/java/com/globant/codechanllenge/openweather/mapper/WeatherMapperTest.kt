package com.globant.codechanllenge.openweather.mapper

import com.globant.codechanllenge.data.model.City
import com.globant.codechanllenge.data.model.Coord
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.Main
import com.globant.codechanllenge.data.model.Weather
import com.globant.codechanllenge.data.model.WeatherData
import com.globant.codechanllenge.data.model.Wind
import com.globant.codechanllenge.openweather.ui.landing.model.LandingModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class WeatherMapperTest {

    @Mock
    lateinit var forecastData: ForecastData

    @InjectMocks
    lateinit var weatherMapper: WeatherMapper

    lateinit var mockMain: Main

    lateinit var mockCity: City

    lateinit var mockCoord: Coord

    lateinit var mockWind: Wind

    lateinit var mockWeather: Weather


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mockCoord = mock(Coord::class.java)
        `when`(mockCoord.lat).thenReturn(40.7306)
        `when`(mockCoord.lon).thenReturn(-73.9352)

        mockMain = mock(Main::class.java)
        `when`(mockMain.temp).thenReturn(270.55)
        `when`(mockMain.feelsLike).thenReturn(264.76)
        `when`(mockMain.tempMin).thenReturn(268.06)
        `when`(mockMain.tempMax).thenReturn(272.12)
        `when`(mockMain.pressure).thenReturn(1015)
        `when`(mockMain.seaLevel).thenReturn(1020)
        `when`(mockMain.grndLevel).thenReturn(1008)
        `when`(mockMain.humidity).thenReturn(87)
        `when`(mockMain.tempKf).thenReturn(2.49)

        mockCity = mock(City::class.java)
        `when`(mockCity.id).thenReturn(5125125)
        `when`(mockCity.name).thenReturn("Long Island City")
        `when`(mockCity.coord).thenReturn(mockCoord)
        `when`(mockCity.country).thenReturn("US")
        `when`(mockCity.population).thenReturn(100000)
        `when`(mockCity.timezone).thenReturn(-18000)
        `when`(mockCity.sunrise).thenReturn(1705407443)
        `when`(mockCity.sunset).thenReturn(1705441972)

        mockWind = mock(Wind::class.java)
        `when`(mockWind.speed).thenReturn(5.36)
        `when`(mockWind.deg).thenReturn(12)
        `when`(mockWind.gust).thenReturn(7.15)

        mockWeather = mock(Weather::class.java)
        `when`(mockWeather.id).thenReturn(601)
        `when`(mockWeather.main).thenReturn("Snow")
        `when`(mockWeather.description).thenReturn("snow")
        `when`(mockWeather.icon).thenReturn("13n")
    }


    @Test
    fun `testBuildLandingViewModel with ValidWeatherData shouldMapToLandingModel`() {
        val weatherData = WeatherData(
            "222",
            1,
            1,
            mockCity,
            "City",
            mockMain,
            mockWind,
            listOf(mockWeather)
        )

        val result: LandingModel = weatherMapper.buildLandingViewModel(weatherData, forecastData)

        assertEquals("City", result.cityName)
        assertEquals("N", result.windSpeedDirection)
        assertEquals("12", result.windSpeed)
        assertEquals("17", result.feelsLike)
        assertEquals("-3", result.celsiusTemperature)
        assertEquals("27", result.fahrenheitTemperature)
        assertNotNull(result.forecastList)
    }

    @Test
    fun `testKelvinToCelsius converts Kelvin temperatures to Celsius`() {
        assertEquals(0.0, weatherMapper.kelvinToCelsius(273.15), 0.001)
        assertEquals(25.0, weatherMapper.kelvinToCelsius(298.15), 0.001)
        assertEquals(-10.0, weatherMapper.kelvinToCelsius(263.15), 0.001)
        assertEquals(-273.15, weatherMapper.kelvinToCelsius(0.0), 0.001)
        assertEquals(100.0, weatherMapper.kelvinToCelsius(373.15), 0.001)
    }

    @Test
    fun `testDegreesToCardinalDirection converts degrees to cardinal direction`() {
        assertEquals("N", weatherMapper.degreesToCardinalDirection(0))
        assertEquals("NE", weatherMapper.degreesToCardinalDirection(45))
        assertEquals("E", weatherMapper.degreesToCardinalDirection(90))
        assertEquals("SE", weatherMapper.degreesToCardinalDirection(135))
        assertEquals("S", weatherMapper.degreesToCardinalDirection(180))
        assertEquals("SW", weatherMapper.degreesToCardinalDirection(225))
        assertEquals("W", weatherMapper.degreesToCardinalDirection(270))
        assertEquals("NW", weatherMapper.degreesToCardinalDirection(315))
        assertEquals("N", weatherMapper.degreesToCardinalDirection(360))
        assertEquals("N", weatherMapper.degreesToCardinalDirection(720))
        assertEquals("NW", weatherMapper.degreesToCardinalDirection(-45))
        assertEquals("NE", weatherMapper.degreesToCardinalDirection(-315))
    }

    @Test
    fun `test meters per second to miles per hour`() {
        val speedInMetersPerSecond = 10.0
        val expectedSpeedInMilesPerHour = 22.3694
        val result = weatherMapper.metersPerSecondToMilesPerHour(speedInMetersPerSecond)
        assertEquals(expectedSpeedInMilesPerHour, result, 0.001)
    }

    @Test
    fun `test meters per second to miles per hour conversion`() {
        val speedInMetersPerSecond = 10.0
        val expectedSpeedInMilesPerHour = 22.3694
        val result = weatherMapper.metersPerSecondToMilesPerHour(speedInMetersPerSecond)
        assertEquals(expectedSpeedInMilesPerHour, result, 0.001)
    }

    @Test
    fun `test zero meters per second to miles per hour conversion`() {
        val speedInMetersPerSecond = 0.0
        val result = weatherMapper.metersPerSecondToMilesPerHour(speedInMetersPerSecond)
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun `test negative meters per second to miles per hour conversion`() {
        val speedInMetersPerSecond = -5.0
        val expectedSpeedInMilesPerHour = -11.1847
        val result = weatherMapper.metersPerSecondToMilesPerHour(speedInMetersPerSecond)
        assertEquals(expectedSpeedInMilesPerHour, result, 0.001)
    }
}
