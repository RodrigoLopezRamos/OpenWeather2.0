package com.globant.codechanllenge.openweather.ui.landing

import com.globant.codechanllenge.data.common.Result
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.data.model.WeatherData
import com.globant.codechanllenge.domain.usecase.weather.GetWeatherUseCase
import com.globant.codechanllenge.openweather.mapper.WeatherMapper
import com.globant.codechanllenge.openweather.mockdata.MockData
import com.globant.codechanllenge.openweather.util.AppPreferences
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class LandingViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val mockGetWeatherUseCase = mockk<GetWeatherUseCase>()
    private val mockAppPreferences = mockk<AppPreferences>()
    private val mockWeatherMapper = mockk<WeatherMapper>()

    private lateinit var viewModel: LandingViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `creating a viewModel and calling get WeatherUiState exposes loading ui state`() = runTest {
        val mockLocation = MockData.MockData.createMockLocation()
        val mockResult = mockk<Result<Pair<WeatherData, ForecastData>>>()
        val mockForecastData = MockData.MockData.createMockForecastData()

        coEvery { mockAppPreferences.getLocation() } returns mockLocation
        coEvery {
            mockGetWeatherUseCase.invoke(
                mockLocation.first,
                mockLocation.second,
                any(),
                any()
            )
        } returns flowOf(mockResult)

        coEvery { mockResult.status } returns Result.Status.LOADING
        coEvery { mockWeatherMapper.buildForeCastViewModels(mockForecastData.list) } returns listOf()

        viewModel = LandingViewModel(mockGetWeatherUseCase, mockAppPreferences, mockWeatherMapper)
        advanceTimeBy(1000)
        assertEquals(WeatherUiState.Loading, viewModel.state.value)
    }

    @Test
    fun `creating a viewModel and calling get WeatherUiState exposes success ui state`() = runTest {
        val mockLocation = MockData.MockData.createMockLocation()
        val mockResult = mockk<Result<Pair<WeatherData, ForecastData>>>()
        val mockWeatherData = MockData.MockData.createMockWeatherData()
        val mockForecastData = MockData.MockData.createMockForecastData()

        coEvery { mockAppPreferences.getLocation() } returns mockLocation
        coEvery {
            mockGetWeatherUseCase.invoke(
                mockLocation.first,
                mockLocation.second,
                any(),
                any()
            )
        } returns flowOf(mockResult)
        coEvery { mockResult.status } returns Result.Status.OK
        coEvery { mockResult.data } returns Pair(mockWeatherData, mockForecastData)
        coEvery { mockWeatherMapper.buildForeCastViewModels(mockForecastData.list) } returns listOf(
            MockData.MockData.createMockForecastModel()
        )

        viewModel = LandingViewModel(mockGetWeatherUseCase, mockAppPreferences, mockWeatherMapper)

        verify { mockWeatherMapper.buildLandingViewModel(mockWeatherData, mockForecastData) }
    }

    @Test
    fun `creating a viewModel and calling get WeatherUiState exposes error ui state`() = runTest {
        val mockLocation = MockData.MockData.createMockLocation()
        val mockResult = mockk<Result<Pair<WeatherData, ForecastData>>>()

        coEvery { mockAppPreferences.getLocation() } returns mockLocation
        coEvery {
            mockGetWeatherUseCase.invoke(
                mockLocation.first,
                mockLocation.second,
                any(),
                any()
            )
        } returns flowOf(mockResult)



        coEvery { mockResult.status } returns Result.Status.ERROR
        coEvery { mockResult.error?.type?.name } returns "SomeErrorType"

        viewModel = LandingViewModel(mockGetWeatherUseCase, mockAppPreferences, mockWeatherMapper)
        assertTrue(viewModel.state.value is WeatherUiState.WeatherUiStateError)
        assertEquals("SomeErrorType", (viewModel.state.value as WeatherUiState.WeatherUiStateError).error)
    }
}