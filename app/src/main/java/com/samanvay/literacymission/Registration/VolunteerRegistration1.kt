package com.samanvay.literacymission.Registration

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.samanvay.literacymission.LoadingDialog.loadingDialog
import com.samanvay.literacymission.databinding.ActivityVolunteerRegistration1Binding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class VolunteerRegistration1 : AppCompatActivity() {
    private var branch:ArrayList<String> =ArrayList()
    private var home:ArrayList<String> =ArrayList()
    private var preferredMedium:ArrayList<String> =ArrayList()
    private var preferredClass:ArrayList<String> =ArrayList()
    private var year:ArrayList<String> =ArrayList()
    private var passingYear:ArrayList<String> =ArrayList()
    var imageUri: Uri? = null
    lateinit var imageURL:String
    var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var nameS:String
    lateinit var branchS:String
    lateinit var contactS:String
    lateinit var homeStateS:String
    lateinit var emailS:String
    lateinit var preferredClassS:String
    lateinit var preferredMediumS:String
    lateinit var passingYearS:String
    lateinit var yearS:String
    var progressDialog=loadingDialog(this)
    var iImage=0
    private lateinit var binding:ActivityVolunteerRegistration1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityVolunteerRegistration1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fillBranchArray()
        setBranchSpinner()
        fillHomeArray()
        setHomeSpinner()
        fillPreferredClassArray()
        setPreferredClassSpinner()
        fillPreferredMediumArray()
        setPreferredMediumSpinner()
        fillYearArray()
        setYearSpinner()
        fillPassingYearArray()
        setPassingYearSpinner()
        binding.selectImage.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }
        binding.Save.setOnClickListener {
            extractStringFromEV()
            when {
                iImage==0 -> Toast.makeText(this,"Image is not selected",Toast.LENGTH_SHORT).show()
                nameS.isEmpty() -> binding.name.error = "Required"
                contactS.isEmpty() -> binding.number.error="Required"
                emailS.isEmpty() -> binding.email.error="Required"
                else -> {
                    progressDialog.startLoadingDialog()
                    uploadImageToFirebase()
                }
            }
        }
        binding.back.setOnClickListener {
            finish()
        }
        
    }

    private fun extractStringFromEV() {
        nameS=binding.name.text.toString()
        contactS=binding.number.text.toString()
        emailS=binding.number.text.toString()
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
        val documentReference: DocumentReference = store.collection("Volunteer").document(nameS.substring(0,2)+dateCode.toString())
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = nameS
        user["branch"]=branchS
        user["homeState"]=homeStateS
        user["email"]=emailS
        user["preferredClass"]=preferredClassS
        user["year"]=yearS
        user["preferred medium"]=preferredMediumS
        user["contact"]=contactS
        user["imageURL"]=imageURL
        user["passingYear"]=passingYearS

        documentReference
            .set(user).addOnSuccessListener(OnSuccessListener<Void> {
                progressDialog.dismissDialog()
                Toast.makeText(this,"student registered successfully!!",Toast.LENGTH_SHORT).show()
                finish()
            })
    }


    private fun getFileExtension(uri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            data.data.also { imageUri = it }
            binding.image.setImageURI(imageUri)
            iImage = 1
        }
    }


    private fun fillYearArray(){
        year.add("First")
        year.add("Second")
        year.add("Pre-Final")
        year.add("Final")
        year.add("Super-Final")

    }
    private fun getDateTime(): String? {
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat =
            SimpleDateFormat("yyyyMMddHHmmss")
        val date = Date()
        return dateFormat.format(date)
    }

    private fun getDate(): String? {
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat = SimpleDateFormat("dd")
        val date = Date()
        return dateFormat.format(date)
    }
    private fun fillPassingYearArray(){
        for(i in 2015..2030){
            passingYear.add(i.toString())
        }
    }
    private fun fillBranchArray(){
        branch.add("Computer Science and Engineering")
        branch.add("Computer Science and Engineering(Dual)")
        branch.add("Civil Engineering")
        branch.add("Chemical Engineering")
        branch.add("Electronics & Communication Engineering")
        branch.add("Electronics & Communication Engineering(Dual)")
        branch.add("Electrical Engineering")
        branch.add("Mechanical Engineering")
        branch.add("Material Science & Engineering")
        branch.add("Chemistry")
        branch.add("Mathematics and Computing")
        branch.add("Physics & Photonics Science")
        branch.add("Engineering Physics")
        branch.add("Architecture")
        branch.add("Humanities & Social Sciences")
        branch.add("Management Studies")
    }

    private fun fillPreferredMediumArray() {
        preferredMedium.add("Hindi")
        preferredMedium.add("English")
    }

    private fun fillPreferredClassArray() {
        preferredClass.add("Nursery")
        preferredClass.add("class 1")
        preferredClass.add("class 2")
        preferredClass.add("class 3")
        preferredClass.add("class 4")
        preferredClass.add("class 5")
        preferredClass.add("class 6")
        preferredClass.add("class 7")
        preferredClass.add("class 8")
        preferredClass.add("class 9")
        preferredClass.add("class 10")
        preferredClass.add("class 11")
        preferredClass.add("class 12")


    }

    private fun fillHomeArray() {
        home.add("Andaman and Nicobar (UT)")
        home.add("Andhra Pradesh")
        home.add("Arunachal Pradesh")
        home.add("Assam")
        home.add("Bihar")
        home.add("Chandigarh (UT)")
        home.add("Chandigarh (UT)")
        home.add("Dadra and Nagar Haveli (UT)")
        home.add("Daman and Diu (UT)")
        home.add("Delhi")
        home.add("Goa")
        home.add("Gujarat")
        home.add("Haryana")
        home.add("Himachal Pradesh")
        home.add("Jammu and Kashmir")
        home.add("Jharkhand")
        home.add("Karnataka")
        home.add("Kerala")
        home.add("Lakshadweep (UT)")
        home.add("Madhya Pradesh")
        home.add("Maharashtra")
        home.add("Manipur")
        home.add("Meghalaya")
        home.add("Mizoram")
        home.add("Nagaland")
        home.add("Orissa")
        home.add("Puducherry (UT)")
        home.add("Punjab")
        home.add("Rajasthan")
        home.add("Sikkim")
        home.add("Tamil Nadu")
        home.add("Telangana")
        home.add("Tripura")
        home.add("Uttar Pradesh")
        home.add("Uttarakhand")
        home.add("West Bengal")

    }
    private fun setYearSpinner(){
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, year)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.year.adapter = branchAdapter
        binding.year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                yearS = parent.getItemAtPosition(position).toString()
//                iFSYear = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                iFSYear = 0
            }
        }

    }
    private fun setPassingYearSpinner(){
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, passingYear)
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.volunteerPassingYear.adapter = adapter
        binding.volunteerPassingYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                passingYearS = parent.getItemAtPosition(position).toString()
//                iFSYear = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                iFSYear = 0
            }
        }

    }



    private fun setPreferredMediumSpinner() {
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, preferredMedium)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.preferredMedium.adapter = branchAdapter
        binding.preferredMedium.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                preferredMediumS = parent.getItemAtPosition(position).toString()
//                iFSYear = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                iFSYear = 0
            }
        }
    }

    private fun setPreferredClassSpinner() {
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, preferredClass)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.preferredClass.adapter = branchAdapter
        binding.preferredClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                preferredClassS = parent.getItemAtPosition(position).toString()
//                iFSYear = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                iFSYear = 0
            }
        }
    }

    private fun setHomeSpinner() {
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, home)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.homeState.adapter = branchAdapter
        binding.homeState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                homeStateS = parent.getItemAtPosition(position).toString()
//                iFSYear = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                iFSYear = 0
            }
        }
    }

    private fun setBranchSpinner() {
        val branchAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, branch)
        branchAdapter.setDropDownViewResource(android.R.layout.select_dialog_item)
        binding.branch.adapter = branchAdapter
        binding.branch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                branchS = parent.getItemAtPosition(position).toString()
//                iFSYear = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                iFSYear = 0
            }
        }
    }

}