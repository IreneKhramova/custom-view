package com.example.customview

import android.content.res.Resources

/**
 * Created by Irene Khramova on 19.04.2019.
 */
fun Int.dpToPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return Math.round(this * density)
}