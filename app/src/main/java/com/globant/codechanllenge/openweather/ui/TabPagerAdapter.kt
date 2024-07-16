package com.globant.codechanllenge.openweather.ui

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.globant.codechanllenge.openweather.R
import com.globant.codechanllenge.openweather.ui.forecast.fragment.ForecastFragment
import com.globant.codechanllenge.openweather.ui.landing.LandingComposeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val tabIcons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_forecast
    )

    private val tabIconsSelected = listOf(
        R.drawable.ic_home_selected,
        R.drawable.ic_forecast_selected
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LandingComposeFragment()
            1 -> ForecastFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    fun setupTabLayout(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            val customTab = when (position) {
                0 -> LayoutInflater.from(tabLayout.context)
                    .inflate(R.layout.custom_tab_home, null) as ConstraintLayout

                1 -> LayoutInflater.from(tabLayout.context)
                    .inflate(R.layout.custom_forecast_tab, null) as ConstraintLayout

                else -> throw IllegalArgumentException("Invalid tab position")
            }

            val tabIcon = customTab.findViewById<ImageView>(R.id.tab_icon)
            tabIcon.setImageResource(tabIcons[position])

            val tabText = customTab.findViewById<TextView>(R.id.tab_text)
            tabText.text = getTabTitle(position)

            val colorStateList =
                ContextCompat.getColorStateList(tabText.context, R.color.tab_text_color)
            tabText.setTextColor(colorStateList)

            tab.customView = customTab

            if (position == 0) {
                tabIcon.setImageResource(tabIconsSelected[0])
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIcon = tab?.customView?.findViewById<ImageView>(R.id.tab_icon)
                tabIcon?.setImageResource(tabIconsSelected[tab.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabIcon = tab?.customView?.findViewById<ImageView>(R.id.tab_icon)
                tabIcon?.setImageResource(tabIcons[tab.position])
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> "Home"
            1 -> "Forecast"
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}
