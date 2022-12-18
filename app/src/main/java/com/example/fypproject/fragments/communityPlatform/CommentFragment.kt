package com.example.fypproject.fragments.communityPlatform

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.data.Comments
import com.example.fypproject.R
import com.example.fypproject.adapter.CommentAdapter
import com.example.fypproject.databinding.FragmentCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class CommentFragment : Fragment() {

    private lateinit var binding : FragmentCommentBinding

    private lateinit var db : DatabaseReference
    private lateinit var db2 : DatabaseReference
    private lateinit var db3 : DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userArrayList : ArrayList<Comments>
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    var inputPos: Int? = null
    var inputPostId: String = ""
    var inputPostUsername: String = ""
    var inputPostDate: String = ""
    var inputPostContent: String = ""
    var inputPostImage : String = ""
    var inputPostCommentCount : Int = 0

    private var commentDetails = ""
    private var dt = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_comment, container, false)
        binding = FragmentCommentBinding.inflate(inflater,container,false)

//        activity?.findViewById<View>(R.id.bottom_navigation)?.isGone = true

//        (TestActivity()).window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) // Maybe will use when combine
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activity?.window?.decorView?.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            val isImeVisible = insetsCompat.isVisible(WindowInsetsCompat.Type.ime())
            // below line, do the necessary stuff:
//            binding.bottom.visibility =  if (isImeVisible) View.GONE else View.VISIBLE
            activity?.findViewById<View>(R.id.bottom_navigation)?.visibility  = if (isImeVisible) View.GONE else View.VISIBLE
            view.onApplyWindowInsets(insets)
        }




        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Posts")

        //fetch data
        inputPos = arguments?.getInt("input_pos")
        inputPostId = arguments?.getString("input_post_id").toString()
        inputPostUsername = arguments?.getString("input_post_username").toString()
        inputPostDate = arguments?.getString("input_post_date").toString()
        inputPostContent = arguments?.getString("input_post_details").toString()
        inputPostImage = arguments?.getString("input_post_image").toString()
        inputPostCommentCount = arguments?.getInt("input_comment_count")!!.toInt()

//        binding.postUsername?.text = inputPostUsername
//        binding.postDate?.text = inputPostDate
//        binding.postContent?.text = inputPostContent
//        if(inputPostImage == ""){
//            binding.postImg.setImageBitmap(null)
//        }else{
//            Picasso.get().load(inputPostImage).into(binding.postImg)
//        }



        userRecyclerView = binding.commentRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<Comments>()

        getCommentsData(inputPostId)

        binding.backBtn.setOnClickListener(){
             this.parentFragmentManager.popBackStack()
        }

        binding.post.setOnClickListener(){

            commentDetails = binding.inputComment.text.toString().trim()
            db3 = FirebaseDatabase.getInstance().getReference("Users")
            db3.child(userUId).get().addOnSuccessListener {
                val username = it.child("username").value.toString()
                if(commentDetails.isEmpty()) {
                    Toast.makeText(context, "Comment cannot be empty...", Toast.LENGTH_SHORT).show()
                }else{
                    addComment(inputPostId, commentDetails, username)
                    updateCommentCount(inputPostId,inputPostCommentCount)
                    updateBookmarkCommentCount(inputPostId,inputPostCommentCount)
                    Toast.makeText(context, "Comment Successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        originalMode?.let { activity?.window?.setSoftInputMode(it) }
//    }

    private fun getCommentsData(postId : String){

        val getData = db.child(postId).child("Comments")

        getData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()
                    for(commentSnapshot in snapshot.children){
                        val comments = commentSnapshot.getValue(Comments::class.java)
                        userArrayList.add(comments!!)
//                        val postId = commentSnapshot.child("postId").value
//                        userArrayList.add(postId)
                    }
                    userRecyclerView.adapter = CommentAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun  updateCommentCount(postId : String, commentCount : Int){
        db.child(postId).child("commentCount").setValue(commentCount+1).addOnSuccessListener {
            binding.inputComment.text.clear()
        }.addOnFailureListener{
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateBookmarkCommentCount(postId : String, commentCount : Int){

        //Update bookmark comment count
        db2 = FirebaseDatabase.getInstance().getReference("Bookmarks")
        db2.child(userUId).child(postId).get().addOnSuccessListener {
            if(it.child("commentCount").exists()) {
                db2.child(userUId).child(postId).child("commentCount").setValue(commentCount + 1)
                    .addOnSuccessListener {
                        binding.inputComment.text.clear()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun addComment(postId : String, commentDetails: String,commenterUsername : String){
        val commentID = "C" + (0..10000).random()

        val c: Calendar = Calendar.getInstance()
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dt = sdf.format(c.time);
        val commentDate = dt

        val comments = Comments(commentID, commenterUsername, commentDate, commentDetails, postId)
        db.child(postId).child("Comments").child(commentID).setValue(comments).addOnSuccessListener {
            binding.inputComment.text.clear()
        }.addOnFailureListener{
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

}

