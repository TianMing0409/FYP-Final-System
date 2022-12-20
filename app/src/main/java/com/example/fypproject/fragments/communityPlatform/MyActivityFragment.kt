package com.example.fypproject.fragments.communityPlatform

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.adapter.MyActivityRecyclerAdapter
import com.example.fypproject.adapter.PostRecyclerAdapter
import com.example.fypproject.data.Posts
import com.example.fypproject.databinding.FragmentCreatePostBinding
import com.example.fypproject.databinding.FragmentMyActivityBinding
import com.example.fypproject.fragments.communityPlatform.communityDashboard.PassCommData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class MyActivityFragment : Fragment(), PassCommData {

    private lateinit var binding : FragmentMyActivityBinding

    private lateinit var db : DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userArrayList : ArrayList<Posts>
    private lateinit var auth : FirebaseAuth
//    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyActivityBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Posts")

        userRecyclerView = binding.myPostRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<Posts>()

        myPostsData()

//        postToList()


//        binding.myPostRecyclerView.layoutManager = LinearLayoutManager(context)
//        binding.myPostRecyclerView.adapter = PostRecyclerAdapter(usernameList, postDateList, postDetailsList, likeCountList,
//            commentCountList, profileImageList)


        return binding.root
    }

    private fun myPostsData(){

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val getData = db

        getData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    userArrayList.clear()
                    for (postSnapshot in snapshot.children) {
                        val posts = postSnapshot.getValue(Posts::class.java)
                        if(posts!!.postUserID == userUId){
                            userArrayList.add(posts!!)
                        }
                    }
                    userRecyclerView.adapter = MyActivityRecyclerAdapter(userArrayList, this@MyActivityFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun passCommData(position: Int, postID: String, postUsername: String, postDate: String, postDetails: String
                              , commentCount: Int, postImage : String,postUserID : String)
    {
        val bundle = Bundle()
        bundle.putInt("input_pos", position)
        bundle.putString("input_post_id",postID)
        bundle.putString("input_post_username", postUsername)
        bundle.putString("input_post_date",postDate)
        bundle.putString("input_post_details", postDetails)
        bundle.putInt("input_comment_count",commentCount)
        bundle.putString("input_post_image",postImage)
        bundle.putString("input_post_userID",postUserID)

        val transaction = this.parentFragmentManager.beginTransaction()
        val commentFragment =CommentFragment()
        commentFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, commentFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}