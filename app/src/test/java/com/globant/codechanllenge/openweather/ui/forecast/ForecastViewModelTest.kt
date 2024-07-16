package com.globant.codechanllenge.openweather.ui.forecast


import com.globant.codechanllenge.data.common.Result
import com.globant.codechanllenge.data.model.ForecastData
import com.globant.codechanllenge.domain.usecase.weather.GetForecastUseCase
import com.globant.codechanllenge.openweather.mapper.WeatherMapper
import com.globant.codechanllenge.openweather.mockdata.MockData
import com.globant.codechanllenge.openweather.ui.model.ForecastModel
import com.globant.codechanllenge.openweather.util.AppPreferences
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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

private const val DELAY: Long = 1000

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class ForecastViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private var mockResult = mockk<Result<ForecastData>>()

    private var mockGetForecastUseCase = mockk<GetForecastUseCase>()

    private var mockAppPreferences = mockk<AppPreferences>()

    private var mockAppPWeatherMapper = mockk<WeatherMapper>()

    private lateinit var viewModel: ForecastViewModel


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
    fun `creating a viewModel and calling get forecast5 exposes loading ui state`() = runTest {
        val mockForecastData = MockData.MockData.createMockForecastData()

        coEvery { mockAppPreferences.getLocation() } returns Pair(2.0, 2.0)
        coEvery { mockGetForecastUseCase.invoke(2.0, 2.0) } returns flowOf(mockResult)
        coEvery { mockResult.status } returns Result.Status.LOADING
        coEvery { mockResult.data?.list } returns mockForecastData.list
        coEvery { mockAppPWeatherMapper.buildForeCastViewModels(mockForecastData.list) } returns listOf<ForecastModel>()

        viewModel =
            ForecastViewModel(mockGetForecastUseCase, mockAppPreferences, mockAppPWeatherMapper)

        assertEquals(ForecastUiState.Loading, viewModel.state.value)
    }

    @Test
    fun `creating a viewModel and calling get forecast5 exposes OK ui state`() = runTest {

        val mockAppPreferences = mockk<AppPreferences>()
        val mockForecastData = MockData.MockData.createMockForecastData()
        coEvery { mockAppPreferences.getLocation() } returns MockData.MockData.createMockLocation()
        coEvery {
            mockGetForecastUseCase.invoke(
                MockData.MockData.createMockLocation().first,
                MockData.MockData.createMockLocation().second
            )
        } returns flowOf(mockResult)
        every { mockResult.status } returns Result.Status.OK
        every { mockResult.data?.list } returns mockForecastData.list
        every { mockAppPWeatherMapper.buildForeCastViewModels(mockForecastData.list) } returns listOf<ForecastModel>()

        viewModel =
            ForecastViewModel(mockGetForecastUseCase, mockAppPreferences, mockAppPWeatherMapper)

        advanceTimeBy(DELAY)
        val expectedReadyState = ForecastUiState.ForecastUiStateReady(listOf())
        assertEquals(expectedReadyState, viewModel.state.value)
    }


    @Test
    fun `creating a viewModel and calling get forecast5 exposes ERROR ui state`() = runTest {

        val mockAppPreferences = mockk<AppPreferences>()
        val mockForecastData = MockData.MockData.createMockForecastData()
        coEvery { mockAppPreferences.getLocation() } returns MockData.MockData.createMockLocation()
        coEvery {
            mockGetForecastUseCase.invoke(
                MockData.MockData.createMockLocation().first,
                MockData.MockData.createMockLocation().second
            )
        } returns flow {
            throw Exception("Simulated error")

        }
        every { mockResult.status } returns Result.Status.ERROR
        every { mockResult.data?.list } returns mockForecastData.list
        every { mockAppPWeatherMapper.buildForeCastViewModels(mockForecastData.list) } returns listOf<ForecastModel>()

        viewModel =
            ForecastViewModel(mockGetForecastUseCase, mockAppPreferences, mockAppPWeatherMapper)

        val expectedReadyState = ForecastUiState.ForecastUiStateError(error = "Unknown")
        assertEquals(expectedReadyState, viewModel.state.value)
    }
}

