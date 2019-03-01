package com.supercaly.photoloader

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Image
import com.supercaly.photoloader.viewholder.AddButtonHolder
import com.supercaly.photoloader.viewholder.ItemHolder
import java.io.File

class PhotoAdapter(private val maxPhotoSize: Int,
                   private val addButton: AddButton):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "PhotoAdapter"
        private const val ITEM_TYPE = 0
        private const val ADD_TYPE = 1
    }

    /*
     * Variable to store all the selected image uri strings
     */
    var uris = mutableListOf<Uri>()
        private set

    /*
     * Method to add images to the uris
     */
    fun addAll(list: List<Image>) {
        //Remove the error from the button if previously there was one
        if (!list.isEmpty() && addButton.error)
            addButton.error = false
        //Add every item to the uris
        list.forEach { uris.add(Uri.fromFile(File(it.path))) }
        //Hide the button if we reach the maximum number of photo
        if (uris.size >= maxPhotoSize)
            addButton.visibility = View.GONE
        notifyDataSetChanged()
    }

    /*
     * Method for restoring the uris from saved state
     */
    fun restoreData(list: List<String>?) {
        list?.let {
            //Remove the error from the button if previously there was one
            if (!list.isEmpty() && addButton.error)
                addButton.error = false
            //Add every item to the uris
            list.forEach { uris.add((Uri.fromFile(File(it)))) }
            //Hide the button if we reach the maximum number of photo
            if (uris.size >= maxPhotoSize)
                addButton.visibility = View.GONE
            notifyDataSetChanged()
        }
    }

    /*
     * Method for removing one item from the uris
     */
    private fun removeItem(s: Uri) {
        if (uris.contains(s)) {
            Log.i(TAG, "removeItem: $s")
            uris.remove(s)
            if (uris.size < maxPhotoSize && addButton.visibility != View.VISIBLE)
                addButton.visibility = View.VISIBLE
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_TYPE -> ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item_layout, parent, false))
            ADD_TYPE -> AddButtonHolder(addButton)
            else -> throw Exception("No type that matches $viewType. Make sure your using types correctly")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder)
            holder.bind(uris[position]) { removeItem(uris[position]) }
    }

    override fun getItemCount(): Int { return uris.size + 1 }

    override fun getItemViewType(position: Int): Int { return if (position == itemCount - 1) ADD_TYPE else ITEM_TYPE }
}