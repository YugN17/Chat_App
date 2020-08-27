package com.example.chattapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.Adapter.ChatAdapter
import com.example.chattapp.ModelClass.Chat
import com.example.chattapp.ModelClass.Users
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_message_chat.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MessageChatActivity : AppCompatActivity() {
    var userIdvisit=""
    var firebaseUser:FirebaseUser?=null
    var chatAdapter:ChatAdapter?=null
    var mChatList:List<Chat>?=null
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)
        intent=intent
        userIdvisit=intent.getStringExtra("visit_id")
        recyclerView=findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager=LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd=true
        recyclerView.layoutManager=linearLayoutManager
        firebaseUser=FirebaseAuth.getInstance().currentUser

        val reference=FirebaseDatabase.getInstance().reference.child("Users").child(userIdvisit)
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                val users=p.getValue(Users::class.java)
                username_chat.text=users!!.getUsername()
                Glide.with(applicationContext).load(users.getProfile()).into(profile_chat)
                retreiveMessages(firebaseUser!!.uid,userIdvisit,users.getProfile())

            }


        })
        send_chatt.setOnClickListener {
            val message=edit_text_chat.text.toString()
            if(message==""){
                Toast.makeText(this@MessageChatActivity,"Error Message",Toast.LENGTH_SHORT).show()

            }else{
                sendMessageToUser(firebaseUser!!.uid,userIdvisit,message)
            }
            edit_text_chat.setText("")
        }
        attach_ment.setOnClickListener {
            val intent=Intent();
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(Intent.createChooser(intent,"Pick Image"),438)
        }


    }

    private fun retreiveMessages(senderid: String, receiverid: String?, receiveimageUrl: String?) {

        mChatList=ArrayList()
        val reference=FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for(snapshot in p0.children){
                    val chat=snapshot.getValue(Chat::class.java)
                    if (chat!!.getReceiver().equals(receiverid) && chat.getSender().equals(senderid) || chat!!.getReceiver().equals(senderid) && chat.getSender().equals(receiverid) ){
                        (mChatList as ArrayList<Chat>).add(chat)
                    }
                    chatAdapter= ChatAdapter(this@MessageChatActivity, mChatList as ArrayList<Chat>,
                        receiveimageUrl!!
                    )
                    recyclerView.adapter=chatAdapter

                }
            }


        })




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

        reference.child("Chats").child(msgKey!!).setValue(messageHashMap).addOnCompleteListener {

            if(it.isSuccessful){

                val chatListReference=FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid).child(userIdvisit!!)
                chatListReference.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p: DatabaseError) {

                    }

                    override fun onDataChange(p: DataSnapshot) {
                        if(!p.exists()){
                            chatListReference.child("id").setValue(firebaseUser!!.uid)
                        }
                        val chatListReceiverRef=FirebaseDatabase.getInstance().reference.child("ChatList").child(userIdvisit!!).child(firebaseUser!!.uid)
                        chatListReceiverRef.child("id").setValue(firebaseUser!!.uid)
                    }


                })

                val reference=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)


            }
        }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==438 && resultCode==RESULT_OK && data!=null && data!!.data!=null){
            val loadingBar=ProgressDialog(this)
            loadingBar.setMessage("Please wait,Image is being uploaded")
            loadingBar.show()
            val fileUrl=data.data
            val storageRefrence= FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref=FirebaseDatabase.getInstance().reference
            val msgId=ref.push().key
            val filepath=storageRefrence.child("$msgId.jpg")
            var uploadTask: StorageTask<*>
            uploadTask=filepath.putFile(fileUrl!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{

                if(!it.isSuccessful){

                    it.exception?.let {
                        throw it
                    }
                }

                return@Continuation filepath.downloadUrl
            }).addOnCompleteListener {
                if(it.isSuccessful){
                    val downloadUrl=it.result
                    val url=downloadUrl.toString()
                    val messageHashMap=HashMap<String,Any?>()
                    messageHashMap["sender"]=firebaseUser!!.uid
                    messageHashMap["message"]="sent you an image"
                    messageHashMap["receiver"]=userIdvisit
                    messageHashMap["isseen"]=false
                    messageHashMap["url"]=url
                    messageHashMap["messageId"]=msgId
                    ref.child("Chats").child(msgId!!).setValue(messageHashMap)

                    loadingBar.dismiss()


                }
            }




        }
    }
}