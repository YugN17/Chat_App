package com.example.chattapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.MessageChatActivity
import com.example.chattapp.ModelClass.Chat
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.R
import com.example.chattapp.VisitUserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

@Suppress("UNREACHABLE_CODE")
class UserAdapter(
    mContext: Context,
    mUsers:ArrayList<Users>,
    isChatChecked:Boolean
):RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val mContext:Context
    private val mUsers:ArrayList<Users>
    private val isChatChecked:Boolean
    var lastmsg:String=""

    init {
        this.mContext=mContext
        this.mUsers=mUsers
        this.isChatChecked=isChatChecked
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var usernametxt:TextView
        var LastMssgtxt:TextView
        var profileimage:CircleImageView
        var onlineprofile:CircleImageView
        var offlineprofile:CircleImageView

        init{
            usernametxt=itemView.findViewById(R.id.user_name)
            LastMssgtxt=itemView.findViewById(R.id.last_text)
            profileimage=itemView.findViewById(R.id.profooo)
            onlineprofile=itemView.findViewById(R.id.online)
            offlineprofile=itemView.findViewById(R.id.offline)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.user_search_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("jiji", mUsers.size.toString()+"jkjkj")
        return mUsers.size

    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {

        val user:Users=mUsers[position]
        holder.usernametxt.text=user.getUsername()
        Glide.with(mContext).load(user.getProfile()).into(holder.profileimage)

        if(isChatChecked){
            if(user.getStatus()=="online"){
                holder.onlineprofile.visibility=View.VISIBLE
                holder.offlineprofile.visibility=View.GONE

            }else{
                holder.onlineprofile.visibility=View.GONE
                holder.offlineprofile.visibility=View.VISIBLE
            }
            retirieveLastMessage(user.getUID(),holder.LastMssgtxt)

        }else{
            holder.LastMssgtxt.visibility=View.GONE
            holder.onlineprofile.visibility=View.GONE
            holder.offlineprofile.visibility=View.GONE

        }
        holder.itemView.setOnClickListener {
            val options=arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )
            val builder:AlertDialog.Builder=AlertDialog.Builder(mContext)
            builder.setTitle("What do you want?")
            builder.setItems(options,DialogInterface.OnClickListener { dialog, which ->
                if(which==0){
                    val intent= Intent(mContext,MessageChatActivity::class.java)
                    intent.putExtra("visit_id",user.getUID())
                    mContext.startActivity(intent)
                }
                if(which==1){

                    val intent= Intent(mContext,VisitUserProfile::class.java)
                    intent.putExtra("visit_id",user.getUID())
                    mContext.startActivity(intent)
                }
            })
            builder.show()
        }

    }

    private fun retirieveLastMessage(Onlineuid: String?, lastMssgtxt: TextView) {

        lastmsg="defaultMsg"
        val firebaseUser=FirebaseAuth.getInstance().currentUser
        val reference=FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
               for(datasnapshot in p.children){
                   val chat=datasnapshot.getValue(Chat::class.java)
                   if(firebaseUser!=null  && chat!=null){

                       if(chat.getReceiver()==firebaseUser!!.uid && chat.getSender()==Onlineuid ||chat.getReceiver()== Onlineuid && chat.getSender()==firebaseUser.uid ){

                           lastmsg= chat.getMessage()!!
                       }
                   }
               }
                when(lastmsg){
                    "defaultMsg"->lastMssgtxt.text="No Message"
                    "sent you an image"->lastMssgtxt.text="image sent"
                    else->lastMssgtxt.text=lastmsg
                }
                lastmsg="defaultMsg"
            }


        })



    }


}