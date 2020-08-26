package com.example.chattapp.ModelClass

class Chat{

    private var sender:String=""
    private var message:String=""
    private var receiver:String=""
    private var isSeen:String=""
    private var url:String=""
    private var messageId:String=""

    constructor()
    constructor(
        sender: String,
        message: String,
        receiver: String,
        isSeen: String,
        url: String,
        messageId: String
    ) {
        this.sender = sender
        this.message = message
        this.receiver = receiver
        this.isSeen = isSeen
        this.url = url
        this.messageId = messageId
    }

    fun getSender():String?{
        return sender
    }
fun setSender(sender: String){
    this.sender=sender
}
    fun getMessage():String?{
        return message
    }
    fun setMessage(message: String){
        this.message=message
    }
    fun getReceiver():String?{
        return receiver
    }
    fun setReceiver(receiver: String){
        this.receiver=receiver
    }
    fun getSeen():String?{
        return isSeen
    }
    fun setSeen(isSeen: String){
        this.isSeen=isSeen
    }
    fun geturl():String?{
        return url
    }
    fun setUrl(url: String){
        this.url=url
    }
    fun getMessageId():String?{
        return messageId
    }
    fun setMessageId(sender: String){
        this.messageId=messageId
    }
}