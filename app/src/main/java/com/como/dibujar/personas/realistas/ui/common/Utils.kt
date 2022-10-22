package com.como.dibujar.personas.realistas.ui.common

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide


class Utils {

    companion object {

        fun image (context: Context, image: String, imageView: ImageView) {
            Glide
                .with(context)
                .load(image)
                .into(imageView)
        }

    }
}



