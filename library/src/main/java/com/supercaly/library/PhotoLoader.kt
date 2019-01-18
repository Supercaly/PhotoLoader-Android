package com.supercaly.library

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker


class PhotoLoader(context: Context, attrs: AttributeSet?): LinearLayout(context, attrs), AddButton.OnClickListener {

    companion object {
        private const val TAG = "PhotoLoader"

        /**
         * Default value for the maximum photo number
         */
        private const val DEFAULT_MAX_PHOTO_NUM = 5

        /**
         * Default title form the done button
         */
        private const val DEFAULT_DONE_BTN_TITLE = "Done"

        /**
         * Default text shown when reached maximum selection number
         */
        private const val DEFAULT_LIMIT_MSG = "You have reached selection limit"

        /**
         * Request code used to get the data from ImagePicker
         */
        const val REQUEST_CODE = 4321
    }

    /**
     * LayoutManager for the RecyclerView
     */
    private val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    /**
     * RecyclerView
     */
    private var mRecyclerView: RecyclerView = RecyclerView(context, attrs)

    /**
     * Adapter
     */
    private var mAdapter: PhotoAdapter

    /**
     * Maximum Photo Number
     *
     * <p> State the maximum selectable images,
     * it can be assigned from code or from XML. </p>
     */
    var maxPhotoNumber: Int = DEFAULT_MAX_PHOTO_NUM

    /**
     * Folder Name
     *
     * <p> Rapresent the name of the folder in witch the
     * photos taken by the ImagePicker will be saved.
     * It can be set by code or by XML. </p>
     */
    var folderName: String? = TAG

    /**
     * Text for the done button
     *
     */
    var doneBtnTitle: String? = DEFAULT_DONE_BTN_TITLE

    /**
     * Text for the limit message
     */
    var limitMessage: String? = DEFAULT_LIMIT_MSG

    /**
     * {@link ImagePicker.Builder}
     */
    private var mImagePicker: ImagePicker.Builder? = null

    private var mAddButton = AddButton(context)

    /**
     * Constructor with only Context
     */
    constructor(context: Context): this(context, null)

    init {
        //Get the max photo number and the drawable resources from attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PhotoLoader, 0, 0)
            try {
                maxPhotoNumber = typedArray.getInteger(R.styleable.PhotoLoader_max_photo_num, DEFAULT_MAX_PHOTO_NUM)
                val folder = typedArray.getString(R.styleable.PhotoLoader_folder_name)
                val done = typedArray.getString(R.styleable.PhotoLoader_done_button_title)
                val limit = typedArray.getString(R.styleable.PhotoLoader_limit_message)
                folderName = folder ?: TAG
                doneBtnTitle = done ?: DEFAULT_DONE_BTN_TITLE
                limitMessage = limit ?: DEFAULT_LIMIT_MSG
            } finally {
                typedArray.recycle()
            }
        }

        //Set the AddButton
        mAddButton.setOnClickListener(this)

        //Set the recyclerview
        mRecyclerView.id = View.generateViewId()
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = PhotoAdapter(maxPhotoNumber, mAddButton)
        mRecyclerView.adapter = mAdapter

        //Add the recyclerview to the LinearLayout
        addView(mRecyclerView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    130f,
                    context.resources.displayMetrics)
                    .toInt()
            )
        )

        //PhotoLoader initialized
        Log.d(TAG, "init: $TAG Initialized with folder: $folderName and maximum photo number: $maxPhotoNumber")
    }

    /**
     * Initialize the ImagePicker.Builder
     *
     * <p> Must be called from the Activity's onCreate method,
     * it set the ImagePicker.Builder with my personal parameters. </p>
     *
     * @param activity Activity, needed for ImagePicker.with()
     */
    fun setImageBuilder(activity: Activity){x
        mImagePicker = ImagePicker.with(activity)
            .setToolbarColor("#2196F3")
            .setStatusBarColor("#1976D2")
            .setToolbarTextColor("#FFFFFF")
            .setToolbarIconColor("#FFFFFF")
            .setProgressBarColor("#1976D2")
            .setBackgroundColor("#1976D2")
            .setDoneTitle(doneBtnTitle)
            .setLimitMessage(limitMessage)
            .setSavePath(folderName)
            .setAlwaysShowDoneButton(true)
            .setKeepScreenOn(true)
            .setRequestCode(REQUEST_CODE)
    }

    /**
     * Get the result from onActivityResult of the Activity
     *
     * <p> Check if the requestCode is form PhotoLoader, if the resultCode
     * is RESULT_OK and if there are some data; in this case get the images
     * from ImagePicker and add all to the adapter. </p>
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mAdapter.addAll(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES))
        }
    }

    override fun onClick() {
        if (mImagePicker != null) {
            val currentMaxSize = maxPhotoNumber - mAdapter.itemCount + 1
            mImagePicker!!.setMaxSize(currentMaxSize)
            mImagePicker!!.start()
        } else {
            throw IllegalArgumentException("ImagePicker is not initialized. " +
                    "You should invoke method setImageBuilder")
        }
    }

    /**
     * Returns the selected photos
     *
     * <p> Variable for getting the path of each selected
     * photo in the ImagePicker. </p>
     *
     * @return ArrayList<String>
     */
    val images: ArrayList<String>
        get() { return mAdapter.data }

    /**
     * Returns if the are selected photos
     *
     * <p> If the data list of the adapter isEmpty the
     * user haven't selected any photo. </p>
     * @return Boolean
     */
    val isEmpty: Boolean
        get() { return mAdapter.data.isEmpty() }

    /**
     * Set the error of the
     */
    var error:Boolean
        get() = mAddButton.error
        set(value) { mAddButton.error = value }

    /**
     * Inline method to get the image
     *
     * <p> This method set the error if the data is empty and
     * pass the data as ArrayList or null to the callback. </p>
     *
     */
    inline fun <T> images(body: (ArrayList<String>?)->T): T {
        if (isEmpty) {
            error = true
            return body(null)
        }
        error = false
        return body(images)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putStringArrayList("imagesStrings", mAdapter.data)
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
}