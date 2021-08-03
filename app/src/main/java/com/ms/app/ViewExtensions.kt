package com.ms.app

import android.view.View

fun View.show(shouldShow: Boolean) {
    visibility = if (shouldShow) {
        View.VISIBLE
    } else {
        View.GONE
    }
}