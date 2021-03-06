package com.example.chattapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chattapp.Adapter.UserAdapter
import com.example.chattapp.ModelClass.ChatList
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.Notifications.Token
import com.example.chattapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId


class ChatFragment : Fragment() {

    private var userAdapter: UserAdapter?=null
    private var mUsers:List<Users>? =null
    private var useChatList:List<ChatList>?=null
    lateinit var recyclerView: RecyclerView
    private var firebaseUser:FirebaseUser?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_chat, container, false)


        recyclerView=view.findViewById(R.id.recycler_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(context)
        firebaseUser=FirebaseAuth.getInstance().currentUser

        useChatList=ArrayList()
        val ref=FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid)
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                (useChatList as ArrayList<ChatList>).clear()
                for(datasnapshot in p0.children){
                    val chatList=datasnapshot.getValue(ChatList::class.java)
                    if (chatList != null) {
                        (useChatList as ArrayList<ChatList>).add(chatList)
                    }

                }
                retriveChatList()

            }


        })

        updateToken(FirebaseInstanceId.getInstance().token)
        return view
    }

    private fun updateToken(token: String?) {
        val ref=FirebaseDatabase.getInstance().reference.child("Tokens")
        val token1=Token(token!!)
        ref.child(firebaseUser!!.uid).setValue(token1)

    }

    private fun retriveChatList(){
        mUsers=ArrayList()
        val ref=FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()

                Log.d("inside on data changed","kk")
                for(datasnapshot in p0.children){
                    val user=datasnapshot.getValue(Users::class.java)
                    Log.d("pp","inside data snapshot")
                    for(eachList in useChatList!!){
                        Log.d("kg",eachList.getId()+"hjhj")
                        if((user!!.getUID()).equals(eachList.getId())) {
                            Log.d("kP",user.getUID()+"JIJH")
                            (mUsers as ArrayList<Users>).add(user)
                            Log.d("huu",user.getUsername())
                        }
                    }


                }
                try {
                    userAdapter = UserAdapter(context!!, mUsers as ArrayList<Users>, true)

                }catch (e:NullPointerException){

                }

                recyclerView.adapter=userAdapter
            }


        })

    }

}