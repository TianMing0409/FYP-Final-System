package com.example.fypproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fypproject.data.EmojiData
import com.example.fypproject.data.UserData
import com.example.moodmonitoringapp.data.CheckInNo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel(){
    val isRegistered = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    var username = ""
    var email = ""
    var password = ""
    var phoneNumber = ""
    var imageUrl = ""

    var veryHappy = 0
    var happy = 0
    var normal = 0
    var sad = 0
    var verySad = 0
    var checkIn = 0

    fun register(mAuth: FirebaseAuth) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = UserData(username,phoneNumber, email,password,imageUrl)
                val mood = EmojiData(veryHappy, happy, normal, sad, verySad)
                val checkInNo = CheckInNo(checkIn)
                FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(user)
                    .addOnCompleteListener { task ->
                        isRegistered.value = task.isSuccessful
                        mAuth.currentUser!!.sendEmailVerification()
                    }
                FirebaseDatabase.getInstance().getReference("Stats")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).child("TotalMoods")
                    .setValue(mood)
                    .addOnCompleteListener { task ->
                        isRegistered.value = task.isSuccessful
                        mAuth.currentUser!!.sendEmailVerification()
                    }
                FirebaseDatabase.getInstance().getReference("Check-In")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(checkInNo)
                    .addOnCompleteListener { task ->
                        isRegistered.value = task.isSuccessful
                        mAuth.currentUser!!.sendEmailVerification()
                    }
            } else {
                isRegistered.value = false
                error.value = it.exception!!.message.toString()
            }
        }
    }
}