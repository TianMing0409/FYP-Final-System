package com.example.fypproject.fragments.communityPlatform

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.adapter.BookmarkAdapter
import com.example.fypproject.adapter.MyActivityRecyclerAdapter
import com.example.fypproject.adapter.PostRecyclerAdapter
import com.example.fypproject.data.Bookmarks
import com.example.fypproject.data.Posts
import com.example.fypproject.databinding.FragmentMyActivityBinding
import com.example.fypproject.databinding.FragmentMyBookmarkBinding
import com.example.fypproject.fragments.communityPlatform.communityDashboard.PassCommData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MyBookmarkFragment : Fragment(R.layout.fragment_my_bookmark), PassCommData {

    private lateinit var binding : FragmentMyBookmarkBinding

    private lateinit var db : DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
//    private lateinit var bookmarkArrayList : ArrayList<Bookmarks>
    private lateinit var bookmarkArrayList : ArrayList<Bookmarks>
    private lateinit var auth : FirebaseAuth
//    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_bookmark, container, false)
        binding = FragmentMyBookmarkBinding.inflate(inflater,container,false)

//        val userUId = FirebaseAuth.getInstance().currentUser!!.uid
//        Log.v(ContentValues.TAG, "The user ID is : $userUId")

//        auth = FirebaseAuth.getInstance()
//        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Bookmarks")

        userRecyclerView = binding.bookmarkRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        bookmarkArrayList = arrayListOf<Bookmarks>()

        getMyBookmarkData()

        return binding.root
    }

    private fun getMyBookmarkData(){

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val getData = db.child(userUId)

        getData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    bookmarkArrayList.clear()
                    for(bookmarkSnapshot in snapshot.children){
                        val bookmarks = bookmarkSnapshot.getValue(Bookmarks::class.java)
                        bookmarkArrayList.add(bookmarks!!)
                    }
                    userRecyclerView.adapter = BookmarkAdapter(bookmarkArrayList,this@MyBookmarkFragment)
                    BookmarkAdapter(bookmarkArrayList,this@MyBookmarkFragment).notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    override fun passCommData(position: Int, postID: String, postUsername: String, postDate: String, postDetails: String
                              , commentCount: Int, imageUrl : String, postUserID : String)
    {
        val bundle = Bundle()
        bundle.putInt("input_pos", position)
        bundle.putString("input_post_id",postID)
        bundle.putString("input_post_username", postUsername)
        bundle.putString("input_post_date",postDate)
        bundle.putString("input_post_details", postDetails)
        bundle.putInt("input_comment_count",commentCount)
        bundle.putString("input_post_image",imageUrl)
        bundle.putString("input_post_userID",postUserID)

        val transaction = this.parentFragmentManager.beginTransaction()
        val commentFragment =CommentFragment()
        commentFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, commentFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

}