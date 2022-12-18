package com.example.fypproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fypproject.databinding.ActivityMainBinding
import com.example.fypproject.fragments.communityPlatform.CommunityFragment
import com.example.fypproject.fragments.communityPlatform.MyActivityFragment
import com.example.fypproject.fragments.communityPlatform.communityDashboard.CommunityDashboardFragment
import com.example.fypproject.fragments.goals.ActiveGoalsFragment
import com.example.fypproject.fragments.goals.CompletedGoalsFragment
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.example.fypproject.fragments.home.HomeFragment
import com.example.fypproject.fragments.loginSignUp.LoginFragment
import com.example.fypproject.fragments.loginSignUp.OnBoardingFragment
import com.example.fypproject.fragments.loginSignUp.UserProfileFragment
import com.example.fypproject.fragments.moodCheckIn.MoodCheckInFragment
import com.example.fypproject.fragments.profile.ProfileFragment
import com.example.fypproject.fragments.recommendation.RecommendationFragment
import com.example.fypproject.fragments.stats.StatsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val profileFragment = UserProfileFragment()
    private val onBoardingFragment = OnBoardingFragment()
    private val statsFragment = StatsFragment()
    private val dashBoardFragment = DashBoardFragment()
    private val communityDashboardFragment = CommunityDashboardFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(onBoardingFragment)


        binding.bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home -> replaceFragment(MoodCheckInFragment())
                R.id.stats ->replaceFragment(statsFragment)
                R.id.communityPlatform -> replaceFragment(communityDashboardFragment)
                R.id.goals -> replaceFragment(dashBoardFragment)
                R.id.profile -> replaceFragment(profileFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null ){

            val fragmentTransaction  = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

}