package com.example.fypproject.adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.data.Bookmarks
import com.example.fypproject.data.Goals
import com.example.fypproject.data.Posts
import com.example.fypproject.fragments.communityPlatform.CommentFragment
import com.example.fypproject.fragments.communityPlatform.communityDashboard.PassCommData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class PostRecyclerAdapter (private val posts: ArrayList<Posts>,private val listener: PassCommData) :
    RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>(){

    private lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
//    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""
    private var isBookmark = false
//    private lateinit var bookmarkArrayList : ArrayList<Bookmarks>


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val username: TextView = itemView.findViewById(R.id.userName)
        val postDate : TextView = itemView.findViewById(R.id.postDate)
        val postDetails : TextView = itemView.findViewById(R.id.postDetails)
        val bookmarkIcon : ImageView = itemView.findViewById(R.id.bookmarkIcon)
        val commentCount : TextView = itemView.findViewById(R.id.commentCount)
        val commentIcon : ImageView = itemView.findViewById(R.id.commentIcon)
        var postImage:ImageView = itemView.findViewById(R.id.postImage)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            commentIcon.setOnClickListener() {

                val position = adapterPosition
                val itemId = posts[adapterPosition].postID
                val itemUsername = posts[adapterPosition].postUsername
                val itemDate = posts[adapterPosition].postDate
                val itemDetails = posts[adapterPosition].postDetails
                val itemCommentCount = posts[adapterPosition].commentCount
                val itemImage = posts[absoluteAdapterPosition].imageUrl
                val itemUserID = posts[absoluteAdapterPosition].postUserID
                if (position != RecyclerView.NO_POSITION) {
                    listener.passCommData(
                        position,
                        itemId,
                        itemUsername,
                        itemDate,
                        itemDetails,
                        itemCommentCount,
                        itemImage,
                        itemUserID
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.public_post_view,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        val currentItem = posts[position]
        holder.username.text = currentItem.postUsername
        holder.postDate.text = currentItem.postDate
        holder.postDetails.text = currentItem.postDetails
        holder.commentCount.setText(currentItem.commentCount.toString()+" comments")

        if(currentItem.imageUrl.toString() == ""){
            holder.postImage.setImageBitmap(null)
            holder.postImage.isGone = true
        }else{
            Picasso.get().load(currentItem.imageUrl).into(holder.postImage)
        }

        checkIsBookmark(currentItem.postID,holder.bookmarkIcon)  //Bind bookmark icon

        holder.bookmarkIcon.setOnClickListener(){
            if(holder.bookmarkIcon.tag.equals("No Bookmark")){

                //Add bookmark
                db = FirebaseDatabase.getInstance().getReference("Bookmarks")

                val bookmarkId = "B" + (0..9000).random()
                val bookmark = Bookmarks(bookmarkId,currentItem.postID, currentItem.postUsername,currentItem.postDate,
                    currentItem.postDetails,currentItem.commentCount,currentItem.imageUrl,currentItem.postUserID)

                db.child(userUId).child(currentItem.postID).setValue(bookmark).addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Added to bookmark!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{

                }
            }else{

                //Remove Bookmark
                db = FirebaseDatabase.getInstance().getReference("Bookmarks")

                db.child(userUId)
                    .child(currentItem.postID).removeValue().addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Removed from bookmark!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{

                    }
            }
        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    private fun checkIsBookmark(postId : String, bookmarkIcon : ImageView){

        val userUId = FirebaseAuth.getInstance().currentUser!!.uid

        db = FirebaseDatabase.getInstance().getReference("Bookmarks")

        db.child(userUId).child(postId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isBookmark = snapshot.exists()
                if(isBookmark){
                    bookmarkIcon.setImageResource(R.drawable.bookmarked_24)
                    bookmarkIcon.setTag("Bookmarked")
                }else{
                    bookmarkIcon.setImageResource(R.drawable.bookmark_border_24)
                    bookmarkIcon.setTag("No Bookmark")
                }
            }

            override fun onCancelled(error: DatabaseError){

            }

        })
    }


}