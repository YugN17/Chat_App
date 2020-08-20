package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity : AppCompatActivity() {
    var userIdvisit=""
    var firebaseUser:FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)
        val intent= Intent()
        userIdvisit=intent.getStringExtra("visit_id")
        firebaseUser=FirebaseAuth.getInstance().currentUser

        send_chatt.setOnClickListener {
            val message=edit_text_chat.text.toString()
            if(message==""){
                Toast.makeText(this@MessageChatActivity,"Error Message",Toast.LENGTH_SHORT).show()

            }else{
                sendMessageToUser(firebaseUser!!.uid,userIdvisit,message)
            }

        }


    }

    private fun sendMessageToUser(
        uid: String,
        userIdvisit: String?,
        message: String
    ) {

        val reference=FirebaseDatabase.getInstance().reference
        val msgKey=reference.push().key


        val messageHashMap=HashMap<String,Any?>()
        messageHashMap["sender"]=uid
        messageHashMap["message"]=message
        messageHashMap["receiver"]=userIdvisit
        messageHashMap["isseen"]=false
        messageHashMap["url"]=""
        messageHashMap["messageId"]=msgKey

        reference.child("Chats").child(msgKey!!).setValue(messageHashMap)





    }
}