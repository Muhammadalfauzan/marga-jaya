package com.example.kamandanoe.ui.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistoryPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Dua tab: Past Bookings dan Upcoming Bookings

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingFragment() // Fragment untuk Past Bookings
            1 -> FinishedFragment() // Fragment untuk Upcoming Bookings
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
