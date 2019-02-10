package com.supercaly.library

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Image
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
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
    var data: ArrayList<String> = ArrayList()
        private set

    /*
     * Method to add a list the data
     */
    fun addAll(list: List<Image>) {
        if (!list.isEmpty() && addButton.error) addButton.error = false
        if (data.size >= maxPhotoSize) addButton.visibility = View.GONE
        Log.d(TAG, "addAll: $list")
        for (img in list) data.add(img.path)
        notifyDataSetChanged()
    }

    /*
     * Method for restoring the data from saved state
     */
    fun restoreData(list: List<String>?) {
        list?.let {
            if (!it.isEmpty() && addButton.error) addButton.error = false
            if (data.size >= maxPhotoSize) addButton.visibility = View.GONE
            Log.d(TAG, "restoreData: $list")
            for (img in it) data.add(img)
            notifyDataSetChanged()
        }
    }

    /*
     * Method for removing one item from the data
     */
    private fun removeItem(s: String) {
        Log.d(TAG, "removeItem: $s")
        data.remove(s)
        if (data.size < maxPhotoSize && addButton.visibility != View.VISIBLE) addButton.visibility = View.VISIBLE
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_TYPE -> ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item_layout, parent, false))
            ADD_TYPE -> AddButtonHolder(addButton)
            else -> throw Exception("No type that matches $viewType. Make sure your using types correctly")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            Picasso.get().load(File(data[position]))
                .placeholder(R.color.gray)
                .transform(RoundedCornersTransformation(15, 0))
                .fit()
                .centerCrop()
                .into(holder.imageView)

            holder.remove.setOnClickListener { removeItem(data[position]) }
        }
    }

    override fun getItemCount(): Int { return data.size + 1 }

    override fun getItemViewType(position: Int): Int { return if (position == itemCount - 1) ADD_TYPE else ITEM_TYPE }

    /*
     * Class that hold the Item type view
     */
    class ItemHolder(item: View): RecyclerView.ViewHolder(item) {
        var imageView: ImageView = item.findViewById(R.id.photo_img)
        var remove: ImageView = item.findViewById(R.id.remove_img)
    }

    /*
     * Class that hold the AddButton view
     */
    class AddButtonHolder(item: View): RecyclerView.ViewHolder(item)
}