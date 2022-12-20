package com.example.fypproject.fragments.communityPlatform.communityDashboard

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.fypproject.R
import com.example.fypproject.R.*
import com.example.fypproject.fragments.communityPlatform.communityDashboard.CommunityDashboardPagerAdapter
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class CommunityDashboardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(layout.fragment_community_dashboard,container,false)


        val userUId = FirebaseAuth.getInstance().currentUser!!.uid
        Log.v(ContentValues.TAG, "The user ID is : $userUId")


        val adapter = CommunityDashboardPagerAdapter(requireActivity())
        val viewPager : ViewPager2 = view.findViewById(R.id.viewpagerComm)
        viewPager.adapter = adapter

        val tabs : TabLayout = view.findViewById(R.id.tabLayoutComm)
        TabLayoutMediator(tabs,viewPager, CommunityDashboardFragment.TabConfigurationComm()).attach()

        viewPager.isSaveFromParentEnabled = false

        return view
    }

    class TabConfigurationComm: TabLayoutMediator.TabConfigurationStrategy {
        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            val tabNames = listOf("Community", "My Posts","Bookmarks")
            tab.setText(tabNames[position])
        }
    }

}