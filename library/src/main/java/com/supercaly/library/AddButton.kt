package com.supercaly.library

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class AddButton(context: Context, attrs: AttributeSet?): SquaredRelativeLayout(context, attrs) {

    var normalDrawableResource: Int = 0

    var errorDrawableResource: Int = 0

    private var mOnClickListener: OnClickListener? = null

    private var mImage: ImageView = ImageView(context)

    var error: Boolean = false
        set(value) {
            field = value
            if (field) mImage.setImageResource(errorDrawableResource) else mImage.setImageResource(normalDrawableResource)
        }

    constructor(context: Context): this(context, null)

    init {
        //Set LinearLayout's layoutParams
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        //Set ImageView's layoutParams and margins
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params.setMargins(15, 15, 15, 15)
        mImage.layoutParams = params

        //Add ImageView to layout
        addView(mImage)

        //Set OnClickListener to ImageView
        mImage.setOnClickListener { mOnClickListener?.onClick() }

        //Initialize the image rosource to normal setting error to false
        error = false
    }

    fun setOnClickListener(l: OnClickListener){
        mOnClickListener = l
    }

    interface OnClickListener {
        fun onClick()
    }
}