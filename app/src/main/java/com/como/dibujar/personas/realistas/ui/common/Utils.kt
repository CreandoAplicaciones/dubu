package com.como.dibujar.personas.realistas.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide


class Utils {

    companion object {

        fun image (context: Context, image: String, imageView: ImageView) {
            Glide
                .with(context)
                .load(image)
                .into(imageView)
        }

        fun toast (context: Context, RInt: Int) {
            Toast.makeText(context, context.getString(RInt), Toast.LENGTH_SHORT).show()
        }
        fun shareApp(message:String, activity: Activity){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            activity.startActivity(intent)
        }
    }
}



