package com.example.fypproject.fragments.communityPlatform.communityDashboard

interface PassCommData {
    fun passCommData(position: Int,
                     postID : String,
                     postUsername: String,
                     postDate: String ,
                     postDetails: String,
                     commentCount:Int,
                     imageUrl: String,
                     postUserID : String)

}