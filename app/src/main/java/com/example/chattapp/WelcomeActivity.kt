package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser?=null
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        FirebaseApp.initializeApp(this)
        registerbtton.setOnClickListener {
            val intent=Intent(this@WelcomeActivity,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth=FirebaseAuth.getInstance()
        loginbtton.setOnClickListener {
            loginUser()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseUser=FirebaseAuth.getInstance().currentUser
        if(firebaseUser!=null){

            val intent= Intent(this@WelcomeActivity,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
    private fun loginUser() {
        val email=emaillogin.text.toString().trim()
        val password=passlogin.text.toString()
        if(email==""){
            Toast.makeText(this@WelcomeActivity,"Please write email", Toast.LENGTH_SHORT).show()
        }else if(password==""){
            Toast.makeText(this@WelcomeActivity,"Please write password", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent=Intent(this@WelcomeActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@WelcomeActivity,"Error Message:"+it.exception!!.message.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}