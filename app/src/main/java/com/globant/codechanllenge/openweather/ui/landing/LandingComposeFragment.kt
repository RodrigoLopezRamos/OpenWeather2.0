package com.globant.codechanllenge.openweather.ui.landing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.globant.codechanllenge.openweather.R
import com.globant.codechanllenge.openweather.ui.theme.Typography
import com.globant.codechanllenge.openweather.ui.theme.WeatherGreen
import com.globant.codechanllenge.openweather.ui.theme.transparent
import com.globant.codechanllenge.openweather.ui.theme.Purple700


class LandingComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WeatherScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    viewModel: LandingViewModel = hiltViewModel(),
) {
    val weatherState by viewModel.state.collectAsState()
    val landingModel = (weatherState as? WeatherUiState.WeatherUiStateReady)?.landingModel

    val isLoading = when (weatherState) {
        is WeatherUiState.Loading -> true
        else -> false
    }

    val pullRefreshState = rememberPullRefreshState(isLoading, { viewModel.refresh() })

    Scaffold(
        backgroundColor = Color.LightGray,
        content = {
            landingModel?.let {
                WeatherContent(viewModel, pullRefreshState)
            }
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.fillMaxWidth().
                wrapContentSize().
                size(40.dp), backgroundColor = Color.White
            )
        }, modifier = Modifier
            .fillMaxSize()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherContent(viewModel: LandingViewModel, pullRefreshState: PullRefreshState) {
    val weatherState by viewModel.state.collectAsState()  // Using collectAsState to observe the StateFlow
    val landingModel = (weatherState as? WeatherUiState.WeatherUiStateReady)?.landingModel
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.small_margin),
            vertical = dimensionResource(id = R.dimen.default_margin)
        )

    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.micro_margin))
            ) {
                landingModel?.cityName?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.small_margin))
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(dimensionResource(id = R.dimen.micro_margin))
                    ) {
                        Text(
                            text = "Wind",
                            fontSize = Typography.subtitle1.fontSize,
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.micro_margin))
                        )
                        Text(
                            text = "${landingModel?.windSpeedDirection} ",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.micro_margin))
                        )
                        Text(
                            text = "${landingModel?.windSpeed} mph",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.micro_margin))
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(1.dp)
                    ) {
                        Text(
                            text = "Feels Like: ${landingModel?.feelsLike}°",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(1.dp)
                                .align(Alignment.End)
                        )
                        Text(
                            text = "${landingModel?.fahrenheitTemperature} °/${landingModel?.celsiusTemperature}°",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(1.dp)
                                .align(Alignment.End)
                        )
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.extra_large_margin)))
                    }
                }
            }
        }

        landingModel?.let {
            items(it.forecastList) { forecastModel ->
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_margin)))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = WeatherGreen)
                ) {
                    Text(
                        text = forecastModel.hour,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(1f)
                            .padding(dimensionResource(id = R.dimen.default_margin))
                    )


                    Text(
                        text = "${forecastModel.highTemperature}°F",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 100.dp)
                    )
                    val imageUrl = forecastModel.iconUrl
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.extra_large_margin))
                            .size(50.dp)
                    )
                }
            }
        }
    }
}


