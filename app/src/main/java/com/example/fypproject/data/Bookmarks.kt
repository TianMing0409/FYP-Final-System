package com.example.fypproject.data

data class Bookmarks(
    var bookmarkID : String ="",
    var postID:String ="",
    var postUsername:String="",
    var postDate:String ="",
    var postDetails:String="",
    var commentCount: Int=0,
    var imageUrl : String = "",
    var postUserID : String = ""
)
