package com.example.fypproject.fragments.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fypproject.R
import com.example.fypproject.databinding.FragmentCompletedGoalDetailsBinding
import com.example.fypproject.databinding.FragmentCompletedGoalsBinding
import com.example.fypproject.databinding.FragmentGoalsDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CompletedGoalDetailsFragment : Fragment() {

    private lateinit var binding : FragmentCompletedGoalDetailsBinding

    private lateinit var db : DatabaseReference
    private lateinit var db2 : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    var inputPos: Int? = null
    var inputGoalId: String = ""
    var inputGoalTitle: String = ""
    var inputGoalStatus: String = ""
    var inputGoalTargetDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_completed_goal_details, container, false)
        binding = FragmentCompletedGoalDetailsBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Goals")
        db2 = FirebaseDatabase.getInstance().getReference("Stats")

        //fetch data
        inputPos = arguments?.getInt("input_pos")
        inputGoalId = arguments?.getString("input_goal_id").toString()
        inputGoalTitle = arguments?.getString("input_goal_title").toString()
        inputGoalStatus = arguments?.getString("input_goal_status").toString()
        inputGoalTargetDate = arguments?.getString("input_goal_target_date").toString()

        binding.completedGoal?.text = inputGoalTitle
        binding.scheduleDate?.text = inputGoalTargetDate

        return binding.root
    }

}