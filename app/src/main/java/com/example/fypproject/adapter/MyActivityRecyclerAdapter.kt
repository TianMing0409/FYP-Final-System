package com.example.fypproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.data.Bookmarks
import com.example.fypproject.data.Posts
import com.example.fypproject.fragments.communityPlatform.communityDashboard.PassCommData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MyActivityRecyclerAdapter (private val posts: ArrayList<Posts>, private val listener: PassCommData) :
    RecyclerView.Adapter<MyActivityRecyclerAdapter.ViewHolder>(){

    private lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""
    private var isBookmark = false


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val username: TextView = itemView.findViewById(R.id.myUsername)
        val postDate : TextView = itemView.findViewById(R.id.myPostDate)
        val postDetails : TextView = itemView.findViewById(R.id.myPostDetails)
//        val bookmarkIcon : ImageView = itemView.findViewById(R.id.myBookmarkIcon)
        val commentCount : TextView = itemView.findViewById(R.id.myCommentCount)
        val commentIcon : ImageView = itemView.findViewById(R.id.myCommentIcon)
        var postImage: ImageView = itemView.findViewById(R.id.myPostImage)
        val dotSetting : ImageView = itemView.findViewById(R.id.horizontalDotSetting)


        init {
            itemView.setOnClickListener(this)

            dotSetting.setOnClickListener(){
                popupMenus(it)
            }

        }

        override fun onClick(v: View?) {
            commentIcon.setOnClickListener() {

                val position = adapterPosition
                val itemId = posts[adapterPosition].postID
                val itemtUsername = posts[adapterPosition].postUsername
                val itemDate = posts[adapterPosition].postDate
                val itemDetails = posts[adapterPosition].postDetails
                val itemCommentCount = posts[adapterPosition].commentCount
                val itemImage = posts[absoluteAdapterPosition].imageUrl
                val itemUserID = posts[absoluteAdapterPosition].postUserID
                if (position != RecyclerView.NO_POSITION) {
                    listener.passCommData(
                        position,
                        itemId,
                        itemtUsername,
                        itemDate,
                        itemDetails,
                        itemCommentCount,
                        itemImage,
                        itemUserID
                    )
                }
            }
        }

        private fun popupMenus(v:View){

            val popupMenus = PopupMenu(itemView.context,v)
            val postId = posts[adapterPosition].postID
            popupMenus.inflate(R.menu.post_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.deletePost ->{
                        deletePost(postId)
                        Toast.makeText(itemView.context, "Delete post successfully!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java).invoke(menu,true)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.personal_post_view,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = posts[position]
        holder.username.text = currentItem.postUsername
        holder.postDate.text = currentItem.postDate
        holder.postDetails.text = currentItem.postDetails
//        holder.commentCount.text = currentItem.commentCount.toString()
        holder.commentCount.setText(currentItem.commentCount.toString()+" comments")
//        Picasso.get().load(currentItem.imageUrl).into(holder.postImage)
        if(currentItem.imageUrl.toString() == ""){
            holder.postImage.setImageBitmap(null)
            holder.postImage.isGone = true
        }else{
            Picasso.get().load(currentItem.imageUrl).into(holder.postImage)
        }

//        checkIsBookmark(currentItem.postID,holder.bookmarkIcon)  //Bind bookmark icon

//        holder.bookmarkIcon.setOnClickListener(){
//            if(holder.bookmarkIcon.tag.equals("No Bookmark")){
//
//                //Add bookmark
//                db = FirebaseDatabase.getInstance().getReference("Bookmarks")
//
//                val bookmarkId = "B" + (0..9000).random()
//                val bookmark = Bookmarks(bookmarkId,currentItem.postID, currentItem.postUsername,currentItem.postDate,
//                    currentItem.postDetails,currentItem.commentCount,currentItem.imageUrl,currentItem.postUserID)
//
//                db.child(userUId).child(currentItem.postID).setValue(bookmark).addOnSuccessListener {
//                    Toast.makeText(holder.itemView.context, "Added to bookmark!", Toast.LENGTH_SHORT).show()
//                }.addOnFailureListener{
//
//                }
//            }else{
//
//                //Remove Bookmark
//                db = FirebaseDatabase.getInstance().getReference("Bookmarks")
//
//                db.child(userUId)
//                    .child(currentItem.postID).removeValue().addOnSuccessListener {
//
//                        Toast.makeText(holder.itemView.context, "Removed from bookmark!", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener{
//
//                    }
//            }
//        }


    }

    override fun getItemCount(): Int {
        return posts.size
    }

    private fun deletePost(postId : String){
        db = FirebaseDatabase.getInstance().getReference("Posts")

        db.child(postId).removeValue()

    }

//    private fun checkIsBookmark(postId : String, bookmarkIcon : ImageView){
//        db = FirebaseDatabase.getInstance().getReference("Bookmarks")
//
//        db.child(userUId).child(postId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                isBookmark = snapshot.exists()
//                if(isBookmark){
//                    bookmarkIcon.setImageResource(R.drawable.bookmarked_24)
//                    bookmarkIcon.setTag("Bookmarked")
//                }else{
//                    bookmarkIcon.setImageResource(R.drawable.bookmark_border_24)
//                    bookmarkIcon.setTag("No Bookmark")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError){
//
//            }
//
//        })
//    }


}