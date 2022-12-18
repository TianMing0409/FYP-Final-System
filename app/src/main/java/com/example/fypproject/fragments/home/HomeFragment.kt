package com.example.fypproject.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.fypproject.R
import com.example.fypproject.databinding.FragmentCommunityBinding
import com.example.fypproject.databinding.FragmentHomeBinding
import com.example.fypproject.databinding.FragmentRecommendationBinding
import com.example.fypproject.fragments.recommendation.RecommendationFragment

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view =inflater.inflate(R.layout.fragment_home,container,false)
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.btnRecomm.setOnClickListener(){
            val recommDialog = RecommendationFragment()

            recommDialog.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
        }


        return binding.root
    }

}