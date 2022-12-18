package com.example.fypproject.fragments.goals

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.example.fypproject.R
import com.example.fypproject.databinding.FragmentCongratulationBinding
import com.example.fypproject.databinding.FragmentRecommendationBinding
import kotlin.random.Random

class CongratulationFragment : DialogFragment() {

    private lateinit var binding : FragmentCongratulationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_congratulation, container, false)

//        val percentage = 65
//        val percent = percentage.toFloat() / 100
//        val dm = Resources.getSystem().displayMetrics
//        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
//        val percentWidth = rect.width() * percent
//        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

        binding = FragmentCongratulationBinding.inflate(inflater,container,false)


        val randomValues = Random.nextInt(0,4)
        if(randomValues == 0) {
            binding.goodRabbit.visibility = View.GONE
            binding.nice.visibility = View.GONE
            binding.tick.visibility = View.GONE
            binding.cheering.visibility = View.GONE
            binding.great.visibility = View.GONE
            binding.celebrate.visibility = View.GONE

            binding.congratulationAnimation.playAnimation()
            binding.congratulationGreet.playAnimation()

        }else if(randomValues == 1){
            binding.congratulationAnimation.visibility = View.GONE
            binding.congratulationGreet.visibility = View.GONE
            binding.nice.visibility = View.GONE
            binding.tick.visibility = View.GONE
            binding.cheering.visibility = View.GONE
            binding.great.visibility = View.GONE
            binding.celebrate.visibility = View.GONE

            binding.goodRabbit.playAnimation()

        }else if(randomValues == 2){
            binding.goodRabbit.visibility = View.GONE
            binding.congratulationAnimation.visibility = View.GONE
            binding.congratulationGreet.visibility = View.GONE
            binding.cheering.visibility = View.GONE
            binding.great.visibility = View.GONE
            binding.celebrate.visibility = View.GONE

            binding.nice.playAnimation()
            binding.tick.playAnimation()

        }else if(randomValues == 3){
            binding.goodRabbit.visibility = View.GONE
            binding.congratulationAnimation.visibility = View.GONE
            binding.congratulationGreet.visibility = View.GONE
            binding.nice.visibility = View.GONE
            binding.tick.visibility = View.GONE

            binding.cheering.playAnimation()
            binding.great.playAnimation()
            binding.celebrate.playAnimation()
        }

        binding.okCongratBtn.setOnClickListener(){
            dismiss()
        }


        return binding.root

    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(900,1050)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}