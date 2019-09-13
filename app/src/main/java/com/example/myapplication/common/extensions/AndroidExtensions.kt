package com.example.myapplication.common.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat

inline fun onMarshmallowAndAbove(yesAction: () -> Unit, noAction: () -> Unit) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) yesAction() else noAction()
}

inline fun View.onClick(crossinline onClickAction: () -> Unit) {
  setOnClickListener { onClickAction() }
}

inline fun Context.checkPermission(permission: String, positiveAction: () -> Unit, negativeAction: () -> Unit) {
  onMarshmallowAndAbove(
    yesAction = {
      if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        negativeAction() // permission is not granted, ask for it
      else
        positiveAction()
    }, noAction = { positiveAction() })
}