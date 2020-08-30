package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class FullImageViewActivity : AppCompatActivity() {
    private lateinit var image_viewer:ImageView
    private  var imageUrl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image_view)
        image_viewer=findViewById(R.id.image_viewer)
        imageUrl=intent.getStringExtra("url")
        Glide.with(this).load(imageUrl).into(image_viewer)
    }
}