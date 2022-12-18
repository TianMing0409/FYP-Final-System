package com.example.fypproject.fragments.goals.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.fypproject.R
import com.example.fypproject.databinding.FragmentDashboardBinding
import com.example.fypproject.fragments.goals.AddGoalsFragment
import com.example.fypproject.util.viewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DashBoardFragment : Fragment(R.layout.fragment_dashboard) {

    //private lateinit var binding : FragmentDashboardBinding
    private val binding by viewBinding(FragmentDashboardBinding::bind)
    private lateinit var addGoalFab: FloatingActionButton
    private lateinit var dashboardPagerAdapter: DashboardPagerAdapter
    var tabTitle = arrayOf("Active", "Completed", "Overdue")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
//    binding = FragmentDashboardBinding.inflate(inflater,container,false)

        addGoalFab = view.findViewById(R.id.fabAddGoal)

        addGoalFab.setOnClickListener { v: View ->
            val activity = v!!.context as AppCompatActivity
            val fragmentAddGoals = AddGoalsFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentAddGoals).addToBackStack(null).commit()
        }

//    val pager =view.findViewById<ViewPager2>(R.id.viewpager)
//    val tl =view.findViewById <TabLayout>(R.id.tabLayout)
//    pager.adapter = DashboardPagerAdapter(requireActivity())

//        dashboardPagerAdapter = DashboardPagerAdapter(requireActivity())
//        initViewPager()
        val adapter = DashboardPagerAdapter(requireActivity())
        val viewPager: ViewPager2 = view.findViewById(R.id.viewpager)
        viewPager.adapter = adapter

        val tabs: TabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager, TabConfigurationGoal()).attach()

        return view
    }

    class TabConfigurationGoal : TabLayoutMediator.TabConfigurationStrategy {
        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            val tabNames = listOf("Active", "Completed", "Overdue")
            tab.setText(tabNames[position])
        }
    }
}
