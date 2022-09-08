package com.samanvay.literacymission

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.samanvay.literacymission.AllMembers.AllActivity
import com.samanvay.literacymission.AllMembers.Students.AllStudents
import com.samanvay.literacymission.AllMembers.Volunteer.AllVolunteer
import com.samanvay.literacymission.LogIn.VolunteerLogin
import com.samanvay.literacymission.Registration.StudentRegistration
import com.samanvay.literacymission.Registration.VolunteerRegistration1
import com.samanvay.literacymission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding:ActivityMainBinding

    private lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)



        mainBinding.studentImageView.setOnClickListener{
            startActivity(Intent(this, AllStudents::class.java))
        }
        mainBinding.activityImageView.setOnClickListener{
            startActivity(Intent(this,AllActivity::class.java))
        }
        mainBinding.alumniImageView.setOnClickListener{
            startActivity(Intent(this,StudentRegistration::class.java))
        }
        mainBinding.volunteerImageView.setOnClickListener{
            startActivity(Intent(this,AllVolunteer::class.java))
        }
        setSupportActionBar(mainBinding.Toolbar)

        setDrawer()

    }

    private fun setDrawer() {
        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            mainBinding.drawerLayout,
            mainBinding.Toolbar,
            R.string.navigation_open,
            R.string.navigation_close
        )
        toggle.drawerArrowDrawable.isSpinEnabled=true
        toggle.isDrawerIndicatorEnabled=true
        mainBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mainBinding.drawerNavigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.drawer_register_student->{
                    startActivity(Intent(this@MainActivity,StudentRegistration::class.java))
                }
                R.id.drawer_register_volunteer->{
                    startActivity(Intent(this@MainActivity,VolunteerRegistration1::class.java))
                }
                R.id.drawer_logout->{
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@MainActivity,VolunteerLogin::class.java))
                }

            }
            true

        }
    }


}