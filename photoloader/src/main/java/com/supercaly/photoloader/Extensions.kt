package com.supercaly.photoloader

import android.net.Uri

fun MutableList<Uri>.stringify(): List<String> {
    val returnList = mutableListOf<String>()
    forEach {
        returnList.add(it.path!!)
    }
    return returnList
}