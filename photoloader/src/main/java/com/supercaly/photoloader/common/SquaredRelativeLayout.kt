package com.supercaly.photoloader.common

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

open class SquaredRelativeLayout(context: Context, attributeSet: AttributeSet?): RelativeLayout(context, attributeSet) {

    constructor(context: Context): this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec)
    }
}