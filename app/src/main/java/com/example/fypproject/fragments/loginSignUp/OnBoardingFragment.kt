package com.example.fypproject.fragments.loginSignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fypproject.R
import com.example.fypproject.databinding.FragmentActiveGoalsBinding
import com.example.fypproject.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private lateinit var binding : FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility  = View.GONE

        binding = FragmentOnBoardingBinding.inflate(inflater,container,false)

        binding.btnGetStarted.setOnClickListener {
            replaceFragment(SignupFragment())

        }

        return binding.root

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = this.parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}