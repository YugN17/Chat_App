package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var mAuth:FirebaseAuth
    private lateinit var refusers:DatabaseReference
    private var firebaseUserID :String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(this)
        setSupportActionBar(findViewById(R.id.toolbar_reg))
        toolbar= findViewById(R.id.toolbar_reg)
        supportActionBar!!.title="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            val intent= Intent(this@RegisterActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth=FirebaseAuth.getInstance()
        register_butt.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username=username.text.toString()
        val email=email .text.toString().trim()
        val password=passw.text.toString()
        if(username==""){
            Toast.makeText(this@RegisterActivity,"Please write username ",Toast.LENGTH_SHORT).show()

        }else if(email==""){
            Toast.makeText(this@RegisterActivity,"Please write email",Toast.LENGTH_SHORT).show()

        }else if(password==""){
            Toast.makeText(this@RegisterActivity,"Please write password",Toast.LENGTH_SHORT).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {

                if(it.isSuccessful){
                    firebaseUserID=mAuth.currentUser!!.uid
                    refusers=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUserID)

                    val userHashMap=HashMap<String,Any>()
                    userHashMap["uid"]=firebaseUserID
                    userHashMap["username"]=username
                    userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/chat-app-48607.appspot.com/o/ic_profile.png?alt=media&token=bbbf2a1a-4724-4b4c-a67e-55e6dd15a4e3"
                    userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/chat-app-48607.appspot.com/o/getty_509107562_2000133320009280346_351827.jpg?alt=media&token=082024f8-4a9c-48f2-bc0c-133004658140"
                    userHashMap["status"]="offline"
                    userHashMap["search"]=username.toLowerCase()
                    userHashMap["facebook"]="https://m.facebook.com"
                    userHashMap["instagram"]="https://m.instagram.com"
                    userHashMap["website"]="https://www.google.com"

                    refusers.updateChildren(userHashMap).addOnCompleteListener {
                       if(it.isSuccessful){
                           val intent=Intent(this@RegisterActivity,MainActivity::class.java)
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                           startActivity(intent)
                           finish()
                       }
                    }
                }else{
                    Toast.makeText(this@RegisterActivity,"Error Message:"+it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                }
            }
        }

    }
}