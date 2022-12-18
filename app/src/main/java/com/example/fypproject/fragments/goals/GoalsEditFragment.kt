package com.example.fypproject.fragments.goals

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.fypproject.R
import com.example.fypproject.data.Goals
import com.example.fypproject.databinding.FragmentGoalsDetailsBinding
import com.example.fypproject.databinding.FragmentGoalsEditBinding
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class GoalsEditFragment : Fragment() {

    private lateinit var binding : FragmentGoalsEditBinding

    private lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    var inputPos: Int? = null
    var inputGoalId : String = ""
    var inputGoalTitle: String = ""
    var inputGoalStatus: String = ""
    var inputGoalTargetDate: String = ""
    private lateinit var userArrayList : ArrayList<Goals>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val view =inflater.inflate(R.layout.fragment_goals_edit,container,false)
        binding = FragmentGoalsEditBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Goals")

        val btnSave: Button = binding.saveBtn
        val btnDelete : Button = binding.delBtn

        //fetch data
        inputGoalId = arguments?.getString("ori_goal_id").toString()
        inputGoalTitle = arguments?.getString("ori_goal_title").toString()
        inputGoalStatus = arguments?.getString("ori_goal_status").toString()
        inputGoalTargetDate = arguments?.getString("ori_goal_target_date").toString()

        binding.inputNewGoal.setText(inputGoalTitle)
        binding.inputNewDate.text = inputGoalTargetDate

        userArrayList = arrayListOf<Goals>()

        //Calendar mode date picker
        binding.inputNewDate.setOnClickListener(){
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this.requireActivity(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.inputNewDate.text = dat
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

        btnSave.setOnClickListener(){
            validateData()
        }

        btnDelete.setOnClickListener(){
            deleteGoal(inputGoalId,inputGoalTitle,inputGoalStatus,inputGoalTargetDate)
            deleteExpiredGoal(inputGoalId)
        }

        return binding.root
    }

    private var goalID=""
    private var goalName=""
    private var goalTargetDate = ""
    private var goalStatus = ""

    private fun validateData(){
        goalID= inputGoalId
        goalName = binding.inputNewGoal.text.toString().trim()
        goalStatus = inputGoalStatus
        goalTargetDate = binding.inputNewDate.text.toString().trim()

        if(goalName.isEmpty()){
            Toast.makeText(context, "Enter goal...", Toast.LENGTH_SHORT).show()
        }
        else{
            //Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()

            editGoal(goalID,goalName,goalStatus,goalTargetDate)
            checkExpiredGoals(goalTargetDate,goalID)
        }
    }

    private fun editGoal(goalID : String ,goalName : String ,goalStatus : String, goalTargetDate : String) {

        val goal = Goals(goalID, goalName,goalStatus, goalTargetDate)

        db.child("Active").child(userUId)
            .child(goalID).setValue(goal).addOnSuccessListener {
                binding.inputNewGoal.text.clear()
                Toast.makeText(context, "Edit Successfully!", Toast.LENGTH_SHORT).show()

                replaceFragment(DashBoardFragment())   // Need to change replace dashboard fragment

            }.addOnFailureListener{
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteGoal(goalID : String ,goalName : String ,goalStatus : String, goalTargetDate : String) {

        db.child("Active").child(userUId)
            .child(goalID).removeValue()

        Toast.makeText(context, "Delete Successfully!", Toast.LENGTH_SHORT).show()

        replaceFragment(DashBoardFragment())     // Need to change replace dashboard fragment
    }

    private fun checkExpiredGoals(newGoalTargetDate : String,goalID : String){

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH) + 1
        val day = today.get(Calendar.DAY_OF_MONTH)


        val goalTargetDate = formatter.parse(newGoalTargetDate)
        val todayDate = formatter.parse("$day-$month-$year")
        val cmp = goalTargetDate.compareTo(todayDate)
        if(cmp>0) {
            deleteExpiredGoal(goalID)
        }

//        val getData = db.child("Expired").child(userUId)
//
//        getData.addValueEventListener(object : ValueEventListener {
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if(snapshot.exists()){
//                    userArrayList.clear()
//                    for(goalSnapshot in snapshot.children){
//                        val goals = goalSnapshot.getValue(Goals::class.java)
//                        val goalTargetDate = formatter.parse(newGoalTargetDate)
//                        val todayDate = formatter.parse("$day-$month-$year")
//                        val cmp = goalTargetDate.compareTo(todayDate)
//                        if(cmp>0) {
//                            deleteExpiredGoal(goalID)
//                        }
//                    }
//                }
////                    userRecyclerView.adapter = GoalRecyclerAdapter(userArrayList,this@ActiveGoalsFragment)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//
//        })

    }

    private fun deleteExpiredGoal(goalID : String) {

        db.child("Expired").child(userUId)
            .child(goalID).removeValue()

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null ){

            val fragmentTransaction  = this.parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,fragment)
            fragmentTransaction.commit()
        }
    }


}