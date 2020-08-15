package com.example.chattapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.fragments.ChatFragment
import com.example.chattapp.fragments.SearchFragment
import com.example.chattapp.fragments.SettingsFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar;
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    var refusers:DatabaseReference?=null
    var firebaseUser:FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setSupportActionBar(findViewById(R.id.toolbar))
        toolbar= findViewById(R.id.toolbar)
        supportActionBar!!.title=""
        tabLayout=findViewById(R.id.tablayout)
        viewPager=findViewById(R.id.view_pager)

        val viewpageradapter=ViewPagerAdapter(supportFragmentManager)

        viewpageradapter.addFragment(ChatFragment(),"Chatt")
        viewpageradapter.addFragment(SearchFragment(),"Search")
        viewpageradapter.addFragment(SettingsFragment(),"Settings")

        viewPager.adapter=viewpageradapter
        tabLayout.setupWithViewPager(viewPager)
        firebaseUser=FirebaseAuth.getInstance().currentUser
        refusers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refusers!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(pp: DataSnapshot) {

                val users: Users?=pp.getValue(Users::class.java)
                user_name.text=users?.getUsername()
                Glide.with(this@MainActivity).load(users?.getProfile()).into(profile_image)
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> { FirebaseAuth.getInstance().signOut()

                val intent= Intent(this@MainActivity,WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    internal class  ViewPagerAdapter(fragmenManager: FragmentManager): FragmentPagerAdapter(fragmenManager){
        private val fragments:ArrayList<Fragment>
        private val titles:ArrayList<String>


        init {
            fragments=ArrayList<Fragment>()
            titles=ArrayList<String>()
        }

        override fun getItem(position: Int): Fragment {

            return fragments[position]

        }

        override fun getCount(): Int {
            return fragments.size }


        fun addFragment(fragment: Fragment, title:String){

            fragments.add(fragment)
            titles.add(title)

        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]

        }
    }
}