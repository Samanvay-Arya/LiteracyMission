package com.samanvay.literacymission.AllMembers.Students.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samanvay.literacymission.AllMembers.Students.Model.studentModel
import com.samanvay.literacymission.R

class adapter(var context:Context,var studentList:ArrayList<studentModel>): RecyclerView.Adapter<adapter.VH>() {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name:TextView=itemView.findViewById(R.id.student_name_card_design);
        val image:ImageView=itemView.findViewById(R.id.studentImage_card_design);
        val phone1:TextView=itemView.findViewById(R.id.student_number1_card_design);
        val phone2:TextView=itemView.findViewById(R.id.student_number2_card_design);
        val assignedVolunteer:TextView=itemView.findViewById(R.id.student_assigned_volunteer_card_design);
        val call:ImageButton=itemView.findViewById(R.id.student_call_card_design);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter.VH {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.student_card_design,parent,false);
        return VH(v)
    }

    override fun onBindViewHolder(holder: adapter.VH, p: Int) {
        holder.name.text=studentList[p].name
        holder.assignedVolunteer.text=studentList[p].assignedVolunteer
        holder.phone1.text=studentList[p].parentNumber
        holder.phone2.text=studentList[p].alternateNumber
        Glide.with(holder.image).load(studentList[p].imageURL).into(holder.image)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

}