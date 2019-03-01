package com.supercaly.photoloader

import android.content.Context
import android.util.AttributeSet

class Config(var title: String = DEFAULT_TITLE_TXT,
             var titleSize: Float = DEFAULT_TITLE_SIZE,
             var maxPhotoNumber: Int = DEFAULT_MAX_PHOTO_NUM,
             var folderName: String = PhotoLoader.TAG,
             var doneBtnTitle: String = DEFAULT_DONE_BTN_TITLE,
             var limitMessage: String = DEFAULT_LIMIT_MSG,
             var normalBtnDrawableRes: Int = 0,
             var errorBtnDrawableRes: Int = 0) {

    companion object {
        /*
         * Default title
         */
        private const val DEFAULT_TITLE_TXT = "Select images"

        private const val DEFAULT_TITLE_SIZE = 18f
        /*
         * Default value for the maximum photo number
         */
        private const val DEFAULT_MAX_PHOTO_NUM = 5

        /*
         * Default title form the done button
         */
        private const val DEFAULT_DONE_BTN_TITLE = "Done"

        /*
         * Default text shown when reached maximum selection number
         */
        private const val DEFAULT_LIMIT_MSG = "You have reached selection limit"

        fun getConfigFromAttrs(context: Context, attrs: AttributeSet?): Config {
            val conf = Config()
            attrs?.let {
                val typedArray = context.obtainStyledAttributes(it, R.styleable.PhotoLoader, 0, 0)
                try {
                    val titleString = typedArray.getString(R.styleable.PhotoLoader_pl_title)
                    val folder = typedArray.getString(R.styleable.PhotoLoader_pl_folder_name)
                    val done = typedArray.getString(R.styleable.PhotoLoader_pl_done_button_title)
                    val limit = typedArray.getString(R.styleable.PhotoLoader_pl_limit_message)

                    conf.title = titleString ?: DEFAULT_TITLE_TXT
                    conf.titleSize = typedArray.getFloat(R.styleable.PhotoLoader_pl_title_size, DEFAULT_TITLE_SIZE)
                    conf.maxPhotoNumber = typedArray.getInteger(R.styleable.PhotoLoader_pl_max_photo_num, DEFAULT_MAX_PHOTO_NUM)
                    conf.folderName = folder ?: PhotoLoader.TAG
                    conf.doneBtnTitle = done ?: DEFAULT_DONE_BTN_TITLE
                    conf.limitMessage = limit ?: DEFAULT_LIMIT_MSG
                    conf.normalBtnDrawableRes = typedArray.getResourceId(R.styleable.PhotoLoader_pl_button_drawable, R.drawable.ic_add_button)
                    conf.errorBtnDrawableRes = typedArray.getResourceId(R.styleable.PhotoLoader_pl_error_button_drawable, R.drawable.ic_ad_button_error)

                } finally {
                    typedArray.recycle()
                }
            }
            return conf
        }
    }
}