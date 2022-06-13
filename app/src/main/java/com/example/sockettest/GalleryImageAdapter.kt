package com.example.sockettest

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class GalleryImageAdapter(private val context: Context, private val images: List<File>) :
    RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryImageAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(
            R.layout.image_layout, parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: GalleryImageAdapter.ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageView)
        }

        fun bind(image: File) {

            GlideApp.with(context)
                .load(image)
                .into(imageView)

            imageView.setOnClickListener {
                val intent = Intent(context, ShowImageActivity::class.java).apply {
                    putExtra("imageFile", image)
                }

                context.startActivity(intent)
            }
        }
    }
}