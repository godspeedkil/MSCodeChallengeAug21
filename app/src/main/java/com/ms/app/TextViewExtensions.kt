package com.ms.app

import android.view.View
import android.widget.TextView

fun TextView.setTextOrGone(newText: String?) {
    newText?.let {
        text = it
        visibility = View.VISIBLE
    } ?: run {
        visibility = View.GONE
    }
}