package com.example.fypproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fypproject.R
import com.example.fypproject.data.Goals
import com.example.fypproject.fragments.goals.GoalsDetailsFragment
import com.example.fypproject.fragments.goals.dashboard.Communicator
import com.google.firebase.database.ValueEventListener

class GoalRecyclerAdapter(private val goals: ArrayList<Goals>, private val listener: Communicator) :
    RecyclerView.Adapter<GoalRecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener{

        val itemTitle: TextView = itemView.findViewById(R.id.goal_title)
        val itemDate: TextView = itemView.findViewById(R.id.target_date)
        val itemPicture: ImageView = itemView.findViewById(R.id.goal_image_CG)

        init {
//            itemView.setOnClickListener { v: View ->
////                val position: Int = adapterPosition
////                Toast.makeText(itemView.context, "You clicked on item# ${position+1}", Toast.LENGTH_SHORT).show()
//
//                //v.setOnClickListener(this)
//
//                val activity = v!!.context as AppCompatActivity
//                val fragmentDetails = GoalsDetailsFragment()
//                activity.supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, fragmentDetails).addToBackStack(null).commit()
//
//
//            }
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val itemId = goals[adapterPosition].goalID
            val itemTitle = goals[adapterPosition].goalName
            val itemStatus = goals[absoluteAdapterPosition].goalStatus
            val itemDate = goals[adapterPosition].goalTargetDate
            if(position!= RecyclerView.NO_POSITION){
                listener.passData(position,itemId,itemTitle,itemStatus,itemDate)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.goals_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = goals[position]

        holder.itemTitle.text = currentItem.goalName
        holder.itemDate.text = currentItem.goalTargetDate

//        holder.itemTitle.text = titles[position]
//        holder.itemDate.text = scheduleDate[position]
//        holder.itemPicture.setImageResource(images[position])

    }

    override fun getItemCount(): Int {
        return goals.size
//        return titles.size
    }

}


