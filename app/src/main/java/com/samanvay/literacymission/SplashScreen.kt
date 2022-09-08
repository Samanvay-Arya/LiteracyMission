package com.samanvay.literacymission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.samanvay.literacymission.LogIn.VolunteerLogin

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Initialize Firebase Auth
        auth = Firebase.auth
        Handler().postDelayed({
            checkUser()
        }, 1500)
    }
    private fun checkUser() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
           startActivity(Intent(this@SplashScreen,MainActivity::class.java))
            finish()
        }
        else{
            startActivity(Intent(this@SplashScreen,VolunteerLogin::class.java))
            finish()
        }
    }
}