package com.example.fypproject.fragments.communityPlatform

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fypproject.R
import com.example.fypproject.adapter.PostRecyclerAdapter
import com.example.fypproject.data.Comments
import com.example.fypproject.data.Goals
import com.example.fypproject.data.Posts
import com.example.fypproject.databinding.FragmentCommunityBinding
import com.example.fypproject.databinding.FragmentCreatePostBinding
import com.example.fypproject.fragments.communityPlatform.communityDashboard.CommunityDashboardFragment
import com.example.fypproject.fragments.goals.dashboard.DashBoardFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class CreatePostFragment : Fragment() {

    private lateinit var binding : FragmentCreatePostBinding

    private lateinit var db : DatabaseReference
    private lateinit var db2: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private var userUId = FirebaseAuth.getInstance().currentUser!!.uid
    var tempUId = ""

    //uri of picked image
    private var imageUri: Uri? = null
    private val TAG ="POSTIMAGE_ADD_TAG"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreatePostBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        tempUId = auth.uid.toString()
        //userUId = tempUId              //Need to uncomment this in real work, because this is to get that signed in user id
        db = FirebaseDatabase.getInstance().getReference("Posts")

        binding.uploadPhoto.setOnClickListener(){
            selectImage()
        }

        binding.shareBtn.setOnClickListener(){v:View ->
            validateData()
            //Toast.makeText(context, "Share Successfully", Toast.LENGTH_SHORT).show()
        }


        return binding.root

    }

    private var postID=""
    private var postUsername=""
    private var postDate = ""
    private var postDetails = ""
    private var likeCount= 0
    private var commentCount = 0
    private var dt = ""
    private var imageUrl = ""

    private fun validateData(){

        postDetails = binding.inputPost.text.toString().trim()

        db2 = FirebaseDatabase.getInstance().getReference("Users")
        db2.child(userUId).get().addOnSuccessListener {
            val username = it.child("username").value.toString()

            if (postDetails.isEmpty()) {
                Toast.makeText(context, "Enter post details...", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUri == null) {
                    uploadPostWithoutImage(postDetails, username, userUId)
                } else {
                    uploadPost(postDetails, username, userUId)
                }
                Toast.makeText(context, "Share Successfully!", Toast.LENGTH_SHORT).show()
                replaceFragment(CommunityDashboardFragment())   // Need to change replace community dashboard fragment
            }
        }
    }

    //Upload to real time database with photo
    private fun uploadPostIntoDb(postID: String, postUsername: String ,postDate:String, postDetails : String, likeCount: Int,
                           commentCount: Int, imageUrl : String,userID : String) {

        val post = Posts(postID,postUsername ,postDate, postDetails,commentCount,imageUrl,userID)

        db.child(postID).setValue(post).addOnSuccessListener {
                binding.inputPost.text.clear()
            }.addOnFailureListener{
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    //Upload post image to firebase storage
    private fun uploadPost(postDetails : String,username : String, userID : String) {

        val c: Calendar = Calendar.getInstance()
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dt = sdf.format(c.time);

        val postID = "P" + (0..9000).random()
        val postDate = dt
        val likeCount = 0
        val commentCount = 0

//        val timestamp = System.currentTimeMillis()
        val filePathAndName = "community/$postID"

        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedPostImageUrl = "${uriTask.result}"
                uploadPostIntoDb(postID,username,postDate,postDetails,likeCount, commentCount ,uploadedPostImageUrl,userID)   //Upload to real time database
                //uploadCampaignInfoToDb(uploadedCampaignUrl, postID.toString())
                //Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e ->
                Toast.makeText(context,"Failed to upload ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadPostWithoutImage(postDetails : String, username: String, userID : String){
        val c: Calendar = Calendar.getInstance()
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dt = sdf.format(c.time);

        val postID = "P" + (0..9000).random()
        val postDate = dt
        val likeCount = 0
        val commentCount = 0
        val imageUrl = ""

        val post = Posts(postID,username ,postDate, postDetails,commentCount,imageUrl,userID)

        db.child(postID).setValue(post).addOnSuccessListener {
            binding.inputPost.text.clear()
        }.addOnFailureListener{
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    //Select an image
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        imageResultLauncher.launch(intent)
    }

    //Display selected image
    private val imageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{ result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                Log.d(TAG, "Picture Picked")
                imageUri = result.data!!.data
                binding.uploadPhoto.setImageURI(imageUri)

            }
            else{
                Log.d(TAG,"Pick Cancelled")
                Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null ){

            val fragmentTransaction  = this.parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,fragment)
            fragmentTransaction.commit()
        }
    }

}