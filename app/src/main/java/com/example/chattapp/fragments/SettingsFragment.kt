package com.example.chattapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    private lateinit var databaseref:DatabaseReference
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_settings, container, false)
firebaseUser= FirebaseAuth.getInstance().currentUser!!
        databaseref=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        databaseref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                if(p.exists()){

                    val user=p.getValue(Users::class.java)
                    if(context!=null){
                        view.profiletext.text=user!!.getUsername()
                        Glide.with(context).load(user.getProfile()).into(view.profilepicture)
                        Glide.with(context).load(user.getCover()).into(view.profilecover)

                    }
                }
            }

        })


        return view
    }


    }
