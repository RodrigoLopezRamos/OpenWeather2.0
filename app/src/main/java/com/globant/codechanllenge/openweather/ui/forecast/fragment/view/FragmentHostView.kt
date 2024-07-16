package com.globant.codechanllenge.openweather.ui.forecast.fragment.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.globant.codechanllenge.openweather.ui.forecast.fragment.ForecastFragment

class FragmentHostView(context: Context) : FrameLayout(context) {
    init {
        id = View.generateViewId()
    }

    fun setFragment(fragment: Fragment) {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(id, fragment)
        transaction.commit()
    }
}
