package com.example.chattapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.MessageChatActivity
import com.example.chattapp.ModelClass.Users
import com.example.chattapp.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_search_item.view.*

class UserAdapter(
    mContext: Context,
    mUsers:List<Users>,
    isChatChecked:Boolean
):RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val mContext:Context
    private val mUsers:List<Users>
    private val isChatChecked:Boolean

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
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {

        val user:Users=mUsers[position]
        holder.usernametxt.text=user.getUsername()
        Glide.with(mContext).load(user.getProfile()).into(holder.profileimage)
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
            })
        }

    }


}