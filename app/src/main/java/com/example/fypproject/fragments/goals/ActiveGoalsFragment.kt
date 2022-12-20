package com.example.fypproject.fragments.goals

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.adapter.GoalRecyclerAdapter
import com.example.fypproject.data.Goals
import com.example.fypproject.data.Posts
import com.example.fypproject.databinding.FragmentActiveGoalsBinding
import com.example.fypproject.fragments.communityPlatform.CommunityFragment
import com.example.fypproject.fragments.goals.dashboard.Communicator
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.example.fypproject.fragments.goals.GoalsDetailsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.ArrayList
import java.util.Calendar


class ActiveGoalsFragment : Fragment(), Communicator {

    private lateinit var binding : FragmentActiveGoalsBinding
    private lateinit var db : DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userArrayList :ArrayList<Goals>
//    private lateinit var auth : FirebaseAuth
//    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
//    var tempUId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentActiveGoalsBinding.inflate(inflater,container,false)
        //val view =inflater.inflate(R.layout.fragment_active_goals,container,false)

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid
        Log.v(TAG, "The user ID is : $userUId")

//        auth = FirebaseAuth.getInstance()
//        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Goals")

        userRecyclerView = binding.goalRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<Goals>()

        checkExpiredGoal()
        getGoalsData(userUId)

        return binding.root
    }

    private fun getGoalsData(userUId : String){

//        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        Log.v(TAG, "The user ID is : $userUId")

        val getData = db.child("Active").child(userUId)

        getData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()
                    for(goalSnapshot in snapshot.children){
                        val goals = goalSnapshot.getValue(Goals::class.java)
                        userArrayList.add(goals!!)
                    }
                    userRecyclerView.adapter = GoalRecyclerAdapter(userArrayList,this@ActiveGoalsFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun passData(position: Int, goalID : String, goal_title: String, goal_status: String, goal_target_date: String) {
        val bundle = Bundle()
        bundle.putInt("input_pos", position)
        bundle.putString("input_goal_id",goalID)
        bundle.putString("input_goal_title", goal_title)
        bundle.putString("input_goal_status",goal_status)
        bundle.putString("input_goal_target_date", goal_target_date)

        val transaction = this.parentFragmentManager.beginTransaction()
        val goalDetailsFragment = GoalsDetailsFragment()
        goalDetailsFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, goalDetailsFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun checkExpiredGoal(){

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH) + 1
        val day = today.get(Calendar.DAY_OF_MONTH)

        val getData = db.child("Active").child(userUId)

        getData.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()
                    for(goalSnapshot in snapshot.children){
                        val goals = goalSnapshot.getValue(Goals::class.java)
                        val goalTargetDate = formatter.parse(goals!!.goalTargetDate)
                        val todayDate = formatter.parse("$day-$month-$year")
                        val cmp = goalTargetDate.compareTo(todayDate)
                        if(cmp<0) {
                            updateExpiredGoal(goals.goalID, goals.goalName, goals.goalStatus, goals.goalTargetDate)
                            deleteGoal(goals.goalID)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

    private fun deleteGoal(goalID : String) {

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        db.child("Active").child(userUId)
            .child(goalID).removeValue()

    }

    private fun updateExpiredGoal(goalID : String ,goalName : String ,goalStatus : String, goalTargetDate : String) {

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val goalStatus = "Expired"
        val goal = Goals(goalID, goalName,goalStatus, goalTargetDate)

        db.child("Expired").child(userUId)
            .child(goalID).setValue(goal).addOnSuccessListener {

//                Toast.makeText(context, "Submit Successfully!", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

}