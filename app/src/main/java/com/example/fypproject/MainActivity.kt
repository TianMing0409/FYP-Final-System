package com.example.fypproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fypproject.databinding.ActivityMainBinding
import com.example.fypproject.fragments.communityPlatform.communityDashboard.CommunityDashboardFragment
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.example.fypproject.fragments.loginSignUp.OnBoardingFragment
import com.example.fypproject.fragments.loginSignUp.UserProfileFragment
import com.example.fypproject.fragments.moodCheckIn.MoodCheckInFragment
import com.example.fypproject.fragments.stats.StatsFragment


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        replaceFragment(OnBoardingFragment())
//
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener{
//            when(it.itemId){
//                R.id.home -> replaceFragment(MoodCheckInFragment())
//                R.id.stats -> replaceFragment(StatsFragment())
//                R.id.communityPlatform -> replaceFragment(CommunityDashboardFragment())
//                R.id.goals -> replaceFragment(DashBoardFragment())
//                R.id.profile -> replaceFragment(UserProfileFragment())
//            }
//            true
//        }
//
//    }
//
//    private fun replaceFragment(fragment: Fragment){
//        if(fragment!=null ){
//
//            val fragmentTransaction  = supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_container,fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//    }
//
//}

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    private val onBoardingFragment = OnBoardingFragment()
    private val homeFragment = MoodCheckInFragment()
    private val statsFragment = StatsFragment()
    private val dashBoardFragment = DashBoardFragment()
    private val profileFragment = UserProfileFragment()
    private val communityDashboardFragment = CommunityDashboardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(onBoardingFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(homeFragment)
                }
                R.id.stats ->{
                    replaceFragment(statsFragment)
                }
                R.id.communityPlatform ->{
                    replaceFragment(communityDashboardFragment)
                }
                R.id.goals -> {
                    replaceFragment(dashBoardFragment)
                }
                R.id.profile -> {
                    replaceFragment(profileFragment)
                }
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