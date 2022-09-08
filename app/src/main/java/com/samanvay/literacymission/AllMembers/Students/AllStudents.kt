package com.samanvay.literacymission.AllMembers.Students

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.samanvay.literacymission.AllMembers.Students.Adapter.adapter
import com.samanvay.literacymission.AllMembers.Students.Model.studentModel
import com.samanvay.literacymission.AllMembers.Volunteer.Model.volunteerModel
import com.samanvay.literacymission.R
import com.samanvay.literacymission.databinding.ActivityAllStudents2Binding
import com.samanvay.literacymission.databinding.ActivityMainBinding

class AllStudents : AppCompatActivity() {
    private lateinit var allStudent: ActivityAllStudents2Binding
    private  lateinit var studentList:ArrayList<studentModel>
    private  lateinit var adapter:adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allStudent= ActivityAllStudents2Binding.inflate(layoutInflater)
        setContentView(allStudent.root)
        dataChangedListener()
        allStudent.recyclerView.layoutManager=LinearLayoutManager(this)
        studentList= arrayListOf()
        adapter= adapter(this,studentList)
        allStudent.recyclerView.adapter=adapter




    }

    @SuppressLint("NotifyDataSetChanged")
    private fun dataChangedListener() {

        val db=FirebaseFirestore.getInstance()
        db.collection("Student").orderBy("name",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    Toast.makeText(this, "ERROR: "+error.message, Toast.LENGTH_SHORT).show()
                }
                for(dc:DocumentChange in value?.documentChanges!!){
                    if(dc.type==DocumentChange.Type.ADDED){
                        studentList.add(dc.document.toObject(studentModel::class.java))
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