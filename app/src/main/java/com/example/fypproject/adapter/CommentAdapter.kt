package com.example.fypproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.data.Comments
import com.example.fypproject.data.Goals
import com.example.fypproject.fragments.goals.dashboard.Communicator

class CommentAdapter (private val comments: ArrayList<Comments>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUsername : TextView = itemView.findViewById(R.id.comment_username)
        val commentDate : TextView = itemView.findViewById(R.id.comment_date)
        val commentDetails : TextView = itemView.findViewById(R.id.comment_content)

        init{
            itemView.setOnClickListener{}
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val currentItem = comments[position]

        holder.commentUsername.text = currentItem.commentUsername
        holder.commentDate.text = currentItem.commentDate
        holder.commentDetails.text = currentItem.commentContent
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}