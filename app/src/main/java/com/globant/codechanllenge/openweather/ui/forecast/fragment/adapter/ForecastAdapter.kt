package com.globant.codechanllenge.openweather.ui.forecast.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.globant.codechanllenge.openweather.R
import com.globant.codechanllenge.openweather.ui.model.ForecastModel
import coil.load

class ForecastAdapter(
    private val context: Context,
    private var forecasts: List<ForecastModel>
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecasts[position]
        holder.textHighTemp.text = context.getString(R.string.high_temp, forecast.highTemperature)
        holder.textLowTemp.text = context.getString(R.string.low_temp, forecast.lowTemperature)
        holder.textDate.text = forecast.date
        holder.textWindSpeed.text = context.getString(R.string.wind_speed_value, forecast.windSpeed)
        holder.textWindDirection.text = forecast.windSpeedDirection
        holder.textDescription.text = forecast.description
        holder.imageWeatherIcon.load(forecast.iconUrl)
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHighTemp: TextView = itemView.findViewById(R.id.highTemp)
        val textLowTemp: TextView = itemView.findViewById(R.id.lowTemp)
        val textDate: TextView = itemView.findViewById(R.id.date)
        val textWindSpeed: TextView = itemView.findViewById(R.id.windSpeedValue)
        val textWindDirection: TextView = itemView.findViewById(R.id.windSpeedDirection)
        val textDescription: TextView = itemView.findViewById(R.id.weatherDescription)
        val imageWeatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)

    }

    fun setData(newForecasts: kotlin.collections.List<ForecastModel>) {
        forecasts = newForecasts
        notifyDataSetChanged()
    }
}
