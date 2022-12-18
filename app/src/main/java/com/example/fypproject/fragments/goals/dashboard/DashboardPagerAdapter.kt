package com.example.fypproject.fragments.goals.dashboard

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fypproject.fragments.goals.ActiveGoalsFragment
import com.example.fypproject.fragments.goals.CompletedGoalsFragment
import com.example.fypproject.fragments.goals.ExpiredGoalsFragment

class DashboardPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ActiveGoalsFragment()
            1 -> return CompletedGoalsFragment()
            2 -> return ExpiredGoalsFragment()
        }
        return ActiveGoalsFragment()
    }
}

//Working pure dashboard without content (active,completed),
//class DashboardPagerAdapter(
//    fragmentActivity: FragmentActivity,
//) : FragmentStateAdapter(fragmentActivity) {
//
//    override fun getItemCount() = 2
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> ActiveGoalsFragment.newInstance()
//            else -> CompletedGoalsFragment.newInstance()
//        }
//    }
//}



