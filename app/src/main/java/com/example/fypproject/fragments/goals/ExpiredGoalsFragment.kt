package com.example.fypproject.fragments.goals

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.adapter.GoalRecyclerAdapter
import com.example.fypproject.data.Goals
import com.example.fypproject.databinding.FragmentActiveGoalsBinding
import com.example.fypproject.databinding.FragmentExpiredGoalsBinding
import com.example.fypproject.fragments.goals.dashboard.Communicator
import com.example.fypproject.fragments.goals.GoalsDetailsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ExpiredGoalsFragment : Fragment(R.layout.fragment_expired_goals), Communicator {

    private lateinit var binding : FragmentExpiredGoalsBinding
    private lateinit var db : DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userArrayList : ArrayList<Goals>
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_expired_goals, container, false)
        binding = FragmentExpiredGoalsBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Goals")


        userRecyclerView = binding.goalRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<Goals>()

        getExpiredGoalsData()

        return binding.root
    }

    private fun getExpiredGoalsData(){

        val getData = db.child("Expired").child(userUId)

        getData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()
                    for(goalSnapshot in snapshot.children){
                        val goals = goalSnapshot.getValue(Goals::class.java)
                        userArrayList.add(goals!!)
                    }
                    userRecyclerView.adapter = GoalRecyclerAdapter(userArrayList,this@ExpiredGoalsFragment)
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

}