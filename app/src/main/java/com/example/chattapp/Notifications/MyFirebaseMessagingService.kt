package com.example.chattapp.Notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.chattapp.MessageChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(mRemote: RemoteMessage) {
        super.onMessageReceived(mRemote)
        val sented=mRemote.data["sented"]
        val user=mRemote.data["user"]

        val sharedpref=getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val currentOnlineUser=sharedpref.getString("currentUser","None")

        val firebaseuser= FirebaseAuth.getInstance().currentUser
        if(firebaseuser!=null && sented==firebaseuser.uid){

            if(currentOnlineUser!=null){

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    sendOreoNotifications(mRemote)
                }else{
                    sendNotifications(mRemote)
                }
            }

        }


    }


    private fun sendNotifications(mRemote: RemoteMessage) {
        val user=mRemote.data["user"]
        val icon=mRemote.data["icon"]
        val title=mRemote.data["title"]
        val body=mRemote.data["body"]
        val notification=mRemote.notification
        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
        val intent= Intent(this,MessageChatActivity::class.java)


        val bundle=Bundle()
        bundle.putString("userId",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)

        val defSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
     val builder=NotificationCompat.Builder(this)
         .setSmallIcon(icon!!.toInt())
         .setContentTitle(title)
         .setContentText(body)
         .setAutoCancel(true)
         .setSound(defSound)
         .setContentIntent(pendingIntent)


        val noti=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager






        var i=0;
        if(j>0){
            i=j;
        }

        noti.notify(i,builder.build())



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendOreoNotifications(mRemote: RemoteMessage) {

        val user=mRemote.data["user"]
        val icon=mRemote.data["icon"]
        val title=mRemote.data["title"]
        val body=mRemote.data["body"]
        val notification=mRemote.notification
        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
        val intent= Intent(this,MessageChatActivity::class.java)


        val bundle=Bundle()
        bundle.putString("userId",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)

        val defSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreoNotification=OreoNotification(this)
        val builder=oreoNotification.getOreoNotificationChannel(title,body,pendingIntent,defSound,icon)




        var i=0;
        if(j>0){
            i=j;
        }

        oreoNotification.getManager!!.notify(i,builder.build())



    }


}