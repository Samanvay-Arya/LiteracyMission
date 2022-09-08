package com.samanvay.literacymission.Registration

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.samanvay.literacymission.LoadingDialog.loadingDialog
import com.samanvay.literacymission.R
import com.samanvay.literacymission.databinding.ActivityStudentRegistrationBinding
import com.samanvay.literacymission.databinding.ActivityVolunteerRegistration1Binding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StudentRegistration : AppCompatActivity() {
    private var medium:ArrayList<String> =ArrayList()
    private var sClass:ArrayList<String> =ArrayList()
    private lateinit var nameS:String
    private lateinit var classS:String
    private lateinit var mediumS:String
    private lateinit var parentNameS:String
    private lateinit var parentNumberS:String
    private lateinit var tempAddressS:String
    private lateinit var perAddressS:String
    private lateinit var assignedVolunteerS:String
    private lateinit var phone2S:String
    var imageUri: Uri? = null
    lateinit var imageURL:String
    var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    var progressDialog= loadingDialog(this)
    var iImage=0

    private lateinit var binding: ActivityStudentRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStudentRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillClassArray()
        setClassSpinner()
        fillMediumArray()
        setMediumSpinner()
        binding.selectImageStudentRegistration.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }
        binding.Save.setOnClickListener {

            extractStringFromEV()
            when {
                iImage==0 -> Toast.makeText(this,"Image is not selected",Toast.LENGTH_SHORT).show()
                nameS.isEmpty() -> binding.studentName.error = "Required"
                parentNameS.isEmpty() -> binding.studentParentName.error="Required"
                parentNumberS.isEmpty() -> binding.parentNumber.error="Required"
                tempAddressS.isEmpty() -> binding.temporaryAddress.error="Required"
                perAddressS.isEmpty() -> binding.permanentAddress.error="Required"
                assignedVolunteerS.isEmpty()-> binding.assignedVolunteer.error="Required"
                else -> {
                    progressDialog.startLoadingDialog()
                    uploadImageToFirebase()
                }
            }
        }
        binding.back.setOnClickListener{
            finish()
        }
    }
    private fun extractStringFromEV() {
        nameS=binding.studentName.text.toString()
        parentNameS=binding.studentParentName.text.toString()
        parentNumberS=binding.parentNumber.text.toString()
        tempAddressS=binding.temporaryAddress.text.toString()
        perAddressS=binding.permanentAddress.text.toString()
        assignedVolunteerS=binding.assignedVolunteer.text.toString()
        phone2S=binding.parentNumber2.text.toString()
    }
    private fun uploadImageToFirebase() {
        if (imageUri != null) {
            val storageReference1 = storageReference.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)
            )
            storageReference1.putFile(imageUri!!).addOnSuccessListener {
                storageReference1.downloadUrl.addOnSuccessListener { uri ->
                    imageURL = uri.toString()
                    sendDataToFireStore()
                }
            }.addOnProgressListener { }.addOnFailureListener { }
        } else {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_LONG).show()
        }
    }
    private fun sendDataToFireStore() {
        val dateCode = getDateTime()
        val store: FirebaseFirestore = FirebaseFirestore.getInstance()
        val documentReference: DocumentReference = store.collection("Student").document(nameS.substring(0,2)+dateCode.toString())
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = nameS
        user["standard"]=classS
        user["tempAddress"]=tempAddressS
        user["perAddress"]=perAddressS
        user["medium"]=mediumS
        user["parentName"]=parentNameS
        user["parentNumber"]=parentNumberS
        user["imageURL"]=imageURL
        user["assignedVolunteer"]=assignedVolunteerS
        user["alternateNumber"]=phone2S

        documentReference
            .set(user).addOnSuccessListener(OnSuccessListener<Void> {
                progressDialog.dismissDialog()
                finish()
            })
    }


    private fun getFileExtension(uri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            binding.studentImage.setImageURI(imageUri)
            iImage = 1
        }
    }
    private fun getDateTime(): String? {
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat =
            SimpleDateFormat("yyyyMMddHHmmss")
        val date = Date()
        return dateFormat.format(date)
    }
    private fun setMediumSpinner() {
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, medium)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.studentMedium.adapter = branchAdapter
        binding.studentMedium.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                mediumS = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun setClassSpinner() {
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, sClass)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.studentClass.adapter = branchAdapter
        binding.studentClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                classS= parent.getItemAtPosition(position).toString()
                Toast.makeText(this@StudentRegistration,"class is initialized",Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun fillMediumArray() {
        medium.add("Hindi")
        medium.add("English")
    }

    private fun fillClassArray() {
        sClass.add("Nursery")
        sClass.add("Class 1")
        sClass.add("Class 2")
        sClass.add("Class 3")
        sClass.add("Class 4")
        sClass.add("Class 5")
        sClass.add("Class 6")
        sClass.add("Class 7")
        sClass.add("Class 8")
        sClass.add("Class 9")
        sClass.add("Class 10")
        sClass.add("Class 11")
        sClass.add("Class 12")
    }
}