package com.example.fypproject.fragments.recommendation

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.PyApplication
import com.example.fypproject.R
import com.example.fypproject.databinding.FragmentRecommendationBinding
import com.example.fypproject.fragments.goals.AddGoalsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class RecommendationFragment : DialogFragment() {

    private lateinit var binding : FragmentRecommendationBinding

    private lateinit var db : DatabaseReference
//    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecommendationBinding.inflate(inflater,container,false)

        db = FirebaseDatabase.getInstance().getReference("Stats")


        binding.cancelBtn.setOnClickListener(){
            dismiss()
        }

        val linkGoaltv = binding.goToGoalLink
        val content = SpannableString("Click here to set your own goal")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        linkGoaltv.text = content

        binding.goToGoalLink.setOnClickListener(){

            dismiss()
            val fragmentTransaction  = this.parentFragmentManager.beginTransaction()
            val addGoalFragment = AddGoalsFragment()

            fragmentTransaction.replace(R.id.fragment_container,addGoalFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        getMoodData()   // Get user mood condition

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null ){

            val fragmentTransaction  = this.parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun getRecommMusic(userMood : String): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")

        return pythonFile.callAttr("recomm_music",userMood).toString()
    }

    private fun getRecommMusicDay(): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recommMusic_day").toString()
    }

    private fun getRecommMusicMonth(): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recommMusic_month").toString()
    }

    private fun getRecommMusicYear(): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recommMusic_year").toString()
    }

    private fun getRecommMovie(userMood : String): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recomm_movie",userMood).toString()
    }

    private fun getRecommMovieDay(): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recommMovie_day").toString()
    }

    private fun getRecommMovieMonth(): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recommMovie_month").toString()
    }

    private fun getRecommMovieYear(): String{

        val python : Python = Python.getInstance()

        val pythonFile : PyObject = python.getModule("MoodRecommendationModule")
        return pythonFile.callAttr("recommMovie_year").toString()
    }


    private fun getMoodData() {

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val getData = db

        getData.child(userUId).get().addOnSuccessListener {
            val mood = it.child("mood").value.toString()
            val moodText : String

            if(mood == "verySad" || mood == "UPSET" || mood == "RED" || mood == "OneStar"){
                moodText = "Very Sad"
            }else if (mood == "sad" || mood == "DOWN" || mood == "ORANGE" || mood == "TwoStar"){
                moodText = "Sad"
            }else if (mood == "sad" || mood == "NEUTRAL" || mood == "YELLOW" || mood == "ThreeStar"){
                moodText = "Normal"
            }else if (mood == "happy" || mood == "COPING" || mood == "GREEN" || mood == "FourStar"){
                moodText = "Happy"
            }else{
                moodText = "Very Happy"
            }

            binding.inputMood.setText(moodText)
            binding.recommendationMusic.text = getRecommMusic(mood)
            binding.recommendationMovie.text = getRecommMovie(mood)

            //Add music recommendation as goal (Direct to Add goal page)
            binding.addRecommMusicBtn.setOnClickListener(){
                val recommMusic = binding.recommendationMusic.text.toString()
                val recommMusic_day = getRecommMusicDay().toInt()
                val recommMusic_month = getRecommMusicMonth().toInt()
                val recommMusic_year = getRecommMusicYear().toInt()

                val bundle = Bundle()
                bundle.putString("recomm_goal",recommMusic)
                bundle.putInt("recomm_day",recommMusic_day)
                bundle.putInt("recomm_month",recommMusic_month)
                bundle.putInt("recomm_year",recommMusic_year)

                val transaction = this.parentFragmentManager.beginTransaction()
                val addGoalsFragment = AddGoalsFragment()
                addGoalsFragment.arguments = bundle

                dismiss()      // Close the dialog fragment
                transaction.replace(R.id.fragment_container, addGoalsFragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }

            //Add movie recommendation as goal (Direct to Add goal page)
            binding.addRecommMovieBtn.setOnClickListener(){
                val recommMovie = binding.recommendationMovie.text.toString()
                val recommMovie_day = getRecommMovieDay().toInt()
                val recommMovie_month = getRecommMovieMonth().toInt()
                val recommMovie_year = getRecommMovieYear().toInt()

                val bundle = Bundle()
                bundle.putString("recomm_goal",recommMovie)
                bundle.putInt("recomm_day",recommMovie_day)
                bundle.putInt("recomm_month",recommMovie_month)
                bundle.putInt("recomm_year",recommMovie_year)

                val transaction = this.parentFragmentManager.beginTransaction()
                val addGoalsFragment = AddGoalsFragment()
                addGoalsFragment.arguments = bundle

                dismiss()      // Close the dialog fragment
                transaction.replace(R.id.fragment_container, addGoalsFragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }



        }
    }


}