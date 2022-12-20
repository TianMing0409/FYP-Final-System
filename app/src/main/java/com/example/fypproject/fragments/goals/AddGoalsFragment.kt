package com.example.fypproject.fragments.goals

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.text.set
import androidx.core.view.get
import com.example.fypproject.R
import com.example.fypproject.data.Goals
import com.example.fypproject.databinding.FragmentActiveGoalsBinding
import com.example.fypproject.databinding.FragmentAddGoalsBinding
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddGoalsFragment : Fragment() {

    private lateinit var binding : FragmentAddGoalsBinding

    private lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
//    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    var recomm: String = ""
    var recomm_day: Int? = null
    var recomm_month: Int? = null
    var recomm_year: Int? = null

    val myCalendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val view =inflater.inflate(R.layout.fragment_add_goals,container,false)
        binding = FragmentAddGoalsBinding.inflate(inflater,container,false)

        binding.inputGoal.setText("")   //Original empty goal

        //Fetch data from recommendation
        recomm = arguments?.getString("recomm_goal").toString()
        recomm_day = arguments?.getInt("recomm_day")
        recomm_month = arguments?.getInt("recomm_month")
        recomm_year = arguments?.getInt("recomm_year")

        binding.inputGoal.setText(recomm).toString().trim()


        if(recomm == "null"){
            binding.inputGoal.setText("")
        }

        //For calender mode date picker to retrieve recommendation date
        if(recomm_day != null && recomm_month != null && recomm_year != null){
//            val dat = (recomm_day.toString() + "-" + recomm_month.toString() + "-" + recomm_year.toString())
//            binding.inputDate.text = dat

            val year = recomm_year!!.toInt()
            val month = recomm_month!!.toInt() - 1
            val day = recomm_day!!.toInt()

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this.requireActivity(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (recomm_day.toString() + "-" + recomm_month.toString() + "-" + recomm_year.toString())
                    binding.inputDate.text = dat
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

        //Spinner Date picker (Original)
//        val picker = binding.datePicker
//        val today = Calendar.getInstance()
//
//        picker.minDate = System.currentTimeMillis() - 1000
//
//        picker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) {
//                view, year, month, day ->
//            val month = month + 1
//        }
//
//        if(recomm_day != null && recomm_month != null && recomm_year != null){
//
//            picker.init(recomm_year!!.toInt(),recomm_month!!.toInt(),recomm_day!!.toInt()) {
//                    view, year, month, day ->
//                val month = month + 1
//            }
//        }


        //Calendar mode date picker
        binding.inputDate.setOnClickListener(){
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
                    binding.inputDate.text = dat
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


        val btnSubmit: Button = binding.submitBtn

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Goals")

        btnSubmit.setOnClickListener(){
//            Toast.makeText(context, "Submit Successfully!", Toast.LENGTH_SHORT).show()
            validateData()
        }

        return binding.root

    }

//    private var goalID=""
    private var goalName=""
//    private var day = 0
//    private var month = 0
//    private var year = 0
    private var goalTargetDate = ""
//    private var goalStatus = ""

    private fun validateData(){
//        goalID = "G" + (0..3000).random()
        goalName = binding.inputGoal.text.toString().trim()
//        day = .binding.datePicker.dayOfMonth
//        month = binding.datePicker.month +1
//        year = binding.datePicker.year
//        goalTargetDate = "$day-$month-$year"
//        goalStatus = "Active"
        goalTargetDate = binding.inputDate.text.toString()

        if(goalName.isEmpty()){
            Toast.makeText(context, "Enter goal...", Toast.LENGTH_SHORT).show()
        }
        else{
            //Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
            uploadGoal(goalName,goalTargetDate)
            Toast.makeText(context, "Submit Successfully!", Toast.LENGTH_SHORT).show()

            replaceFragment(DashBoardFragment())   // Need to change replace dashboard fragment
        }
    }

    private fun uploadGoal(goalName : String , goalTargetDate : String) {

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val goalID = "G" + (0..3000).random()
        val goalStatus = "Active"
        val goal = Goals(goalID, goalName,goalStatus, goalTargetDate)

        db.child("Active").child(userUId)
            .child(goalID).setValue(goal).addOnSuccessListener {
                binding.inputGoal.text.clear()
                //binding.inputDate.text.clear()

            }.addOnFailureListener{
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null ){

            val fragmentTransaction  = this.parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }



}