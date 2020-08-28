package com.example.chattapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chattapp.Adapter.UserAdapter
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
private lateinit var userAdapter: UserAdapter
    private lateinit var mUsers:List<Users>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView=view.findViewById(R.id.searchList)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(context)
        mUsers=ArrayList()
        retrieveAllUsers()
        searchEditText=view.findViewById(R.id.searchuser)
        searchEditText.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUsers(s.toString().toLowerCase())
            }

        })
        return view
    }
    private fun searchForUsers(string: String){
var firebaseUserId=FirebaseAuth.getInstance().currentUser!!.uid
        val queryUser=FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search").startAt(string).endAt(string+"\uff8f")

        queryUser.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {

                (mUsers as ArrayList<Users>).clear()

                    for (snapshot in p.children) {

                        val user: Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getUID()).equals(firebaseUserId)) {
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsers as ArrayList<Users>, false)
                    recyclerView.adapter = userAdapter
                }


        })


    }

    private fun retrieveAllUsers() {
        var firebaseUser=FirebaseAuth.getInstance().currentUser!!.uid
        val refusers=FirebaseDatabase.getInstance().reference.child("Users")
        refusers.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if (searchuser.text.toString() == "") {
                    for (snapshot in p.children) {

                        val user: Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getUID()).equals(firebaseUser)) {
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsers as ArrayList<Users>, false)
                    recyclerView.adapter = userAdapter
                }
            }

        })
    }


}