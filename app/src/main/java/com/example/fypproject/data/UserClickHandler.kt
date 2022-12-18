package com.example.fypproject.data

import com.example.fypproject.data.UserData


interface Communicator {
    fun passData(position: Int,
                 username : String,
                 phoneNumber: String,
                 email: String ,
                 password: String)
}