package com.samanvay.literacymission.AllMembers.Volunteer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.samanvay.literacymission.AllMembers.Students.Adapter.adapter
import com.samanvay.literacymission.AllMembers.Students.Model.studentModel
import com.samanvay.literacymission.AllMembers.Volunteer.Adapter.volunteerAdapter
import com.samanvay.literacymission.AllMembers.Volunteer.Model.volunteerModel
import com.samanvay.literacymission.R
import com.samanvay.literacymission.databinding.ActivityAllStudents2Binding
import com.samanvay.literacymission.databinding.ActivityAllVolunteerBinding

class AllVolunteer : AppCompatActivity() {
    private lateinit var allVolunteer: ActivityAllVolunteerBinding
    private  lateinit var volunteerList:ArrayList<volunteerModel>
    private  lateinit var adapter: volunteerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allVolunteer= ActivityAllVolunteerBinding.inflate(layoutInflater)
        setContentView(allVolunteer.root)
        dataChangedListener()
        allVolunteer.recyclerView.layoutManager= LinearLayoutManager(this)
        volunteerList= arrayListOf()
        adapter= volunteerAdapter(this,volunteerList)
        allVolunteer.recyclerView.adapter=adapter

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun dataChangedListener() {
        val db= FirebaseFirestore.getInstance()
        db.collection("Volunteer").orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    Toast.makeText(this, "ERROR: "+error.message, Toast.LENGTH_SHORT).show()
                }
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type== DocumentChange.Type.ADDED ){
                        volunteerList.add(dc.document.toObject(volunteerModel::class.java))
                    }
                    else if(dc.type==DocumentChange.Type.MODIFIED){
                        finish();
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                }
                adapter.notifyDataSetChanged()

            }
    }
}