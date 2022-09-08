package com.samanvay.literacymission.AllMembers.Volunteer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samanvay.literacymission.AllMembers.Volunteer.Model.volunteerModel
import com.samanvay.literacymission.R
import org.w3c.dom.Text

class volunteerAdapter(var context: Context, var volunteerList:ArrayList<volunteerModel>):RecyclerView.Adapter<volunteerAdapter.VH>() {
    class VH(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val name:TextView=itemView.findViewById(R.id.volunteer_name_card_design)
        val phone1:TextView=itemView.findViewById(R.id.volunteer_phone1_card_design)
        val phone2:TextView=itemView.findViewById(R.id.volunteer_phone2_card_design)
        val passingYear:TextView=itemView.findViewById(R.id.volunteer_passout_year_card_design)
        val image:ImageView=itemView.findViewById(R.id.volunteer_image_card_design)
        val call:ImageButton=itemView.findViewById(R.id.volunteer_call_ImageButton_card_design)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v:View=LayoutInflater.from(parent.context).inflate(R.layout.volunteer_card_design,parent,false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, p: Int) {
         holder.name.text=volunteerList[p].name
        holder.phone1.text=volunteerList[p].contact
        holder.phone2.text=volunteerList[p].contact
        holder.passingYear.text=volunteerList[p].passingYear
        Glide.with(holder.image).load(volunteerList[p].imageURL).into(holder.image)
    }

    override fun getItemCount(): Int {
        return volunteerList.size
    }
}
