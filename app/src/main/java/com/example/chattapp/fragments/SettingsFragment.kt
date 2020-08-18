package com.example.chattapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.profilecover

class SettingsFragment : Fragment() {

    private lateinit var databaseref:DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private val RequestCode=438
    private var imageUri:Uri?=null
    private var Storageref:StorageReference?=null
    private var coverChecker:String?=null
    private var socialChecker:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_settings, container, false)
firebaseUser= FirebaseAuth.getInstance().currentUser!!
        databaseref=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        Storageref=FirebaseStorage.getInstance().reference.child("User Images")
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

        view.profilepicture.setOnClickListener {
            pickImage();
        }
        view.profilecover.setOnClickListener {
            coverChecker="cover"
            pickImage()
        }
        view.set_insta.setOnClickListener{
            socialChecker="instagram"
            setSocialLinks()
        }
        view.set_facebook.setOnClickListener{
            socialChecker="facebook"
            setSocialLinks()
        }
        view.set_web.setOnClickListener{
            socialChecker="website"
            setSocialLinks()
        }


        return view
    }

    private fun setSocialLinks() {
        val builder=AlertDialog.Builder(context,R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        if(socialChecker=="website"){
            builder.setTitle("Write URL: ")
        }else{
            builder.setTitle("Write Username:")
        }
        val editText=EditText(context)
        if(socialChecker=="website"){
            editText.hint="eg. www.google.com"
        }else{
            editText.hint="eg. alizeb430"
        }
        builder.setView(editText)
        builder.setPositiveButton("Create",DialogInterface.OnClickListener {
            dialog, which ->
            val str=editText.text.toString()
            if(str==""){
                Toast.makeText(context,"Please write something",Toast.LENGTH_SHORT).show()
            }else{
                saveSocialLinks(str)
            }
        })

        builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{

            dialog, which ->
            dialog.cancel()
        })
        builder.show()
    }

    private fun saveSocialLinks(string: String) {

        val mapSocial=HashMap<String,Any>()
        when(socialChecker){
            "facebook"->{
                mapSocial["facebook"]="https://m.facebook.com/$string"
            }
            "instagram"->{
                mapSocial["instagram"]="https://m.instagram.com/$string"
            }
            "website"->{
                mapSocial["website"]="https://$string"
            }

        }
        databaseref.updateChildren(mapSocial).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImage() {
        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RequestCode && resultCode==Activity.RESULT_OK && data!!.data!=null){

            imageUri=data.data
            Toast.makeText(context,"uploading......",Toast.LENGTH_LONG).show()
            uploadImageToDatabse()


        }

    }

    private fun uploadImageToDatabse() {
        val progressBar=ProgressDialog(context)
        progressBar.setMessage("Image is uploading,Please wait.......")
        progressBar.show()
        if (imageUri!=null){

            val fileref=Storageref!!.child(System.currentTimeMillis().toString()+".jpg")
            var uploadTask:StorageTask<*>
            uploadTask=fileref.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>>{

                if(!it.isSuccessful){

                    it.exception?.let {
                        throw it
                    }
                }

                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener {
                if(it.isSuccessful){


                    val downloadUrl=it.result
                    val url=downloadUrl.toString()
                    if(coverChecker=="cover"){
                        val mapCoverImag=HashMap<String,Any>()
                        mapCoverImag["cover"]=url
                        databaseref.updateChildren(mapCoverImag)
                        coverChecker=""
                    }else{
                        val mapProfImag=HashMap<String,Any>()
                        mapProfImag["profile"]=url
                        databaseref.updateChildren(mapProfImag)
                        coverChecker=""
                    }

                    progressBar.dismiss()
                }
            }

        }
    }


}
