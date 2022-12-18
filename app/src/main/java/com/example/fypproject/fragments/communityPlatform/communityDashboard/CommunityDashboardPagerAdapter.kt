package com.example.fypproject.fragments.communityPlatform.communityDashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fypproject.R
import com.example.fypproject.fragments.communityPlatform.CommunityFragment
import com.example.fypproject.fragments.communityPlatform.MyActivityFragment
import com.example.fypproject.fragments.communityPlatform.MyBookmarkFragment
import com.example.fypproject.fragments.goals.ActiveGoalsFragment
import com.example.fypproject.fragments.goals.CompletedGoalsFragment

class CommunityDashboardPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CommunityFragment()
            1 -> return MyActivityFragment()
            2 -> return MyBookmarkFragment()
        }
        return CommunityFragment()
    }
}