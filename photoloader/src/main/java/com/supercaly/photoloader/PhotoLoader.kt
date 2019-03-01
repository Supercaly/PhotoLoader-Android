package com.supercaly.photoloader

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import java.util.logging.Logger


class PhotoLoader(context: Context, attrs: AttributeSet?): LinearLayout(context, attrs),
    AddButton.OnClickListener {

    companion object {

        const val TAG = "PhotoLoader"

        /*
         * Request code used to get the uris from ImagePicker
         */
        const val REQUEST_CODE = 4321
    }

    /*
     * LayoutManager for the RecyclerView
     */
    private val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    /*
     * RecyclerView
     */
    private var mRecyclerView: RecyclerView = RecyclerView(context)

    /*
     * TextView used for display the title of the PhotoLoader
     */
    private var mTitleView = TextView(context)

    /*
     * Adapter
     */
    private var mAdapter: PhotoAdapter

    /*
     * ImagePicker.Builder
     */
    private var mImagePicker: ImagePicker.Builder? = null

    /*
     * Add Button
     */
    private var mAddButton = AddButton(context)

    /*
     * Configuration class
     */
    private var config= com.supercaly.photoloader.Config.getConfigFromAttrs(context, attrs)

    /*
     * Constructor with only Context
     */
    constructor(context: Context): this(context, null)

    init {
        //Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.photoloader_layout, this, true)
        //Set the orientation of the parent LinearLayout to be vertical
        orientation = VERTICAL

        //Set AddButton drawables
        mAddButton.normalDrawableResource = config.normalBtnDrawableRes
        mAddButton.errorDrawableResource = config.errorBtnDrawableRes
        mAddButton.error = false
        mAddButton.setOnClickListener(this)

        //Set the Title view
        mTitleView = findViewById(R.id.pl_title_tv)
        mTitleView.text = config.title
        mTitleView.textSize = config.titleSize

        //Set the recyclerview
        mRecyclerView = findViewById(R.id.pl_recyclerview)
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = PhotoAdapter(config.maxPhotoNumber, mAddButton)
        mRecyclerView.adapter = mAdapter

        //PhotoLoader initialized
        Log.d(TAG, "init: $TAG Initialized")
    }

    /*
     * Initialize the ImagePicker.Builder
     *
     * Must be called from the Activity's onCreate method,
     * it set the ImagePicker.Builder with my personal parameters.
     *
     * Activity, needed for ImagePicker.with()
     */
    fun setImageBuilder(activity: Activity){
        mImagePicker = ImagePicker.with(activity)
            .setToolbarColor("#2196F3")
            .setStatusBarColor("#1976D2")
            .setToolbarTextColor("#FFFFFF")
            .setToolbarIconColor("#FFFFFF")
            .setProgressBarColor("#1976D2")
            .setBackgroundColor("#FAFAFA")
            .setDoneTitle(config.doneBtnTitle)
            .setLimitMessage(config.limitMessage)
            .setSavePath(config.folderName)
            .setAlwaysShowDoneButton(true)
            .setKeepScreenOn(true)
            .setRequestCode(REQUEST_CODE)
    }

    /*
     * Get the result from onActivityResult of the Activity
     *
     * Check if the requestCode is form PhotoLoader, if the resultCode
     * is RESULT_OK and if there are some uris; in this case get the images
     * from ImagePicker and add all to the adapter.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mAdapter.addAll(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES))
        }
    }

    override fun onClick() {
        if (mImagePicker != null) {
            val currentMaxSize = config.maxPhotoNumber - mAdapter.itemCount + 1
            mImagePicker!!.setMaxSize(currentMaxSize)
            mImagePicker!!.start()
        } else
            throw InitializationException("ImagePicker is not initialized. " +
                    "You should invoke method setImageBuilder")
    }

    /*
     * Returns the selected photos as a list of Uris
     */
    val images: List<Uri>
        get() { return mAdapter.uris }

    /*
     * Returns the selected photos as a list of Strings
     */
    val imagesString: List<String>
        get() { return mAdapter.uris.stringify() }

    /*
     * Returns if the are selected photos
     *
     * If the uris list of the adapter isEmpty the
     * user haven't selected any photo.
     */
    val isEmpty: Boolean
        get() { return mAdapter.uris.isEmpty() }

    /*
     * Set the error for the button
     */
    var error:Boolean
        get() = mAddButton.error
        set(value) { mAddButton.error = value }

    /*
     * Save and restore the state of the Class
     */
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putStringArrayList("imagesStrings", mAdapter.uris.stringify() as ArrayList)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            val bundle = state as Bundle?
            mAdapter.restoreData(bundle!!.getStringArrayList("imagesStrings"))
            newState = bundle.getParcelable("superState")
        }
        super.onRestoreInstanceState(newState)
    }

    /*
     * Inline method to get the images as a list of uris
     */
    inline fun <T> images(body: (List<Uri>)->T): T =  body(images)
}
