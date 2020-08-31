package com.example.chattapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.FullImageViewActivity
import com.example.chattapp.ModelClass.Chat
import com.example.chattapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.message_item_left.view.*
import org.w3c.dom.Text

class ChatAdapter (
    mcontext:Context,
mChatList:List<Chat>,
    imageUrl:String
):RecyclerView.Adapter<ChatAdapter.ViewHolder?>(){

    private val mcontext:Context
    private val mChatList:List<Chat>
    private val imageUrl:String
    var firebaseUser:FirebaseUser?=FirebaseAuth.getInstance().currentUser!!
    init {

        this.mcontext=mcontext
        this.mChatList=mChatList
        this.imageUrl=imageUrl
    }


inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    var profile_image:CircleImageView?=null
    var show_text_msg: TextView?=null
    var left_image_view: ImageView?=null
    var seen_txt:TextView?=null
    var rightImageView:ImageView?=null

    init {
        profile_image=itemView.findViewById(R.id.profile_left)
        show_text_msg=itemView.findViewById(R.id.show_text_mssg)
        left_image_view=itemView.findViewById(R.id.item_left_image)
        seen_txt=itemView.findViewById(R.id.seen_text)
        rightImageView=itemView.findViewById(R.id.right_image_view)

    }

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if(viewType==1){
            val view=LayoutInflater.from(mcontext).inflate(R.layout.message_item_right,parent,false)
             ViewHolder(view)
        }else{
            val view=LayoutInflater.from(mcontext).inflate(R.layout.message_item_left,parent,false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat=mChatList[position]
        Glide.with(mcontext).load(imageUrl).into(holder.profile_image)
        if(chat.getMessage().equals("sent you an image") && !chat.geturl().equals("")) {
            if (chat.getSender().equals(firebaseUser!!.uid)) {
                holder.show_text_msg!!.visibility = View.GONE
                holder.rightImageView!!.visibility = View.VISIBLE
                Glide.with(mcontext).load(chat.geturl()).into(holder.rightImageView)

                holder.rightImageView!!.setOnClickListener {
                    val options= arrayOf<CharSequence>("View Full Image","Deleted Image","Cancel")
                    val builder=AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want?")
                    builder.setItems(options,DialogInterface.OnClickListener { dialog, which ->

                        if(which==0){ val intent= Intent(mcontext,FullImageViewActivity::class.java)
                            intent.putExtra("url",chat.geturl())
                            mcontext.startActivity(intent)
                        }else if(which==1){
                            deleteSentMessage(position,holder)

                        }

                    })
                    builder.show()

                }
            } else if (!chat.getSender().equals(firebaseUser!!.uid)) {
                holder.show_text_msg!!.visibility = View.VISIBLE
                holder.left_image_view!!.visibility = View.VISIBLE
                Glide.with(mcontext).load(chat.geturl()).into(holder.left_image_view)

                holder.left_image_view!!.setOnClickListener {
                    val options= arrayOf<CharSequence>("View Full Image","Cancel")
                    val builder=AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("What do you want?")
                    builder.setItems(options,DialogInterface.OnClickListener { dialog, which ->

                        if(which==0){ val intent= Intent(mcontext,FullImageViewActivity::class.java)
                            intent.putExtra("url",chat.geturl())
                            mcontext.startActivity(intent)
                        }

                    })
                    builder.show()

                }

            }
        }else{
            holder.show_text_msg!!.text=chat.getMessage()
            if(firebaseUser!!.uid==chat.getSender()){
            holder.show_text_msg!!.setOnClickListener {
                val options = arrayOf<CharSequence>("Deleted Message", "Cancel")
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("What do you want?")
                builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->

                    if (which == 0) {
                        deleteSentMessage(position, holder)
                    }

                })
                builder.show()
            }

            }

        }

        if(position==mChatList.size-1){
            if(chat.getSeen()) {
                holder.seen_txt!!.text = "Seen"
                if (chat.getMessage().equals("sent you an image") && !chat.geturl().equals("")) {
                    val pp: RelativeLayout.LayoutParams? =
                        holder.seen_txt!!.layoutParams as RelativeLayout.LayoutParams?
                    pp!!.setMargins(0, 245, 10, 0)
                    holder.seen_txt!!.layoutParams = pp
                }
            }else{
                holder.seen_txt!!.text = "Sent"
                if (chat.getMessage().equals("sent you an image") && !chat.geturl().equals("")) {
                    val pp: RelativeLayout.LayoutParams? =
                        holder.seen_txt!!.layoutParams as RelativeLayout.LayoutParams?
                    pp!!.setMargins(0, 245, 10, 0)
                    holder.seen_txt!!.layoutParams = pp
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
       return  if(mChatList[position].getSender().equals(firebaseUser!!.uid)){
         1
        }else{
            0
        }

    }
    private fun deleteSentMessage(position: Int, holder: ChatAdapter.ViewHolder){

        val ref=FirebaseDatabase.getInstance().reference.child("Chats").child(mChatList[position].getMessageId()!!).setValue(null)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("ioo", mChatList[position].toString()+"fjjf")
                    Toast.makeText(holder.itemView.context,"Deleted",Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(holder.itemView.context,"Error Occured",Toast.LENGTH_LONG).show()
                }
            }
    }


}