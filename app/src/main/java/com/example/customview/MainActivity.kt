package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Glide.with(this)
                .load(R.drawable.flower)
                //TODO: toPX
                .transform(RoundedCorners(6), MyTransformation(this))
                .into(imageViewBitmap)*/
    }
}
