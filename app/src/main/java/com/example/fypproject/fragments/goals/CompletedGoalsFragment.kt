package com.example.fypproject.fragments.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.adapter.GoalRecyclerAdapter
import com.example.fypproject.data.Goals
import com.example.fypproject.databinding.FragmentCompletedGoalsBinding
import com.example.fypproject.fragments.goals.dashboard.Communicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class CompletedGoalsFragment : Fragment(R.layout.fragment_completed_goals), Communicator {

    //private lateinit var binding : FragmentCompletedGoalsBinding
    //private val binding by viewBinding(FragmentCompletedGoalsBinding::bind)
    private lateinit var binding : FragmentCompletedGoalsBinding

    private lateinit var db : DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userArrayList :ArrayList<Goals>
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    companion object {
        fun newInstance() = CompletedGoalsFragment()
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentCompletedGoalsBinding.inflate(inflater,container,false)
//
//        return binding.root
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedGoalsBinding.inflate(inflater,container,false)

//        postToList()   //Dummy function, need to remove

//        binding.goalRecyclerView.layoutManager = LinearLayoutManager(context)
//        binding.goalRecyclerView.adapter = GoalRecyclerAdapter(titleList,dateList,imageList)

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Goals")


        userRecyclerView = binding.goalRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<Goals>()

        getCompletedGoalsData()



        return binding.root
    }

    private fun getCompletedGoalsData(){

        val getData = db.child("Completed").child(userUId)

        getData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()
                    for(goalSnapshot in snapshot.children){
                        val goals = goalSnapshot.getValue(Goals::class.java)
                        userArrayList.add(goals!!)
                    }
                    userRecyclerView.adapter = GoalRecyclerAdapter(userArrayList,this@CompletedGoalsFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun clickedGoalItem(goal: Goals) {

    }

    override fun passData(position: Int, goalID : String, goal_title: String, goal_status: String, goal_target_date: String) {
        val bundle = Bundle()
        bundle.putInt("input_pos", position)
        bundle.putString("input_goal_id",goalID)
        bundle.putString("input_goal_title", goal_title)
        bundle.putString("input_goal_status",goal_status)
        bundle.putString("input_goal_target_date", goal_target_date)

        val transaction = this.parentFragmentManager.beginTransaction()
        val completedGoalDetailsFragment = CompletedGoalDetailsFragment()
        completedGoalDetailsFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, completedGoalDetailsFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


//    //Dummy function to test data
//    private fun addToList(title:String, date: String,image: Int){
//        titleList.add(title)
//        dateList.add(date)
//        imageList.add(image)
//    }
//
//    //Dummy function to test data
//    private fun postToList(){
//        for(i in 1..25){
//            addToList("Completed Goal Title $i","Date $i",R.drawable.keyboard_arrow_right_icon)
//        }
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }



}