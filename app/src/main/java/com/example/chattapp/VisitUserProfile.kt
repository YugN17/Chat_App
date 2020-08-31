package com.example.chattapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.chattapp.ModelClass.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_visit_user_profile.*

class VisitUserProfile : AppCompatActivity() {
    private var userVisitId:String=""
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
     var user:Users?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_profile)
        setSupportActionBar(findViewById(R.id.toolbar_prof))
        toolbar= findViewById(R.id.toolbar_prof)
        supportActionBar!!.title="Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            val intent= Intent(this@VisitUserProfile,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        userVisitId=intent.getStringExtra("visit_id")
        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(userVisitId)
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p: DataSnapshot) {
                if(p.exists()){
                    user=p.getValue(Users::class.java)
                    profiletextdisplay.text= user!!.getUsername()
                    Glide.with(applicationContext).load(user!!.getProfile()).into(profilepicturedisplay)

                    Glide.with(applicationContext).load(user!!.getCover()).into(profiledisplay)

                }
            }


        })
        facebook_display.setOnClickListener {
            val uri= Uri.parse(user!!.getFacebook())
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)

        }
        insta_display.setOnClickListener {
            val uri= Uri.parse(user!!.getInstagram())
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        web_display.setOnClickListener {
            val uri= Uri.parse(user!!.getWebsite())
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        send_mssg_btt.setOnClickListener {
            val intent=Intent(this@VisitUserProfile,MessageChatActivity::class.java)
            intent.putExtra("visit_id",userVisitId)
            startActivity(intent)
        }
    }
}