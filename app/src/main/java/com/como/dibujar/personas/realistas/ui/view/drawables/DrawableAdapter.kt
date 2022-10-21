package com.como.dibujar.personas.realistas.ui.view.drawables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.como.dibujar.personas.realistas.databinding.ItemImageBinding
import com.como.dibujar.personas.realistas.model.MainImage


class DrawableAdapter(private var images: List<MainImage>,
                      private val listener: (Int) -> Unit
                           ): RecyclerView.Adapter<DrawableAdapter.ImagesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImagesHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val item = images[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = images.size

    class ImagesHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: MainImage, listener: (Int) -> Unit) = with(binding) {

            Glide
                .with(itemView.context)
                .load(image.image)
                .centerCrop()
                .into(imgRv)

            itemView.setOnClickListener { listener(image.id) }
        }
    }
}








