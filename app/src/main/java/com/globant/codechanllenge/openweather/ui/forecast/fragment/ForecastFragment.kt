package com.globant.codechanllenge.openweather.ui.forecast.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.globant.codechanllenge.openweather.R
import com.globant.codechanllenge.openweather.databinding.FragmentForecastBinding
import com.globant.codechanllenge.openweather.ui.forecast.ForecastViewModel
import com.globant.codechanllenge.openweather.ui.forecast.ForecastUiState
import com.globant.codechanllenge.openweather.ui.forecast.fragment.adapter.ForecastAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var binding: FragmentForecastBinding
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forecastAdapter = ForecastAdapter(requireContext(), mutableListOf())
        binding.recyclerViewForecast.adapter = forecastAdapter
        val swipeRefreshLayout = binding.swipeRefreshLayout

        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_line
            )!!
        )
        val dividerDrawable = itemDecoration.drawable
        dividerDrawable?.let {
            val dividerHeight = resources.getDimensionPixelSize(R.dimen.default_margin)
            it.setBounds(0, 0, it.intrinsicWidth, dividerHeight)
        }
        binding.recyclerViewForecast.addItemDecoration(itemDecoration)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ForecastUiState.ForecastUiStateReady -> {
                            swipeRefreshLayout.isRefreshing = false
                            forecastAdapter.setData(state.forecastModels)
                        }

                        is ForecastUiState.Loading -> {
                            swipeRefreshLayout.isRefreshing = true
                        }

                        is ForecastUiState.ForecastUiStateError -> {
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                }
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getForecast5()
        }
    }
}
