package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var toolbar: androidx.appcompat.widget.Toolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        setSupportActionBar(findViewById(R.id.toolbar_log))
        toolbar= findViewById(R.id.toolbar_log)
        supportActionBar!!.title="Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            val intent= Intent(this@LoginActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth=FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email=email_login .text.toString().trim()
        val password=pass_login.text.toString()
        if(email==""){
            Toast.makeText(this@LoginActivity,"Please write email",Toast.LENGTH_SHORT).show()
        }else if(password==""){
            Toast.makeText(this@LoginActivity,"Please write password",Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity,"Error Message:"+it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}