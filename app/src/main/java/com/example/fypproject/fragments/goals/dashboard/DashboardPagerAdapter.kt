package com.example.fypproject.fragments.goals.dashboard

import android.content.ContentValues
import android.util.Log
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.fypproject.R
import com.example.fypproject.fragments.goals.ActiveGoalsFragment
import com.example.fypproject.fragments.goals.CompletedGoalsFragment
import com.example.fypproject.fragments.goals.ExpiredGoalsFragment
import com.google.firebase.auth.FirebaseAuth

class DashboardPagerAdapter(
    fragmentActivity: FragmentActivity,lifecycle: Lifecycle
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

