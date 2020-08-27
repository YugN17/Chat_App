package com.example.chattapp.Adapter

import android.app.ActionBar
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chattapp.ModelClass.Chat
import com.example.chattapp.ModelClass.ChatList
import com.example.chattapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
                holder.show_text_msg!!.visibility = View.VISIBLE
                holder.rightImageView!!.visibility = View.VISIBLE
                Glide.with(mcontext).load(chat.geturl()).into(holder.rightImageView)
            } else if (!chat.getSender().equals(firebaseUser!!.uid)) {
                holder.show_text_msg!!.visibility = View.VISIBLE
                holder.left_image_view!!.visibility = View.VISIBLE
                Glide.with(mcontext).load(chat.geturl()).into(holder.left_image_view)

            }
        }else{
            holder.show_text_msg!!.text=chat.getMessage()

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


}