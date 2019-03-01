package com.supercaly.photoloader.viewholder

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.supercaly.photoloader.R

class ItemHolder(private val item: View): RecyclerView.ViewHolder(item) {
    var imageView: ImageView = item.findViewById(R.id.photo_img)
    var remove: ImageView = item.findViewById(R.id.remove_img)

    fun bind(url: Uri, clickListener: ()->Unit) {
        Glide.with(item.context)
            .load(url)
            .apply(RequestOptions().placeholder(R.color.gray))
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(15)))
            .into(imageView)

        remove.setOnClickListener { clickListener() }
    }
}