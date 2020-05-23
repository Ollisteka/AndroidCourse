package com.example.androidcourse

import android.content.Context
import android.widget.Toast

fun showToast(context: Context?, textId: Int, gravity: Int? = null) {
    val toast = Toast.makeText(context, context?.resources?.getString(textId), Toast.LENGTH_SHORT)
    if (gravity != null)
        toast.setGravity(gravity, 0, 0)
    toast.show()
}