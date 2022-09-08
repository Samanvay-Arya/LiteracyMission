package com.samanvay.literacymission.LogIn

import android.content.ContentValues.TAG
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.samanvay.literacymission.MainActivity
import com.samanvay.literacymission.R
import com.samanvay.literacymission.databinding.ActivityVolunteerLoginBinding

class VolunteerLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVolunteerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ...
       // Initialize Firebase Auth
        auth = Firebase.auth
        binding.buttonLogin.setOnClickListener {
            binding.buttonLogin.visibility=View.GONE
            binding.progressbarLogin.visibility=View.VISIBLE
            signIn(binding.emailLogin.text.toString(),binding.passwordLogin.text.toString())
        }
    }



    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    startActivity(Intent(this@VolunteerLogin,MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    binding.buttonLogin.visibility=View.VISIBLE
                    binding.progressbarLogin.visibility=View.GONE
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }.addOnFailureListener(this){e->
                Toast.makeText(this,e.message+"",Toast.LENGTH_SHORT).show()
            }
        // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }
}